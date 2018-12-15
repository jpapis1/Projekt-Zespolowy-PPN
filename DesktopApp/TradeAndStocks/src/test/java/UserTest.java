import app.config.Config;
import app.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = Config.class)
//@SpringJUnitConfig(Config.class)
//@SpringBootTest
//@ExtendWith(SpringExtension.class)
@SpringBootTest
@SpringJUnitConfig(Config.class)
@ExtendWith(SpringExtension.class)
public class UserTest {
    @Autowired
    UserService userService;
}
