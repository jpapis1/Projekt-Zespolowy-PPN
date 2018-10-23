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

import app.model.*;
import app.repository.PermissionRepository;
import app.repository.PortfolioRepository;
import app.repository.TransactionRepository;
import app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public CommandLineRunner run(UserRepository userRepository, PermissionRepository permissionRepository,
                                 PortfolioRepository portfolioRepository, TransactionRepository transactionRepository) {
        return (args) -> {
/*
            ArrayList<Transaction> transactionList = new ArrayList<>();
            Transaction t1 = new Transaction("appl",1.442,348.38,new Date(),true);
            transactionList.add(t1);
            Transaction t2 = new Transaction("msft",20.481,141.01,new Date(),true);
            transactionList.add(t2);
            transactionRepository.save(t1);
            transactionRepository.save(t2);
            Portfolio portfolio2 = new Portfolio(transactionList);

            portfolioRepository.save(portfolio2);

            permissionRepository.save(new Permission("admin"));
            permissionRepository.save(new Permission("client"));
            userRepository.save(new User("test@test.com",
                    "abc",
                    "gage@ga.pl",
                    "Jerzy",
                    "Hop",
                    new Wallet(0),
                    permissionRepository.findFirstByName("admin"),new Portfolio()));

            userRepository.save(new User("stefan",
                    "passWoRd",
                    "abc@side.pl",
                    "Stefan",
                    "Apel",
                    new Wallet(100.44),
                    permissionRepository.findFirstByName("client"),new Portfolio()));
            userRepository.save(new User("robert@pp.pl",
                    "iusc81Kby",
                    "bcs@internet.com",
                    "Marcin",
                    "Unrukowski",
                    new Wallet(19902.32),
                    permissionRepository.findFirstByName("client"),portfolio2));
            log.warn(Integer.toString(userRepository.findByName("Jerzy").size()));
            userRepository.findAll().forEach(x -> log.warn(x.getName() + " " + x.getSurname() + " " + x.getPermission().getName()));

            User user = userRepository.findFirstByUsername("stefan");
            */
            //user.getPortfolio().addTransaction(new Transaction("appl",1.442,348.38,new Date(),true));
            //user.getPortfolio().addTransaction(new Transaction("msft",20.481,141.01,new Date(),true));
            //userRepository.save(user);

        };
    }



}
