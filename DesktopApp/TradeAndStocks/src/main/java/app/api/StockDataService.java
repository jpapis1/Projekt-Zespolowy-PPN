/*
 *    Copyright 2018-2019 Jacek Papis, Michał Piątek
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *    Data provided for free by IEX https://iextrading.com/developer/.
 *    View IEX’s Terms of Use https://iextrading.com/api-exhibit-a/.
 */

package app.api;

import app.service.UserService;
import app.view.table.AllStocksTable;
import app.view.table.MyStocksTable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class StockDataService {

    static public final String[] usedStocks = {"AMD","GOOGL","AAPL", "HPQ","MSFT", "FB", "FDX", "HAS","TWTR","MET","MCD","FOX"};
    //            , "MET", "NFLX", , "HD", "PYPL", "QCOM", "SBUX", "TGT", "TXN", "TWTR", , "VZ", "XRX"

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
                    .setNameAndSector().setLatestPriceAndDate().setLogo().build();
            tables.add(new AllStocksTable(map));
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
