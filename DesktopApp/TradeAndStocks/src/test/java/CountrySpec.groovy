import app.Application
import app.config.Config
import app.model.Broker
import app.model.Country
import app.model.User
import app.service.BrokerService
import app.service.CountryService
import app.service.PermissionService
import app.service.UserService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional;
import spock.lang.Specification;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
class CountrySpec extends Specification {

    @Autowired
    UserService userService;

    @Autowired
    BrokerService brokerService;

    @Mock
    CountryService countryService;

    @Autowired
    PermissionService permissionService;



    @Test
    def "create country"() {
        given:
        Country country = new Country("zDresffarraaiamdy", 0.01);

        when:
        countryService.addCountry(country.name,country.taxRate);

        then:
        //We can use the == operator to compare Strings in Groovy.
        countryService.getAllCountries().find{ it.name == country.name && it.taxRate == country.taxRate}.collect().size()==1
    }
    @Test
    def "remove country"() {
        given:
        Country country = new Country("DreamLand", 0.01);

        when:
        countryService.addCountry(country.name,country.taxRate);
        countryService.removeCountry(country);

        then:
        countryService.getAllCountries().find{ it.name == country.name && it.taxRate == country.taxRate}.collect().size()==0
    }
}