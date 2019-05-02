import app.Application
import app.config.Config
import app.service.UserService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner;
import spock.lang.Specification;

@RunWith(SpringRunner.class)
@SpringBootTest
class USpec extends Specification {

    @Autowired
    UserService userService

    def "one"() {
        expect:
        userService == null
    }
}