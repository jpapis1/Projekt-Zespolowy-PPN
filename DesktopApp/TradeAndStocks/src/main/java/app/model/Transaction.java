/*
 *  Copyright 2018-2019 Jacek Papis, Michał Piątek, Zofia Napierała
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Data provided for free by IEX https://iextrading.com/developer/.
 *  View IEX’s Terms of Use https://iextrading.com/api-exhibit-a/.
 *
 */

package app.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idTransaction;
    private String shortName;
    private double units;
    private double unitPrice;
    private Date date;
    private boolean isBuy;
    private boolean doesExists;

    @ManyToOne(targetEntity=Broker.class )
    @JoinColumn(name="idBroker")
    private Broker broker;

    @ManyToOne(targetEntity=User.class )
    @JoinColumn(name="idUser")
    private User user;

    public Transaction() {
    }

    public Transaction(String shortName, double units, double unitPrice, Date date, boolean isBuy, boolean doesExists, Broker broker, User user) {
        this.shortName = shortName;
        this.units = units;
        this.unitPrice = unitPrice;
        this.date = date;
        this.isBuy = isBuy;
        this.doesExists = doesExists;
        this.broker = broker;
        this.user = user;
    }

    public int getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(int idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public double getUnits() {
        return units;
    }

    public void setUnits(double units) {
        this.units = units;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public boolean isDoesExists() {
        return doesExists;
    }

    public void setDoesExists(boolean doesExists) {
        this.doesExists = doesExists;
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "idTransaction=" + idTransaction +
                ", shortName='" + shortName + '\'' +
                ", units=" + units +
                ", unitPrice=" + unitPrice +
                ", date=" + date +
                ", isBuy=" + isBuy +
                ", doesExists=" + doesExists +
                ", broker=" + broker +
                ", user=" + user +
                '}';
    }

    public static class TransactionBuilder {
        private String shortName;
        private double units;
        private double unitPrice;
        private Date date;
        private boolean isBuy;
        private boolean doesExists;
        private Broker broker;
        private User user;

        public TransactionBuilder(String shortName, User user) {
            this.shortName = shortName;
            this.user = user;
            this.broker = user.getBroker();
            doesExists = true;
        }

        public TransactionBuilder setToBuy() {
            isBuy = true;
            return this;
        }
        public TransactionBuilder setToSell() {
            isBuy = false;
            return this;
        }
        public TransactionBuilder units(double units) {
            this.units = units;
            return this;
        }
        public TransactionBuilder price(double unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }
        public TransactionBuilder date(Date date) {
            this.date = date;
            return this;
        }
        public Transaction build() {
            return new Transaction(shortName, units, unitPrice, date, isBuy, doesExists, broker, user);
        }

    }
}
