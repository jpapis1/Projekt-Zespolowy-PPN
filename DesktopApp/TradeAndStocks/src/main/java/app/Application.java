/*
 *    Copyright 2018-2019 Jacek Papis, Michał Piątek, Zofia Napierała
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

package app;

import app.config.Config;
import app.repository.*;
import app.service.*;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Application {
    @Autowired
    UserService userService;
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);

    }
    @Bean
    public CommandLineRunner run() {
        return (args) -> {
            //BrokerService brokerService = new BrokerService();

            //BrokerService.initialize(brokerRepository);
            //brokerService.dosth();
            //TestService testService = new TestService();
            //testService.getBrokerById("PKO");
           // UserService.initialize(userRepository);
//            ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);

  //          String webAddress = ctx.getBean(String.class);
   //         System.out.println(webAddress);
            //LoginWindow.launch(LoginWindow.class);
            //ApplicationRunner runMe = new ApplicationRunner();
            //System.out.println(userService.isPasswordCorrect("Test6","password"));

            System.out.println(userService.isPasswordCorrect("Test6","geag"));
          //  System.out.println(testService.getBrokerById("PKO"));
            //runMe.run(args);
            //runMe.start(new Stage());
            //MyStocksWindow.launch(MyStocksWindow.class);
            //ArrayList<MyStocksTable> table  = UserService.getUserTransactions(userRepository.findFirstByUsername("us1zazxaeer"));
            //table.stream().forEach(System.out::println);
            //new LoginWindow().start(new Stage());
            /*User user = new User.UserBuilder("us1zazxaeer")
                    .fullName("Jeragzy","Pek")
                    .pass("H@sło").mail("jerzy@efaefs.pl")
                    .funds(3000.0).perm(PermissionEnum.client)
                    .broker("Bank Of America").build();
            UserService.getRepo().save(user);
            TransactionService.getRepo().save(new Transaction("aapl", 1.94882, 255.23, new Date(), true, true,
                    user.getBroker(), user));

*/
            //log.warn(Boolean.toString(UserService.isPasswordCorrect("Test1","password")));
            //ArrayList<StockData> maps = StockDataService.getAllStocksList();
            //maps.forEach(System.out::println);
        };
    }

}
