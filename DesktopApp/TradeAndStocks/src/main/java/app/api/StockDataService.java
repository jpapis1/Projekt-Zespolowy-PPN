package app.api;

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
    static public ArrayList<StockData> getDayStockPrices(String stockName)  {
        String sURL = "https://api.iextrading.com/1.0/stock/" + stockName + "/chart/1d";

        try {
            URL url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonArray array = root.getAsJsonArray();
            ArrayList<StockData> stockDataList = new ArrayList<>();
            for(JsonElement element : array) {
                String date_s = element.getAsJsonObject().get("date").getAsString() + element.getAsJsonObject().get("minute").getAsString();
                SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddhh:mm");
                Date date = dt.parse(date_s);
                stockDataList.add(new StockData(date,element.getAsJsonObject().get("average").getAsDouble()));

            }
            return stockDataList;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    static public ArrayList<StockData> getMonthStockPrices(String stockName)  {
        String sURL = "https://api.iextrading.com/1.0/stock/" + stockName + "/chart/1m";
        try {
            URL url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonArray array = root.getAsJsonArray();
            ArrayList<StockData> stockDataList = new ArrayList<>();
            for(JsonElement element : array) {
                String date_s = element.getAsJsonObject().get("date").getAsString() + "15:59";
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-ddhh:mm");
                Date date = dt.parse(date_s);
                stockDataList.add(new StockData(date,element.getAsJsonObject().get("close").getAsDouble()));

            }
            return stockDataList;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
