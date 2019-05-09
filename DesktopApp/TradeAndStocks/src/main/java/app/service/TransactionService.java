/*
 *    Copyright 2018-2019 Jacek Papis, Michał Piątek
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *    Data provided for free by IEX https://iextrading.com/developer/.
 *    View IEX’s Terms of Use https://iextrading.com/api-exhibit-a/.
 */

package app.service;

import app.CustomMessages;
import app.api.StockDataService;
import app.model.Transaction;
import app.model.User;
import app.repository.TransactionRepository;
import app.view.table.StockInfoTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserService userService;
    public List<Transaction> getUsersTransactionList(User user) {
        return transactionRepository.findByUser(user);
    }
    public List<Transaction> getUsersActiveTransactionList(User user) {
        return transactionRepository.findByUserAndDoesExistsTrue(user);
    }
    public List<Transaction> getUsersActiveTransactionListOfOneStock(User user,String shortName) {
        return transactionRepository.findByUserAndShortNameAndDoesExistsTrue(user,shortName);
    }

    public void clearUsersTransactions(User user) {
        List<Transaction> list = transactionRepository.findByUserAndDoesExistsTrue(user);
        list.forEach(transaction->{
            transaction.setDoesExists(false);
            transactionRepository.save(transaction);
        });
    }
    public CustomMessages makeTransaction(Transaction transaction, boolean isBuy, double totalTransactionValue) {
        User myUser = UserService.getActiveUser();
        double availableFunds = myUser.getFunds();
        double nettoTransactionValue = transaction.getUnitPrice() * transaction.getUnits();
        //round results
        nettoTransactionValue = Math.round(nettoTransactionValue * 100.0) / 100.0;
        totalTransactionValue = Math.round(totalTransactionValue * 100.0) / 100.0;
        if (isBuy) {
            if ((totalTransactionValue) <= availableFunds) {
                myUser.setFunds(myUser.getFunds() - (totalTransactionValue));
                userService.updateUser(myUser);
                transactionRepository.save(transaction);
                return CustomMessages.Success;
            } else {
                return CustomMessages.NotEnoughFunds;
            }
        } else {
            List<Transaction> list = getUsersActiveTransactionListOfOneStock(UserService.getActiveUser(),transaction.getShortName());
            double currentPortfolioUnits = calculateCurrentlyOwnedStockUnits(list);

           // if((currentPortfolioUnits*transaction.getUnitPrice())>=nettoTransactionValue) {
            /*
            DecimalFormat df=new DecimalFormat("0.000000");
            String formate = df.format(currentPortfolioUnits);
            try {
                currentPortfolioUnits = (Double)df.parse(formate) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
            currentPortfolioUnits = Math.round(currentPortfolioUnits*1000000)/1000000.00;


            System.out.println("UNITS: " + currentPortfolioUnits + " || " + transaction.getUnits() );
            if(currentPortfolioUnits>=transaction.getUnits()) {

                myUser.setFunds(myUser.getFunds() + totalTransactionValue);
                userService.updateUser(myUser);
                transactionRepository.save(transaction);
                return CustomMessages.Success;
            } else {
                return CustomMessages.NothingToSell;
            }

        }
    }

    public ArrayList<StockInfoTable> populateStockInfoTable(User activeUser, String shortName, double currentUnitPrice) {
        ArrayList<StockInfoTable> result = new ArrayList<>();
        List<Transaction> list = transactionRepository.findByUserAndShortNameAndDoesExistsTrue(activeUser,shortName);
        list.forEach(transaction-> {
            StockInfoTable stockInfoTable = new StockInfoTable();
            stockInfoTable.setUnitPriceAtTheDayOfPurchase(transaction.getUnitPrice());
            stockInfoTable.setDateOfTransaction(transaction.getDate());

            // w ifiestockInfoTable.setValueAtTheDayOfPurchase(transaction.getUnits()*transaction.);
            int sell = 0;
            if(transaction.isBuy()) {
                stockInfoTable.setUnits(transaction.getUnits());
                String str = String.format("%.2f",transaction.getUnits()*transaction.getUnitPrice());
                stockInfoTable.setValueAtTheDayOfPurchase(Double.parseDouble(str));
                str = String.format("%.2f",transaction.getUnits()*currentUnitPrice);
                stockInfoTable.setCurrentValue(Double.parseDouble(str));
                sell = -1;
            } else {
                stockInfoTable.setUnits(-transaction.getUnits());
                stockInfoTable.setValueAtTheDayOfPurchase(-(transaction.getUnits()*transaction.getUnitPrice()));
                stockInfoTable.setCurrentValue(-(transaction.getUnits()*currentUnitPrice));
                sell = 1;
            }
            stockInfoTable.setProfitLoss(String.format("%.2f",sell*((1-(stockInfoTable.getCurrentValue()/stockInfoTable.getValueAtTheDayOfPurchase()))*100)) + "%");
            result.add(stockInfoTable);
        });
        return result;
    }

    public double calculateHandlingFee(double transactionValue, double fee) {
        return transactionValue * fee;
    }

    public double calculateProfitLoss(String stockName,User user) {
        // calculating  profit or loss %


        List<Transaction> list = getUsersActiveTransactionListOfOneStock(user,stockName);
        double unitSum = calculateCurrentlyOwnedStockUnits(list);
        double currentUnitPrice = StockDataService.getLatestPrice(stockName).getPrice();
        double sumOfBuyTransactions = list.stream().filter(Transaction::isBuy).mapToDouble(x -> x.getUnitPrice() * x.getUnits()).sum();
        double profitLoss =  ((unitSum * currentUnitPrice)/(sumOfBuyTransactions)) - 1;
        System.out.println(profitLoss);
        return profitLoss;
        /*






        double valueBuy = list.stream().filter(Transaction::isBuy).mapToDouble(x -> x.getUnitPrice() * x.getUnits()).sum();
        double valueSell = list.stream().filter(transaction -> !transaction.isBuy()).mapToDouble(x -> x.getUnitPrice() * x.getUnits()).sum();


        double profitLoss =  -(1 - (valueSell + (currentUnitPrice*unitSum))/valueBuy);
        System.out.println(profitLoss);*/
       // return profitLoss;

    }

    public double calculateCurrentlyOwnedStockUnits(List<Transaction> oneStockUserTransactions) {


        double unitSumBuy = oneStockUserTransactions.stream().filter(Transaction::isBuy).mapToDouble(Transaction::getUnits).sum();
        // all sell transactions
        double unitSumSell = oneStockUserTransactions.stream().filter(transaction -> !transaction.isBuy()).mapToDouble(Transaction::getUnits).sum();
        return unitSumBuy - unitSumSell;

    }
    public double calculateTotalValue(boolean isBuy,double value, double handlingFee, double profitLoss,User user) {
        if(isBuy) {
            return value + handlingFee;
        } else {
            return value - user.getBroker().getProfitMargin()*profitLoss - user.getBroker().getCountry().getTaxRate()*profitLoss;
        }
    }
}
