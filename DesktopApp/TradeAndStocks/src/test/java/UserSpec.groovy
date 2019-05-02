import app.Application
import app.config.Config
import app.model.Broker
import app.model.Country
import app.model.Permission
import app.model.PermissionEnum
import app.model.User
import app.service.BrokerService
import app.service.CountryService
import app.service.PermissionService
import app.service.UserService
import org.junit.After
import org.junit.Before
import org.junit.Test
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
@Transactional
class UserSpec extends Specification {

    @Autowired
    UserService userService;

    @Autowired
    BrokerService brokerService;

    @Autowired
    CountryService countryService;

    @Autowired
    PermissionService permissionService;

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
    void 'create test data'() {
        Permission permission = permissionService.getPermissionByEnum(PermissionEnum.client);
        Country country = new Country("abcdefg", 0.01);
        countryService.addCountry(country);
        Broker broker = new Broker("xabcdefg",0.01,0.03,country);
        brokerService.addBroker(broker);
        User u = new User.UserBuilder("xabcdefxg")
                .broker(broker).fullName("Azzzdam","Bazzsic").mail("adaxabcdefxg.Bzvuaoavsic@web.com")
                .funds(2000).pass("passzwzzord").perm(permission).build();
        user = u;
    }
    @After
    void 'remove test data'() {
        //cascade delete - removes country -> brokers -> users
        countryService.removeCountry(user.getBroker().country);
    }

    @Test
    def "check "() {
        given:

        when:
        userService.createUser(user)
        then:
        userService.getUser("xabcdefxg")!=null





    }

}