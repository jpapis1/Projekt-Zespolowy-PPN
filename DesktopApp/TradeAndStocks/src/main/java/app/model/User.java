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
import app.repository.BrokerRepository;
import app.service.BrokerService;
import app.service.PermissionService;

import javax.persistence.*;
import java.util.Date;

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

    private String firstName;
    private String lastName;

    @ManyToOne(targetEntity=Permission.class )
    @JoinColumn(name="idPermission")
    private Permission permission;


    private double funds;

    @ManyToOne(targetEntity=app.model.Broker.class)
    @JoinColumn(name="idBroker")
    private Broker broker;


    // dla Pythona
    private Date date_joined;
    private String first_name;
    private String last_name;
    private int is_active;
    private int is_staff;
    private int is_superuser;
    private int is_anonymous;
    private int is_authenticated;
    //
    public User(){ }

    public User(String username, String password, String email, String firstName, String lastName, Permission permission, double funds, Broker broker) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.permission = permission;
        this.funds = funds;
        this.broker = broker;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public double getFunds() {
        return funds;
    }

    public void setFunds(double funds) {
        this.funds = funds;
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    // Dla Pythona
    public Date getDate_joined() {
        return date_joined;
    }

    public void setDate_joined(Date date_joined) {
        this.date_joined = date_joined;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getIs_staff() {
        return is_staff;
    }

    public void setIs_staff(int is_staff) {
        this.is_staff = is_staff;
    }

    public int getIs_superuser() {
        return is_superuser;
    }

    public void setIs_superuser(int is_superuser) {
        this.is_superuser = is_superuser;
    }

    public int getIs_anonymous() {
        return is_anonymous;
    }

    public void setIs_anonymous(int is_anonymous) {
        this.is_anonymous = is_anonymous;
    }

    public int getIs_authenticated() {
        return is_authenticated;
    }

    public void setIs_authenticated(int is_authenticated) {
        this.is_authenticated = is_authenticated;
    }
    //
    public static class UserBuilder {
        private String username;
        private String password;
        private String email;
        private String firstName;
        private String lastName;
        private Permission permission;
        private double funds;
        private Broker broker;

        public UserBuilder(String username) {
            this.username = username;
        }
        public UserBuilder pass(String password) {
            this.password = password;
            return this;
        }
        public UserBuilder mail(String email) {
            this.email = email;
            return this;
        }
        public UserBuilder funds(double funds) {
            this.funds = funds;
            return this;
        }
        public UserBuilder perm(PermissionEnum permissionEnum) {
            this.permission = PermissionService.getRepo().findFirstByName(permissionEnum);
            return this;
        }
        public UserBuilder broker(String broker) {
            this.broker = BrokerService.getRepo().findFirstByName(broker);
            return this;
        }
        public UserBuilder fullName(String firstName,String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
            return this;
        }
        public User build() {
            User user = new User(username, password, email, firstName, lastName,
                    permission, funds,broker);
            user.setFirst_name("");
            user.setLast_name("");
            user.setDate_joined(new Date());
            user.setIs_anonymous(0);
            user.setIs_active(0);
            user.setIs_authenticated(0);
            user.setIs_staff(0);
            user.setIs_superuser(0);
            return user;
        }

    }
}
