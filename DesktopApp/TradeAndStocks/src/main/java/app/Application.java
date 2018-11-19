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

import app.model.Permission;
import app.model.PermissionEnum;
import app.model.Transaction;
import app.model.User;
import app.repository.*;
import app.service.*;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.util.Date;


@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);



    }

    @Bean
    public CommandLineRunner run(UserRepository userRepository, PermissionRepository permissionRepository,
                                 TransactionRepository transactionRepository, CountryRepository countryRepository,
                                 BrokerRepository brokerRepository) {
        return (args) -> {
            PermissionService.initialize(permissionRepository);
            CountryService.initialize(countryRepository);
            BrokerService.initialize(brokerRepository);
            TransactionService.initialize(transactionRepository);
            UserService.initialize(userRepository);

            /*User user = new User.UserBuilder("us1zazxaeer")
                    .fullName("Jeragzy","Pek")
                    .pass("H@sło").mail("jerzy@efaefs.pl")
                    .funds(3000.0).perm(PermissionEnum.client)
                    .broker("Bank Of America").build();
            UserService.getRepo().save(user);
            TransactionService.getRepo().save(new Transaction("aapl", 1.94882, 255.23, new Date(), true, true,
                    user.getBroker(), user));

*/
            log.warn(Boolean.toString(UserService.isPasswordCorrect("Test1","password")));
        };
    }



}
