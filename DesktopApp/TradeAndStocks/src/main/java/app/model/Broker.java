package app.model;

import javax.persistence.*;

@Entity
public class Broker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
}
