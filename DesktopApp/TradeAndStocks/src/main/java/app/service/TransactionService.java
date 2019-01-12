package app.service;

import app.CustomMessages;
import app.model.Transaction;
import app.model.User;
import app.repository.TransactionRepository;
import app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public CustomMessages makeTransaction(Transaction transaction, boolean isBuy) {
        User myUser = UserService.getActiveUser();
        double availableFunds = myUser.getFunds();
        double transactionValue = transaction.getUnitPrice() * transaction.getUnits();
        if (isBuy) {
            if (transactionValue <= availableFunds) {
                myUser.setFunds(myUser.getFunds() - transactionValue);
                userService.updateUser(myUser);
                transactionRepository.save(transaction);
                return CustomMessages.Success;
            } else {
                return CustomMessages.NotEnoughFunds;
            }
        } else {
            Double buyValue = transactionRepository.getSumOfBuyTransactions(transaction.getShortName());
            Double sellValue = transactionRepository.getSumOfSellTransactions(transaction.getShortName());

            if(buyValue==null){buyValue=0.0;}
            if(sellValue==null){sellValue=0.0;}
            if((buyValue-sellValue)>=transactionValue) {
                myUser.setFunds(myUser.getFunds() + transactionValue);
                userService.updateUser(myUser);
                transactionRepository.save(transaction);
                return CustomMessages.Success;
            } else {
                return CustomMessages.NothingToSell;
            }

        }
    }
}
