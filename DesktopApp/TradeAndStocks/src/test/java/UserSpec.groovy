import app.Application
import app.CustomMessages
import app.config.Config
import app.model.Broker
import app.model.Country
import app.model.Permission
import app.model.PermissionEnum
import app.model.Transaction
import app.model.User
import app.service.BrokerService
import app.service.CountryService
import app.service.PermissionService
import app.service.TransactionService
import app.service.UserService
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.annotation.Transactional
import spock.lang.Shared
import spock.lang.Specification;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
class UserSpec extends Specification {

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
    void bef(){
        println "before"

        Permission permission = permissionService.getPermissionByEnum(PermissionEnum.client);
        Country country = new Country("countryABC", 0.01);
        countryService.addCountry(country);
        Broker broker = new Broker("brokerABC",0.01,0.03,country);
        brokerService.addBroker(broker);
        user = new User.UserBuilder("userABC")
                .broker(broker).fullName("Adam","Nowak").mail("nowak.adam@web.com")
                .funds(2000).pass("pass").perm(permission).build();
        userService.createUser(user)
        UserService.setActiveUser(user);
        println("User has been created!")


    }
    @Test
    void "make transaction" () {
       // when:
        User user = userService.getUser("userABC");
        Transaction transaction = new Transaction.TransactionBuilder("AAPL",user)
                .date(new Date())
                .price(192.55)
                .units(4.5123)
                .setToBuy().build();

        println transactionService.makeTransaction(transaction,true,2000);
        //transactionService.makeTransaction(transaction,true,sum+sum*user.get
      //  then:

        //println "test1"
    }
    @Test
    void t2(){
        println "test2"
    }
    @After
    void aft(){
        Country country = countryService.getCountryByName("countryABC");
        Broker broker = brokerService.getBrokerByName("brokerABC");
        User user = userService.getUser("userABC")

        userService.removeUser(user)
        brokerService.removeBroker(broker)
        countryService.removeCountry(country);
    }

}