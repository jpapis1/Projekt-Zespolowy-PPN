package app.view.table;
import app.api.StockData;

import java.util.Date;
import java.util.HashMap;

public class AllStocksTable {

    // ONLY USED WHEN CONSTRUCTED WITH ALL DATA
    private String sector;
    private String shortName;
    private String name;
    private Double price;
    private Date date;
    //

    public AllStocksTable (StockData data) {
        HashMap map = data.map;
        this.sector = (String) map.get("sector");
        this.shortName = (String) map.get("shortName");
        this.name = (String) map.get("name");
        this.price = (Double) map.get("price");
        this.date = (Date) map.get("date");
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
