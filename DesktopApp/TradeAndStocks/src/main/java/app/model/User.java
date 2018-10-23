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

import app.repository.UserRepository;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int idUser;

    @Column(unique=true)
    private String username;

    private String password;

    @Column(unique=true)
    private String email;

    private String name;
    private String surname;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="idWallet")
    Wallet wallet;

    @ManyToOne(targetEntity=Permission.class )
    @JoinColumn(name="idPermission")
    private Permission permission;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="idPortfolio")
    private Portfolio portfolio;

    public User(){ }

    public User(String username, String password, String email, String name, String surname, Wallet wallet, Permission permission, Portfolio portfolio) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.wallet = wallet;
        this.permission = permission;
        this.portfolio = portfolio;
    }
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
}
