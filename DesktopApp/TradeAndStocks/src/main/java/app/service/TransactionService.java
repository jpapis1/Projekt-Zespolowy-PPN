package app.service;

import app.model.Transaction;
import app.repository.TransactionRepository;

public class TransactionService {
    private static TransactionRepository transactionRepository;
    private static boolean initialized = false;
    public static void initialize(TransactionRepository repo) {
        if(!initialized) {
            transactionRepository = repo;
            initialized = true;
        }
    }

    public static TransactionRepository getRepo() {
        return transactionRepository;
    }
}
