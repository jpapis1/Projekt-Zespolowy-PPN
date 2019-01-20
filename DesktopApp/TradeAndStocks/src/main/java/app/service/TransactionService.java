package app.service;

import app.CustomMessages;
import app.api.StockDataService;
import app.model.Transaction;
import app.model.User;
import app.repository.TransactionRepository;
import app.repository.UserRepository;
import app.view.table.StockInfoTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
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
    public CustomMessages makeTransaction(Transaction transaction, boolean isBuy, double total, double currentPortfolioUnits, double currentStockPrice) {
        User myUser = UserService.getActiveUser();
        double availableFunds = myUser.getFunds();
        double transactionValue = transaction.getUnitPrice() * transaction.getUnits();
        transactionValue = Math.round(transactionValue * 100.0) / 100.0;
        if (isBuy) {
            if ((total) <= availableFunds) {
                myUser.setFunds(myUser.getFunds() - (total));
                userService.updateUser(myUser);
                transactionRepository.save(transaction);
                return CustomMessages.Success;
            } else {
                return CustomMessages.NotEnoughFunds;
            }
        } else {
            Double buyValue = transactionRepository.getSumOfBuyTransactions(transaction.getShortName(),myUser);
            Double sellValue = transactionRepository.getSumOfSellTransactions(transaction.getShortName(),myUser);

            List<Transaction> listOfTransactions = transactionRepository.findByUserAndShortNameAndDoesExistsTrue(myUser,transaction.getShortName());


            if(buyValue==null){buyValue=0.0;}
            if(sellValue==null){sellValue=0.0;}
            System.out.println("Buy value: " + buyValue + " Sell value: " + sellValue + " Transaction value: " + transactionValue);
            if((currentPortfolioUnits*currentStockPrice)>=transactionValue) {
                myUser.setFunds(myUser.getFunds() + total);
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
            System.out.println("Profit Mar: " + stockInfoTable.getProfitLoss());
            result.add(stockInfoTable);
        });
        return result;
    }
}
