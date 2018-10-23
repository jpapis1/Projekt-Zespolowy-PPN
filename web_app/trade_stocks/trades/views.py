from django.shortcuts import render
from datetime import datetime
import iexfinance as iex
import json
import requests
from bokeh.plotting import figure
from bokeh.embed import components
from datetime import datetime
import numpy as np
import pandas as pd
from bokeh.models import ColumnDataSource

error_msg = "Invalid Parameters"
# Create your views here.
def index(request):
    return render(request, 'index.html')

def chart(request):

    response = requests.get("https://api.iextrading.com/1.0/market")
    # print(response.content)
    data = json.loads(response.content)

    return render(request,'chart.html',{'data':data})

def company(request):
    if request.method == 'GET':
        return render(request,'stock_form.html')
    elif request.method == 'POST':
        # TODO: implement input validation/errors
        ticker = request.POST.get('short')
        
        company_json = requests.get("https://api.iextrading.com/1.0/stock/"+ticker+"/company")
        news_json = requests.get("https://api.iextrading.com/1.0/stock/"+ticker+"/news/last/5")
        
        try:
            company = json.loads(company_json.content)
            news = json.loads(news_json.content)
        except:
            error_msg = "Unknown Ticker"
            return render(request,'stock_form.html',{'error_msg':error_msg})
        
        return render(request,'company_info.html',{'company':company,'news':news})

def rate_single(request):
    # Pick and stay with one - log for single asset over time
    # Simple if multiple assets over the same timeframe
    # Simple Rate of return = (ending price (+dividend) - beg. price) / beg.price
    # Logarythmic ror = log (ending price / beg. price)
    # Annual return = [daily return+1]^365]*100
    if request.method == 'GET':
        return render(request,'rate_single_form.html')
    elif request.method == 'POST':
        # TODO: implement input validation/errors
        ticker = request.POST.get('short')
        start = request.POST.get('start')
        end = request.POST.get('end')

        try:
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
        except:
            # error_msg = "Invalid Parameters"
            return render(request,'rate_single_form.html',{'error_msg':error_msg})
    
        return render(request, 'rate_single.html',{'annual_return':annual_log_return, 'script':script,
        'div':div})