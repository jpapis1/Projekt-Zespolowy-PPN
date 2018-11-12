package app.model;

import javax.persistence.*;

@Entity
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idCountry;

    @Column(unique=true)
    private String name;

    private double taxRate;

    public Country() { }
    public Country(String name, double taxRate) {
        this.name = name;
        this.taxRate = taxRate;
    }

    public int getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(int idCountry) {
        this.idCountry = idCountry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }
}
