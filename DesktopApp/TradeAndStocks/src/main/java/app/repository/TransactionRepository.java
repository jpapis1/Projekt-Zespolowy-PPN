/*
 *    Copyright 2018-2019 Jacek Papis, Michał Piątek
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

package app.repository;

import app.model.Transaction;
import app.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByUserAndDoesExistsTrue(User user);

    List<Transaction> findByUserAndShortName(User user, String shortName);
    List<Transaction> findByUserAndShortNameAndDoesExistsTrue(User user, String shortName);

    public List<Transaction> findAll();

    @Query(value = "SELECT SUM(t.unitPrice*t.units) from Transaction t WHERE t.shortName=:shortName AND t.doesExists=true AND t.isBuy=true AND t.user=:myUser")
    Double getSumOfBuyTransactions(@Param("shortName") String shortName, @Param("myUser") User user); //t.user.idUser=:myUser

    @Query(value = "SELECT SUM(t.unitPrice*t.units) from Transaction t WHERE t.shortName=:shortName AND t.doesExists=true AND t.isBuy=false AND t.user=:myUser")
    Double getSumOfSellTransactions(@Param("shortName") String shortName, @Param("myUser") User user);

    @Query(value = "SELECT SUM(:currentPrice*t.units) from Transaction t WHERE t.shortName=:shortName AND t.doesExists=true AND t.isBuy=true AND t.user=:myUser")
    Double getSumOfBuyTransactionsWithCurrentPrice(@Param("shortName") String shortName, @Param("myUser") User user, @Param("currentPrice") double currentPrice); //t.user.idUser=:myUser

    @Query(value = "SELECT SUM(:currentPrice*t.units) from Transaction t WHERE t.shortName=:shortName AND t.doesExists=true AND t.isBuy=false AND t.user=:myUser")
    Double getSumOfSellTransactionsWithCurrentPrice(@Param("shortName") String shortName, @Param("myUser") User user, @Param("currentPrice") double currentPrice);
}
