package app.service;

import app.api.StockDataService;
import app.model.Transaction;
import app.model.User;
import app.repository.UserRepository;
import app.view.table.MyStocksTable;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;
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
    public String hashPassword(String password) {
        try {
            byte[] salt = new byte[11];
            Random srandom = new Random();
            srandom.nextBytes(salt);
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 120000, 256);
            SecretKey tmp = factory.generateSecret(spec);
            byte[] passwordHash = tmp.getEncoded();
            String s = Base64.encode(passwordHash);
            String sSalt = Base64.encode(salt);
            return "pbkdf2_sha256$" + "120000$" + sSalt + "$" + s;
            //pbkdf2_sha256$120000$LxkByhf1k62j$LBDyAtY+ehMlpJLfVTlQElbZ6bGeK+BVJffcYpubGRg=
            //SecretKeySpec skey = new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("ERROR");
        }
        return null;
    }

    public ArrayList<MyStocksTable> getUserActiveTransactions(User user) {
        List<Transaction> list = transactionService.getUsersActiveTransactionList(user);
        ArrayList<MyStocksTable> myStocksTable = new ArrayList<>();
        Set<String> names = new HashSet<String>();
        for (Transaction t : list) {
            names.add(t.getShortName());
        }
        for (String shortName : names) {
            List<Transaction> transactionsByShortName = list.stream().filter(x -> x.getShortName().equals(shortName)).collect(Collectors.toList());
            double unitSumBuy = transactionsByShortName.stream().filter(Transaction::isBuy).mapToDouble(Transaction::getUnits).sum();
            double unitSumSell = transactionsByShortName.stream().filter(transaction -> !transaction.isBuy()).mapToDouble(Transaction::getUnits).sum();
            double unitSum = unitSumBuy - unitSumSell;
            double currentUnitPrice = StockDataService.getLatestPrice(shortName).getPrice();
            double valueBuy = transactionsByShortName.stream().filter(Transaction::isBuy).mapToDouble(x -> x.getUnitPrice() * x.getUnits()).sum();
            double valueSell = transactionsByShortName.stream().filter(transaction -> !transaction.isBuy()).mapToDouble(x -> x.getUnitPrice() * x.getUnits()).sum();
            double value = valueBuy - valueSell;
            double realValue = unitSum * currentUnitPrice;
            System.out.println(realValue);

            double profitLoss = -(1 - (valueSell + (currentUnitPrice*unitSum))/valueBuy);
            System.out.println(1 + " - " + unitSumSell + "+" + currentUnitPrice + "*" + unitSum + "/" + unitSumBuy);
            String str = String.format("%.2f", profitLoss);
            System.out.print("Profit loss: " + profitLoss);
            profitLoss = Double.valueOf(str);
            System.out.println(" | Double profit lost: " + profitLoss + " | realVal: " + realValue + " | Value: " + value);
            if(unitSum!=0) {
                String realValueRound = String.format("%.2f", realValue);
                myStocksTable.add(new MyStocksTable(shortName, currentUnitPrice, unitSum,Double.parseDouble(realValueRound), profitLoss*100+"%"));
            }
        }
        return myStocksTable;
    }

}
