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

package app.model;

import javax.persistence.*;

@Entity
public class Broker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBroker;

    @Column(unique = true)
    private String name;

    private double profitMargin;
    private double handlingFee;

    @ManyToOne(targetEntity = Country.class)
    @JoinColumn(name = "idCountry")
    private Country country;

    public Broker() {
    }

    public Broker(String name, double profitMargin, double handlingFee, Country country) {
        this.name = name;
        this.profitMargin = profitMargin;
        this.handlingFee = handlingFee;
        this.country = country;
    }

    public int getIdBroker() {
        return idBroker;
    }

    public void setIdBroker(int idBroker) {
        this.idBroker = idBroker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(double profitMargin) {
        this.profitMargin = profitMargin;
    }

    public double getHandlingFee() {
        return handlingFee;
    }

    public void setHandlingFee(double handlingFee) {
        this.handlingFee = handlingFee;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return name;
    }
}
