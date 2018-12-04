package app.api;

import app.view.tables.AllStocksTable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StockDataService {
    static public final String[] usedStocks= {"AAPL","HPQ","INTC","MSFT","FB","AMD","AMZN","GOOGL","FDX","HAS",
            "MCD","MET","NFLX","NKE","HD","PYPL","QCOM","SBUX","TGT","TXN","TWTR","FOX","VZ","XRX"};

    static public ArrayList<StockData> getAllStockDataList() {
        ArrayList<StockData> stockData = new ArrayList<>();
        for(String shortName : usedStocks) {
            StockData map = new StockData.StockDataBuilder(shortName)
                    .setNameAndSector().setLatestPriceAndDate().build();
            stockData.add(map);
        }
        return stockData;
    }
    static public ArrayList<AllStocksTable> getAllStocksTableList() {
        ArrayList<AllStocksTable> tables = new ArrayList<>();
        for(String shortName : usedStocks) {
            StockData map = new StockData.StockDataBuilder(shortName)
                    .setNameAndSector().setLatestPriceAndDate().build();
            tables.add(new AllStocksTable(map));
        }
        return tables;
    }
}
