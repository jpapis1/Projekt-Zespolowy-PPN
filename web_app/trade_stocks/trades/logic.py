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
from io import BytesIO
import base64
     
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

        #Create Each Simulation as a column
        for x in range(num_simulations):
            count = 0
            daily_vol = returns.std()
            
            price_series = []
            
            #Append Start Value
            price = last_price * (1 + np.random.normal(0, daily_vol))
            price_series.append(price)
            
            #Series for Predicted Days
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

        #Note we are assuming drift here
        simulation_df = pd.DataFrame()
        
        #Create Each Simulation as a Column in df
        for x in range(num_simulations):
            
            #Inputs
            count = 0
            avg_daily_ret = returns.mean()
            variance = returns.var()
            
            daily_vol = returns.std()
            daily_drift = avg_daily_ret - (variance/2)
            drift = daily_drift - 0.5 * daily_vol ** 2
            
            #Append Start Value    
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
        # buf.flush()
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
        # buf2.flush()
        buf2.close()
        return image_base642
        # plt.show()
        
    def key_stats(self):
        simulation_df = self.simulation_df
 
        print ('#------------------Simulation Stats------------------#')

        
        print ('\n')
        
        print ('#----------------------Last Price Stats--------------------#')
        print ("Mean Price: ", np.mean(simulation_df.iloc[-1,:]))
        print ("Maximum Price: ",np.max(simulation_df.iloc[-1,:]))
        print ("Minimum Price: ", np.min(simulation_df.iloc[-1,:]))
        print ("Standard Deviation: ",np.std(simulation_df.iloc[-1,:]))
 
        print ('\n')
       
        print ('#----------------------Descriptive Stats-------------------#')
        price_array = simulation_df.iloc[-1, :]
        print (price_array.describe())
 
        print ('\n')
        return np.mean(simulation_df.iloc[-1,:]), np.max(simulation_df.iloc[-1,:]), np.min(simulation_df.iloc[-1,:]),np.std(simulation_df.iloc[-1,:]),price_array.describe()