package app.api;

import java.util.Date;
import java.util.HashMap;

public class StockDataMap {
    private HashMap map;
    public StockDataMap(String shortName, String name, String sector, double price, Date date) {
        map = new HashMap();
        setSector(sector);
        setShortName(shortName);
        setName(name);
        setPrice(price);
        setDate(date);
    }
    public StockDataMap(String shortName, String name, String sector) {
        map = new HashMap();
        setSector(sector);
        setShortName(shortName);
        setName(name);
    }

    @Override
    public String toString() {
        return getShortName() + " | " + getName() + " | " + getPrice() + " | " + getDate() + " | " + getSector();
    }

    public String getShortName() {
        return (String) map.get("shortName");
    }
    public String getName() {
        return (String) map.get("name");
    }
    public double getPrice() {
        return (Double) map.get("price");
    }
    public Date getDate() {
        return (Date) map.get("date");
    }
    public String getSector() {
        return (String) map.get("sector");
    }
    private void setSector(String sector) {
        map.put("sector",sector);
    }
    public void setShortName(String shortName) {
        map.put("shortName", shortName);
    }
    public void setName(String name) {
        map.put("name", name);
    }
    public void setPrice(double price) {
        map.put("price", price);
    }
    public void setDate(Date date) {
        map.put("date", date);
    }
}
