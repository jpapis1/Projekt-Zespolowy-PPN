package app.view.table;

import app.model.User;
import app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;

public class StockInfoTable {
    private double unitPriceAtTheDayOfPurchase;
    private double units;
    private double valueAtTheDayOfPurchase;
    private double currentValue;
    private Date dateOfTransaction;
    private String profitLoss;

    public StockInfoTable(){ }

    public StockInfoTable(double unitPriceAtTheDayOfPurchase, double units, double valueAtTheDayOfPurchase, double currentValue, Date dateOfTransaction, String profitLoss) {
        this.unitPriceAtTheDayOfPurchase = unitPriceAtTheDayOfPurchase;
        this.units = units;
        this.valueAtTheDayOfPurchase = valueAtTheDayOfPurchase;
        this.currentValue = currentValue;
        this.dateOfTransaction = dateOfTransaction;
        this.profitLoss = profitLoss;
    }

    public double getUnitPriceAtTheDayOfPurchase() {
        return unitPriceAtTheDayOfPurchase;
    }

    public void setUnitPriceAtTheDayOfPurchase(double unitPriceAtTheDayOfPurchase) {
        this.unitPriceAtTheDayOfPurchase = unitPriceAtTheDayOfPurchase;
    }

    public double getUnits() {
        return units;
    }

    public void setUnits(double units) {
        this.units = units;
    }

    public double getValueAtTheDayOfPurchase() {
        return valueAtTheDayOfPurchase;
    }

    public void setValueAtTheDayOfPurchase(double valueAtTheDayOfPurchase) {
        this.valueAtTheDayOfPurchase = valueAtTheDayOfPurchase;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(Date dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public String getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(String profitLoss) {
        this.profitLoss = profitLoss;
    }
}


