import pandas as pd
import datetime
import numpy as np
import math
from matplotlib import style
import matplotlib.pyplot as plt
import matplotlib.mlab as mlab
from iexfinance import get_historical_data
from matplotlib import pylab
from django.http import HttpResponse
from matplotlib.backends.backend_agg import FigureCanvasAgg
from bokeh.models import ColumnDataSource
from bokeh.plotting import figure
from bokeh.embed import components
import requests
import json



# from iexfinance import get_historical_data # Change that
import iexfinance as iex

from io import BytesIO
import base64
from sklearn import preprocessing

from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split



# TODO: Add source for monte carlo
class monte_carlo:
    def __init__(self, start, end):
        self.start = start
        self.end = end

    def get_asset(self, symbol):
        #Dates
        start = self.start 
        end = self.end 

        prices = get_historical_data(symbol,start=start, end=end, output_format='pandas')['close']
        returns = prices.pct_change()
        self.returns = returns
        self.prices = prices

    def monte_carlo_sim(self, num_simulations, predicted_days):
        returns = self.returns
        prices = self.prices
        
        last_price = prices[-1]
        
        simulation_df = pd.DataFrame()

        # Create Each Simulation as a column
        for x in range(num_simulations):
            count = 0
            # Daily Volatility - standard deviation of returns
            daily_vol = returns.std()
            
            price_series = []
            
            # Append Start Value
            # Start Value is the last price varying by random value from a range of 0 to daily volatility
            price = last_price * (1 + np.random.normal(0, daily_vol))

            # First of the price series is starting price.
            price_series.append(price)
            
            # Series for Predicted Days - until count is 251 change price by random value withitn daily volatility
            for i in range(predicted_days):
                if count == 251:
                    break
                price = price_series[count] * (1 + np.random.normal(0, daily_vol))
                price_series.append(price)
                count += 1
        
            simulation_df[x] = price_series
            self.simulation_df = simulation_df
            self.predicted_days = predicted_days

    def brownian_motion(self, num_simulations, predicted_days):
        returns = self.returns
        prices = self.prices

        last_price = prices[-1]

        # Note we are assuming drift here
        simulation_df = pd.DataFrame()
        
        # Create Each Simulation as a Column in df
        for x in range(num_simulations):
            
            # Inputs
            count = 0
            avg_daily_ret = returns.mean()
            variance = returns.var()
            
            daily_vol = returns.std()
            daily_drift = avg_daily_ret - (variance/2)
            drift = daily_drift - 0.5 * daily_vol ** 2
            
            # Append Start Value    
            prices = []
            
            shock = drift + daily_vol * np.random.normal()
            last_price * math.exp(shock)
            prices.append(last_price)
            
            for i in range(predicted_days):
                if count == 251:
                    break
                shock = drift + daily_vol * np.random.normal()
                price = prices[count] * math.exp(shock)
                prices.append(price)
                
                count += 1
            simulation_df[x] = prices
            self.simulation_df = simulation_df
            self.predicted_days = predicted_days

    def line_graph(self):
        prices = self.prices
        predicted_days = self.predicted_days
        simulation_df = self.simulation_df
        
        last_price = prices[-1]
        fig = plt.figure()
        style.use('bmh')
        
        title = "Monte Carlo Simulation: " + str(predicted_days) + " Days"
        plt.plot(simulation_df)
        fig.suptitle(title,fontsize=18, fontweight='bold')
        plt.xlabel('Day')
        plt.ylabel('Price ($USD)')
        plt.grid(True,color='grey')
        plt.axhline(y=last_price, color='r', linestyle='-')

        buf = BytesIO()
        plt.savefig(buf, format='png')
        image_base64 = base64.b64encode(buf.getvalue()).decode('utf-8').replace('\n', '')
        buf.close()
        return image_base64


    def histogram(self):
        simulation_df = self.simulation_df
        
        ser = simulation_df.iloc[-1, :]
        x = ser
        mu = ser.mean()
        sigma = ser.std()
        
        num_bins = 20
        # the histogram of the data
        n, bins, patches = plt.hist(x, num_bins, density=1, facecolor='blue', alpha=0.5)
         
        # add a 'best fit' line
        y = mlab.normpdf(bins, mu, sigma)
        plt.plot(bins, y, 'r--')
        plt.xlabel('Price')
        plt.ylabel('Probability')
        plt.title(r'Histogram of Speculated Stock Prices', fontsize=18, fontweight='bold')
 
        # Tweak spacing to prevent clipping of ylabel
        plt.subplots_adjust(left=0.15)
        
        buf2 = BytesIO()
        plt.savefig(buf2, format='png')
        image_base642 = base64.b64encode(buf2.getvalue()).decode('utf-8').replace('\n', '')
        buf2.close()
        return image_base642
        
    def key_stats(self):
        simulation_df = self.simulation_df
        price_array = simulation_df.iloc[-1, :]
        return np.mean(simulation_df.iloc[-1,:]), np.max(simulation_df.iloc[-1,:]), np.min(simulation_df.iloc[-1,:]),np.std(simulation_df.iloc[-1,:]),price_array.describe()


