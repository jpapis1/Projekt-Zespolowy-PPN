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
        nettoTransactionValue = Math.round(nettoTransactionValue * 100.0) / 100.0;
        System.out.println(nettoTransactionValue + "T Value");
        System.out.println(totalTransactionValue + " Total");
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
            if((currentPortfolioUnits*transaction.getUnitPrice())>=nettoTransactionValue) {
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
            if(transaction.isBuy()) {
                stockInfoTable.setUnits(transaction.getUnits());
                String str = String.format("%.2f",transaction.getUnits()*transaction.getUnitPrice());
                stockInfoTable.setValueAtTheDayOfPurchase(Double.parseDouble(str));
                str = String.format("%.2f",transaction.getUnits()*currentUnitPrice);
                stockInfoTable.setCurrentValue(Double.parseDouble(str));
            } else {
                stockInfoTable.setUnits(-transaction.getUnits());
                stockInfoTable.setValueAtTheDayOfPurchase(-(transaction.getUnits()*transaction.getUnitPrice()));
                stockInfoTable.setCurrentValue(-(transaction.getUnits()*currentUnitPrice));
            }
            stockInfoTable.setProfitLoss(String.format("%.2f",(1-(stockInfoTable.getValueAtTheDayOfPurchase()/stockInfoTable.getCurrentValue()))*100) + "%");
            result.add(stockInfoTable);
        });
        return result;
    }

    public double calculateHandlingFee(double transactionValue, double fee) {
        return transactionValue * fee;
    }

    public double calculateProfitLoss(String stockName,User user) {

        List<Transaction> list = getUsersActiveTransactionListOfOneStock(user,stockName);
        double unitSum = calculateCurrentlyOwnedStockUnits(list);


        double currentUnitPrice = StockDataService.getLatestPrice(stockName).getPrice();


        double valueBuy = list.stream().filter(Transaction::isBuy).mapToDouble(x -> x.getUnitPrice() * x.getUnits()).sum();
        double valueSell = list.stream().filter(transaction -> !transaction.isBuy()).mapToDouble(x -> x.getUnitPrice() * x.getUnits()).sum();

        return  -(1 - (valueSell + (currentUnitPrice*unitSum))/valueBuy);

    }

    public double calculateCurrentlyOwnedStockUnits(List<Transaction> oneStockUserTransactions) {


        double unitSumBuy = oneStockUserTransactions.stream().filter(Transaction::isBuy).mapToDouble(Transaction::getUnits).sum();
        // all sell transactions
        double unitSumSell = oneStockUserTransactions.stream().filter(transaction -> !transaction.isBuy()).mapToDouble(Transaction::getUnits).sum();
        return unitSumBuy - unitSumSell;

    }
}
