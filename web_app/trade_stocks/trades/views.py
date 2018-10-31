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
from django.views.decorators.csrf import csrf_exempt
from .forms import SignupForm
from django.http import HttpResponseRedirect

error_msg = "Invalid Parameters"

user_id_counter = 0
# Create your views here.
def index(request):
    return render(request, 'index.html')

def chart(request):

    response = requests.get("https://api.iextrading.com/1.0/market")
    # print(response.content)
    data = json.loads(response.content)

    return render(request,'chart.html',{'data':data})

# TODO: Make sure that post call to this from the desktop app can go through(csrf_exempt maybe token authentication)
def company(request):
    if request.method == 'GET':
        return render(request,'stock_form.html')
    elif request.method == 'POST':
        # TODO: change to form for company so it ccould use GET to retrieve info like this here.
        try:
            ticker = request.POST.get('ticker')
            company_json = requests.get("https://api.iextrading.com/1.0/stock/"+ticker+"/company")
            news_json = requests.get("https://api.iextrading.com/1.0/stock/"+ticker+"/news/last/5")
            try:
                company = json.loads(company_json.content)
                news = json.loads(news_json.content)
            except:
                error_msg = "Unknown Ticker"
                return render(request,'stock_form.html',{'error_msg':error_msg})
            
            return render(request,'company_info.html',{'company':company,'news':news})

        except:
            return render(request,'stock_form.html',{'error_msg':"error_occured"})

        # TODO: Fix that
        # if(type(request.GET.get('ticker')) != None):
        #     ticker = request.GET.get('short')
        #     company_json = requests.get("https://api.iextrading.com/1.0/stock/"+ticker+"/company")
        #     news_json = requests.get("https://api.iextrading.com/1.0/stock/"+ticker+"/news/last/5")
        #     try:
        #         company = json.loads(company_json.content)
        #         news = json.loads(news_json.content)
        #     except:
        #         error_msg = "Unknown Ticker"
        #         return render(request,'stock_form.html',{'error_msg':error_msg})

        #     return render(request,'company_info.html',{'company':company,'news':news})
        # else:
        #     return render(request,'stock_form.html')
        

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
        'div':div,'start':start,'end':end})

def signup(request):
    if request.method == 'POST':
        form = SignupForm(request.POST)
        if (form.is_valid()):
            # Add additional stuff that's not in forms here
            # Get number of users in db and add one to that
            # and use it as an id
            user = form.save(commit=False)
            # TODO: ENCRYPT THE PASSWORDS
            # TODO: Fix the user_id assignment
            user.iduser = 1
            user.brokersprofitmargin = 0
            user.handlingfee = 0
            user.taxrate = 0
            user.funds = 1000000
            # user_id_counter += 1
            user.save()
            # return HttpResponseRedirect(reverse_lazy(index))
            form = SignupForm()
        return render(request,'registration_form.html',{'form': form})

    elif request.method == 'GET':
        form = SignupForm()
        return render(request,'registration_form.html',{'form': form})
