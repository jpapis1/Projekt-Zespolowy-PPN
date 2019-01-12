package app.api;

import app.view.table.AllStocksTable;

import java.util.ArrayList;

public class StockDataService {
    static public final String[] usedStocks = {"AAPL", "HPQ", "INTC","NFLX"};
    //"MSFT", "FB", "AMD", "AMZN", "GOOGL", "FDX", "HAS",
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
        for (String shortName : usedStocks) {
            StockData map = new StockData.StockDataBuilder(shortName)
                    .setNameAndSector().setLatestPriceAndDate().build();
            tables.add(new AllStocksTable(map));
        }
        return tables;
    }
}
