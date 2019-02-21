package app.view.table;

import javafx.scene.image.Image;

public class MyStocksTable {
    private String shortName;
    private Double unitPrice;
    private double units;
    private double value;
    private String profitLoss;
    private Image icon;

    public MyStocksTable(String shortName, Double unitPrice, double units, double value, String profitLoss, Image icon) {
        this.shortName = shortName;
        this.unitPrice = unitPrice;
        this.units = units;
        this.value = value;
        this.profitLoss = profitLoss;
        this.icon = icon;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getUnits() {
        return units;
    }

    public void setUnits(double units) {
        this.units = units;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(String profitLoss) {
        this.profitLoss = profitLoss;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "MyStocksTable{" +
                "shortName='" + shortName + '\'' +
                ", unitPrice=" + unitPrice +
                ", units=" + units +
                ", value=" + value +
                ", profitLoss=" + profitLoss +
                '}';
    }
}

