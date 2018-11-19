package app.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
    static public StockData getLatestStockData(String stockName)  {
        String sURL = "https://api.iextrading.com/1.0/stock/" + stockName + "/chart/1d";

        try {
            URL url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));

            JsonArray array = root.getAsJsonArray();
                JsonObject latestElement = array.get(array.size() - 1).getAsJsonObject();
                int i = 2;
                while (latestElement.get("average").getAsDouble()==-1) {
                    latestElement = array.get(array.size() - i).getAsJsonObject();
                    i++;
                }
                    String date_s = latestElement.get("date").getAsString() + latestElement.get("minute").getAsString();
                    SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddhh:mm");
                    Date date = dt.parse(date_s);


                return new StockData(date, latestElement.get("average").getAsDouble());
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
    static public StockDataMap setNamesAndSector(String shortName) {
        String sURL ="https://api.iextrading.com/1.0/stock/" + shortName + "/company";
        try {
            URL url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            String sector = root.getAsJsonObject().get("sector").getAsString();
            String companyName = root.getAsJsonObject().get("companyName").getAsString();
            return new StockDataMap(shortName,companyName,sector);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    static public ArrayList<StockDataMap> getAllStocksList() {
        ArrayList<StockDataMap> stockDataMaps = new ArrayList<>();
        for(String shortName : usedStocks) {
            StockDataMap map = setNamesAndSector(shortName);
            StockData dateAndPrice = getLatestStockData(shortName);
            map.setDate(dateAndPrice.getDate());
            map.setPrice(dateAndPrice.getPrice());
            stockDataMaps.add(map);
        }
        return stockDataMaps;
    }
}
