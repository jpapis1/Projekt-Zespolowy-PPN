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
import java.util.Date;
import java.util.HashMap;

public class StockData {
    public HashMap map;

    public StockData(String shortName, String name, String sector, double price, Date date) {
        map = new HashMap();
        setSector(sector);
        setShortName(shortName);
        setName(name);
        setPrice(price);
        setDate(date);
    }

    /*public StockData(String shortName, String name, String sector) {
        map = new HashMap();
        setSector(sector);
        setShortName(shortName);
        setName(name);
    }
    public StockData(String shortName, Date date, double price) {
        map = new HashMap();
        setPrice(price);
        setShortName(shortName);
        setDate(date);
    }*/
    public StockData() {
    }

    @Override
    public String toString() {
        return getShortName() + " | " + getName() + " | " + getPrice() + " | " + getDate() + " | " + getSector();
    }

    public String getShortName() {
        return (String) map.get("shortName");
    }

    public void setShortName(String shortName) {
        map.put("shortName", shortName);
    }

    public String getName() {
        return (String) map.get("name");
    }

    public void setName(String name) {
        map.put("name", name);
    }

    public double getPrice() {
        return (Double) map.get("price");
    }

    public void setPrice(double price) {
        map.put("price", price);
    }

    public Date getDate() {
        return (Date) map.get("date");
    }

    public void setDate(Date date) {
        map.put("date", date);
    }

    public String getSector() {
        return (String) map.get("sector");
    }

    private void setSector(String sector) {
        map.put("sector", sector);
    }

    public static class StockDataBuilder {
        private HashMap map = new HashMap();

        public StockDataBuilder(String shortName) {
            map.put("shortName", shortName);
        }

        public StockDataBuilder setNameAndSector() {
            String sURL = "https://api.iextrading.com/1.0/stock/" + map.get("shortName") + "/company";
            try {
                URL url = new URL(sURL);
                URLConnection request = url.openConnection();
                request.connect();
                JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                String sector = root.getAsJsonObject().get("sector").getAsString();
                String companyName = root.getAsJsonObject().get("companyName").getAsString();
                map.put("name", companyName);
                map.put("sector", sector);
                return this;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        public StockDataBuilder setLatestPriceAndDate() {
            String sURL = "https://api.iextrading.com/1.0/stock/" + map.get("shortName") + "/chart/1m";
            try {
                URL url = new URL(sURL);
                URLConnection request = url.openConnection();
                request.connect();
                JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));

                //String range = root.getAsJsonObject().get("range").getAsString();
                Date date = new Date();
                JsonArray array = root.getAsJsonArray();
                JsonObject latestElement = new JsonObject();

                double price = 0.0;
               // if(range.equals("1m")) {
                    latestElement = array.get(array.size() - 1).getAsJsonObject();
                    price = latestElement.get("close").getAsDouble();
                    String date_s = latestElement.get("date").getAsString();
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                    date = dt.parse(date_s);
/*
                } else if(range.equals("1d")) {
                    latestElement = array.get(array.size() - 1).getAsJsonObject();
                    int i = 2;
                    while (latestElement.get("average").getAsDouble() == -1) {
                        latestElement = array.get(array.size() - i).getAsJsonObject();
                        i++;
                    }
                    price = latestElement.get("average").getAsDouble();
                    String date_s = latestElement.get("date").getAsString() + latestElement.get("minute").getAsString();
                    SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddhh:mm");
                    date = dt.parse(date_s);
                }*/
                map.put("date", date);
                map.put("price", price);
                return this;
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
        public StockDataBuilder setLatestPrice() {
            String sURL = "https://api.iextrading.com/1.0/stock/" + map.get("shortName") + "/chart/1m";
            try {
                URL url = new URL(sURL);
                URLConnection request = url.openConnection();
                request.connect();
                JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));

                //String range = root.getAsJsonObject().get("range").getAsString();
                JsonArray array = root.getAsJsonArray();
                JsonObject latestElement = new JsonObject();

                double price = 0.0;
                //if(range.equals("1m")) {
                    latestElement = array.get(array.size() - 1).getAsJsonObject();
                    price = latestElement.get("close").getAsDouble();
/*
                } else if(range.equals("1d")) {
                    latestElement = array.get(array.size() - 1).getAsJsonObject();
                    int i = 2;
                    while (latestElement.get("average").getAsDouble() == -1) {
                        latestElement = array.get(array.size() - i).getAsJsonObject();
                        i++;
                    }
                    price = latestElement.get("average").getAsDouble();
                }
                */
                map.put("price", price);
                return this;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public StockData build() {
            return new StockData((String) map.get("shortName"), (String) map.get("name"),
                    (String) map.get("sector"), (Double) map.get("price"), (Date) map.get("date"));
        }
    }


}
