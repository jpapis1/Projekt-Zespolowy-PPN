package app.api;

import app.service.UserService;
import app.view.table.AllStocksTable;
import app.view.table.MyStocksTable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class StockDataService {

    static public final String[] usedStocks = {"AAPL", "HPQ", "GOOGL","MSFT", "FB", "AMD", "AMZN", "GOOGL", "FDX", "HAS"};
    //            "MCD", "MET", "NFLX", "NKE", "HD", "PYPL", "QCOM", "SBUX", "TGT", "TXN", "TWTR", "FOX", "VZ", "XRX"

    static public ArrayList<StockData> getAllStockDataList() {
        ArrayList<StockData> stockData = new ArrayList<>();
        for (String shortName : usedStocks) {
            StockData map = new StockData.StockDataBuilder(shortName)
                    .setNameAndSector().setLatestPriceAndDate().build();
            stockData.add(map);
        }
        return stockData;
    }

    static public ArrayList<AllStocksTable> getAllStocksTableList() {
        ArrayList<AllStocksTable> tables = new ArrayList<>();
        int i = 0;
        for (String shortName : usedStocks) {
            StockData map = new StockData.StockDataBuilder(shortName)
                    .setNameAndSector().setLatestPriceAndDate().build();
            tables.add(new AllStocksTable(map));

            i++;
        }
        return tables;
    }
    static public StockData getLatestPrice(String shortName) {
            return new StockData.StockDataBuilder(shortName).setLatestPrice().build();
    }

    public static ArrayList<MyStocksTable> getMyStocksTableList(UserService userService) {
        ArrayList<MyStocksTable> tables = new ArrayList<>();
        tables = userService.getUserActiveTransactions(UserService.getActiveUser());

        return tables;

    }
    public static ArrayList<MyStocksTable> getMyStocksTableListOnOneStock(UserService userService, String shortName) {
        ArrayList<MyStocksTable> tables = new ArrayList<>();
        tables = userService.getUserActiveTransactions(UserService.getActiveUser());

        return tables;

    }
}