def linear_reg(ticker, start, end):
            # Calling the API through the module
            df = iex.get_historical_data(ticker,start=start, end=end, output_format='pandas')['close']
            df = df.reset_index()
            df = df[['close']]
            forecast_out = int(30) # predicting 30 days into future
            df['Prediction'] = df[['close']].shift(-forecast_out) #  label column with data shifted 30 units up

            X = np.array(df.drop(['Prediction'], 1))
            X = preprocessing.scale(X)

            X_forecast = X[-forecast_out:] # set X_forecast equal to last 30
            X = X[:-forecast_out] # remove last 30 from X

            y = np.array(df['Prediction'])
            y = y[:-forecast_out]

            # X_train, X_test, y_train, y_test = cross_validation.train_test_split(X, y, test_size = 0.2)
            X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.2)

            # Training
            clf = LinearRegression()
            clf.fit(X_train,y_train)
            # Testing
            confidence = clf.score(X_test, y_test)

            # Predictions for next 30 days.
            forecast_prediction = clf.predict(X_forecast)
            return confidence, forecast_prediction

def portfolio_rate(tickers, start, end):
            mydata = pd.DataFrame()
            for t in tickers:
                mydata[t] = iex.get_historical_data(t, start=start, end=end, output_format='pandas')['close']

            returns = (mydata / mydata.shift(1)) - 1
            
            # Calculate weights of stocks in a portfolio - even by default
            weights = np.array([1/len(tickers)] * len(tickers))

            annual_returns = returns.mean() * 250

            np.dot(annual_returns, weights)

            return str(round(np.dot(annual_returns,weights), 5) * 100) + ' %'

def single_ror(ticker, start, end):
            # Calling the API through the module and calculating log RoR
            df = iex.get_historical_data(ticker, start=start, end=end, output_format='pandas')
            df['log_return'] = np.log(df['close'] / df['close'].shift(1))
            log_return_a = df['log_return'].mean() * 250
            annual_log_return = str(round(log_return_a, 5) * 100) + '%'
            
            # Plotting
            df.reset_index(inplace = True)
            df['date'] = pd.to_datetime(df['date'])
            source = ColumnDataSource(df)
            plot = figure(x_axis_type='datetime',title="Close prices")
            plot.line(x='date', y='close', source=source)
            script, div = components(plot)
            return annual_log_return, script, div

def logo_url(ticker):
    logo_json = requests.get("https://api.iextrading.com/1.0/stock/"+ticker+"/logo")

    try:
        logo = json.loads(logo_json.content)
    except:
        # Blank url for logo if there's an error.
        data = {}
        data['url'] = ''
        logo = json.dumps(data)

    return logo
 