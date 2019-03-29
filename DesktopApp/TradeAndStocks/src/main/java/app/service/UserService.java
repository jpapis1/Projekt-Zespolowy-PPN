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

import app.api.StockData;
import app.api.StockDataService;
import app.model.Transaction;
import app.model.User;
import app.repository.UserRepository;
import app.view.table.AllStocksTable;
import app.view.table.MyStocksTable;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import javafx.scene.image.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.transaction.Transactional;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private static User activeUser;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionService transactionService;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public static void setActiveUser(User user) {
        activeUser = user;
    }
    public  void createUser(User user) throws DataAccessException{
            userRepository.save(user);
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
        String[] pass = new String[3];
        pass[1] = "120000";
        byte[] array = new byte[12]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));

        pass[2] = generatedString;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), pass[2].getBytes(), Integer.valueOf(pass[1]), 256);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            String passHash = Base64.encode(f.generateSecret(spec).getEncoded());
            return "pbkdf2_sha256$" + pass[1] + "$" + pass[2] + "$" + passHash;
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
            double profitLoss = -(1 - (valueSell + (currentUnitPrice*unitSum))/valueBuy)*100;
            String str = String.format("%.2f", profitLoss);
            profitLoss = Double.parseDouble(str);
            if(unitSum>=0.01) {
                String realValueRound = String.format("%.2f", realValue);
                //icon
                    Image icon = new StockData.StockDataBuilder(shortName).getLogo();



                myStocksTable.add(new MyStocksTable(shortName, currentUnitPrice, unitSum,Double.parseDouble(realValueRound), profitLoss+"%",icon));
            }
        }
        return myStocksTable;
    }

    public void removeUser(User selectedItem) throws DataAccessException {
            userRepository.delete(selectedItem);
    }
}
