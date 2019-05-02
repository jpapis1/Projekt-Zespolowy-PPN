import app.Application
import app.config.Config
import app.model.Broker
import app.model.Country
import app.model.User
import app.service.BrokerService
import app.service.CountryService
import app.service.UserService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional;
import spock.lang.Specification;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional()
class BrokerSpec extends Specification {

    @Autowired
    UserService userService;

    @Autowired
    BrokerService brokerService;

    @Autowired
    CountryService countryService;


    @Test
    def "create broker"() {
        given:
        Country country = new Country("DreamLand", 0.01);
        Broker broker = new Broker("ABC",0.02,0.01,country);

        when:
        countryService.addCountry(country);
        brokerService.addBroker(broker);
        Broker found = brokerService.getBrokerByName(broker.name);


        then:
        (found.name==broker.name && found.profitMargin==broker.profitMargin && found.handlingFee == broker.handlingFee);
    }
}