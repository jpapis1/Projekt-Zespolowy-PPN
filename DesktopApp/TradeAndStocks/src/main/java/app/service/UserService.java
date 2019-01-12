package app.service;

import app.model.Transaction;
import app.model.User;
import app.repository.UserRepository;
import app.view.table.MyStocksTable;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static User activeUser;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionService transactionService;

    public static void setActiveUser(User user) {
        activeUser = user;
    }
    public static User getActiveUser() {
        return activeUser;
    }
    public User getUser(String nameOrEmail) {
        return userRepository.findFirstByUsernameOrEmail(nameOrEmail,nameOrEmail);
    }
    public void updateUser(User user) {
        userRepository.save(user);
    }
    public boolean isPasswordCorrect(String usernameOrEmail, String password) {
        User user = userRepository.findFirstByUsername(usernameOrEmail);
        if (user == null) { // username not found
            user = userRepository.findFirstByEmail(usernameOrEmail);
        }
        if (user == null) return false; // neither username nor email was found
        System.out.println(user);
        String[] pass = user.getPassword().split("\\$");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), pass[2].getBytes(), Integer.valueOf(pass[1]), 256);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            String passHash = Base64.encode(f.generateSecret(spec).getEncoded());
            if (pass[3].equals(passHash)) {
                setActiveUser(user);
                return true;
            } else {
                return false;
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("ERROR");
        }
        return false;
    }

    public ArrayList<MyStocksTable> getUserTransactions(User user) {
        List<Transaction> list = transactionService.getUsersTransactionList(user);
        ArrayList<MyStocksTable> myStocksTable = new ArrayList<>();
        Set<String> names = new HashSet<String>();
        for (Transaction t : list) {
            names.add(t.getShortName());
        }
        for (String shortName : names) {
            List<Transaction> transactionsByShortName = list.stream().filter(x -> x.getShortName().equals(shortName)).collect(Collectors.toList());
            double unitSum = transactionsByShortName.stream().mapToDouble(Transaction::getUnits).sum();

            double unitPrice = transactionsByShortName.get(0).getUnitPrice(); // FIX!! tu powinna być aktualna cena
            double value = transactionsByShortName.stream().mapToDouble(x -> x.getUnitPrice() * x.getUnits()).sum();
            double realValue = unitSum * unitPrice;
            double profitLoss = ((1 - (realValue / value)));
            String str = String.format("%.2f", profitLoss);
            profitLoss = Double.valueOf(str);
            for (Transaction transaction : transactionsByShortName) {
                System.out.println(transaction);
            }
            myStocksTable.add(new MyStocksTable(shortName, unitPrice, unitSum, realValue, profitLoss));
        }
        return myStocksTable;
    }
}
