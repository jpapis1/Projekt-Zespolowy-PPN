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
import app.repository.PermissionRepository;
import app.repository.TransactionRepository;
import app.repository.UserRepository;
import app.service.PermissionService;
import app.service.TransactionService;
import app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;


@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);



    }

    @Bean
    public CommandLineRunner run(UserRepository userRepository, PermissionRepository permissionRepository,
                                  TransactionRepository transactionRepository) {
        return (args) -> {
            PermissionService.initialize(permissionRepository);
            TransactionService.initialize(transactionRepository);
            UserService.initialize(userRepository);
            User user = new User.UserBuilder("user")
                    .fullName("Stefan","Waszczyk")
                    .pass("H@sło").mail("stefan@gg.pl")
                    .funds(1000.0).perm(PermissionEnum.client)
                    .taxes(0.1,0,0).build();
            UserService.getRepo().save(user);
            TransactionService.getRepo().save(new Transaction("aapl", 1.94882, 255.23, new Date(), true, true,
                    user.getTaxRate(),user.getBrokersProfitMargin(), user.getHandlingFee(), user));

        };
    }



}
