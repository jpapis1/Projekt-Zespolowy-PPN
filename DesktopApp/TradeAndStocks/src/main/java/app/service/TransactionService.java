package app.service;

import app.model.Transaction;
import app.model.User;
import app.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getUsersTransactionList(User user) {
        return transactionRepository.findByUser(user);
    }
    public void makeTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
