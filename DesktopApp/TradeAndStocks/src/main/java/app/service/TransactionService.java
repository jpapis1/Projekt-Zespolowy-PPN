package app.service;

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

    public List<Transaction> getUsersTransactionList(User user) {
        return transactionRepository.findByUser(user);
    }
}
