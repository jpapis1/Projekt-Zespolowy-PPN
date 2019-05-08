import app.Application;
import app.CustomMessages;
import app.config.Config;
import app.model.Broker;
import app.model.Country;
import app.model.Permission;
import app.model.PermissionEnum;
import app.model.Transaction;
import app.model.User;
import app.service.BrokerService;
import app.service.CountryService;
import app.service.PermissionService;
import app.service.TransactionService;
import app.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserTest {

    @Autowired
    UserService userService;

    @Autowired
    BrokerService brokerService;

    @Autowired
    CountryService countryService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    TransactionService transactionService;

    /*
    @Before
    public void s() {
        println "setup..."
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
    }
    @After
    public void tearDown() {
        println "teardown is happening"
        transactionManager.rollback(transactionStatus);
    }
    */
    static User user;

    @Before
    public void before(){
        Permission permission = permissionService.getPermissionByEnum(PermissionEnum.client);
        Country country = new Country("countryABC", 0.01);
        countryService.addCountry(country);
        Broker broker = new Broker("brokerABC",0.01,0.03,country);
        brokerService.addBroker(broker);
        user = new User.UserBuilder("userABC")
                .broker(broker).fullName("Adam","Nowak").mail("nowak.adam@web.com")
                .funds(2000).pass("pass").perm(permission).build();
        userService.createUser(user);
        UserService.setActiveUser(user);
        System.out.println("User has been created!");

    }

    boolean transaction(String stockName,boolean isBuy,double unitPrice, double units, CustomMessages expectedMessage) {

        User user = userService.getUser("userABC");
        System.out.println("-------------------------------------------------------");
        System.out.println("Funds before transaction: " + String.format("%.2f", user.getFunds()));
        Transaction.TransactionBuilder transactionB = new Transaction.TransactionBuilder(stockName,user)
                .date(new Date())
                .price(unitPrice)
                .units(units);
        if(isBuy) transactionB.setToBuy();
        else transactionB.setToSell();
        Transaction transaction = transactionB.build();

        double simpleTotal = transaction.getUnits() * transaction.getUnitPrice();
        double profitValue = user.getBroker().getProfitMargin()*simpleTotal;


        double profitLoss = transactionService.calculateProfitLoss(stockName,UserService.getActiveUser());



        double total = transactionService.calculateTotalValue(isBuy,
                simpleTotal,
                user.getBroker().getHandlingFee() * simpleTotal,
                profitLoss,user);


        System.out.println("Before fees " + String.format("%.2f", simpleTotal));
        System.out.println("After fees " + String.format("%.2f", total));
        CustomMessages message =  transactionService.makeTransaction(transaction,isBuy,total);
        double finalTransactionValue = total;
        /*
        if(isBuy) {
            finalTransactionValue =
                    Math.round((simpleTotal * (1 + user.getBroker().getHandlingFee())) * 100.0) / 100.0;
        } else {

        }
*/
        System.out.println("Transaction status: " + message);
        System.out.println("Funds after transaction: " +String.format("%.2f", userService.getUser("userABC").getFunds()));
        System.out.println("-------------------------------------------------------");

        if(message == expectedMessage) {
            if (message == CustomMessages.Success) {
                if(isBuy)
                    return Math.round(userService.getUser("userABC").getFunds() * 100) / 100 ==
                        Math.round((user.getFunds() - finalTransactionValue) * 100) / 100;
                else
                    return Math.round(userService.getUser("userABC").getFunds() * 100) / 100 ==
                            Math.round((user.getFunds() + finalTransactionValue) * 100) / 100;
            } else return true;
        } else {
            return false;
        }
    }
    @Test
    public void makeBuyTransaction() {
        assertTrue(transaction("AAPL",true,192.55,4.5123,CustomMessages.Success));
        assertTrue(transaction("AAPL",true,2092.55,2.9271,CustomMessages.NotEnoughFunds));
        assertTrue(transaction("AAPL",true,192.55,4.5123,CustomMessages.Success));
        assertTrue(transaction("AAPL",true,192.55,4.5123,CustomMessages.NotEnoughFunds));
        assertTrue(transaction("AAPL",true,20.13,1.4497,CustomMessages.Success));
        assertTrue(transaction("AAPL",true,192.55,4.5123,CustomMessages.NotEnoughFunds));
    }
    @Test
    public void makeSellTransaction() {
        // making portfolio
        System.out.println("MAKING PORTFOLIO");
        assertTrue(
        transaction("MSFT",true,15.84,2.1,CustomMessages.Success) &&
        transaction("MSFT",true,52.52,3.4,CustomMessages.Success) &&
        transaction("MSFT",true,92.55,5.1,CustomMessages.Success) &&
        transaction("FB",true,19.42,17.1,CustomMessages.Success)
        );
        // 15.84+52.52+92.55 = 160.91 MSFT value | 10.6 units
        // 19.42 FB value | 17.1 units
        // $953.60 available
        System.out.println("SELLING STOCKS");
        // Not enough MSFT units
        assertTrue(transaction("MSFT", false, 1.33,10.72,CustomMessages.NothingToSell));
        // Selling all MSFT stocks
        assertTrue(transaction("MSFT", false, 1.33,5.3,CustomMessages.Success));
        assertTrue(transaction("MSFT", false, 2.33,5.2,CustomMessages.Success));
        assertTrue(transaction("MSFT", false, 12.47,0.1,CustomMessages.Success));

        // Not enough MSFT units
        assertTrue(transaction("MSFT", false, 12.47,0.001,CustomMessages.NothingToSell));

        // big profit
        assertTrue(transaction("FB", false, 150.47,12.1,CustomMessages.Success));



    }
    @After
    public void aft(){
        Country country = countryService.getCountryByName("countryABC");
        Broker broker = brokerService.getBrokerByName("brokerABC");
        User user = userService.getUser("userABC");

        userService.removeUser(user);
        brokerService.removeBroker(broker);
        countryService.removeCountry(country);

        System.out.println("Cleanup");

    }

}