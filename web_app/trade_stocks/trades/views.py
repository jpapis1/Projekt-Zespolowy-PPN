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
from .forms import SignupForm, LoginForm
from django.http import HttpResponseRedirect
from django.contrib.auth.hashers import make_password
from django.shortcuts import redirect
from django.contrib.auth import authenticate, login, logout
import bcrypt
from trades.models import User
from django.contrib.auth.decorators import login_required


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
@csrf_exempt
def company(request):
    if request.method == 'GET':
        return render(request,'stock_form.html')
    elif request.method == 'POST':
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

def rate_single(request):
    # Pick and stay with one - logarythmic for single asset over time
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
            return render(request,'rate_single_form.html',{'error_msg':error_msg})
    
        return render(request, 'rate_single.html',{'annual_return':annual_log_return, 'script':script,
        'div':div,'start':start,'end':end})

def signup(request):
    if request.method == 'POST':
        form = SignupForm(request.POST)
        reg_message = "Registration failed."
        if (form.is_valid()):
            user = form.save(commit=False)
            pw = form.cleaned_data['password']
            print(pw)
            user.password = make_password(pw)
            user.idbroker = 1
            user.funds = 3000
            user.idpermission = 2
            user.is_authenticated = False
            user.save()
            # form = SignupForm()
            reg_message = "Registration Successful!"
        # return redirect('login.html', reg_message='bar')            
        return render(request,'success.html',{'reg_message':reg_message})

    elif request.method == 'GET':
        form = SignupForm()
        return render(request,'registration_form.html',{'form': form})

def loginuser(request):
    if request.method == 'POST':
        username = request.POST['username']
        password = request.POST['password']
        print(username)
        print(password)
        print(make_password('password'))
        print(make_password('password'))
        user = authenticate(request, username=username, password=password)
        if user is not None:
            login(request, user)
            # Redirect to a success page.
            print(user.id)
            print(User.objects.get(pk=user.id))

            # return render(request,'index.html', {'u': username, 'p':password})
            return redirect('index')
        else:
            # Return an 'invalid login' error message.           
            return render(request,'login.html')

    elif request.method == 'GET':
        form = LoginForm()
        return render(request,'login.html',{'form': form})

    return render(request, 'login.html')

def logout_view(request):
    logout(request)
    return redirect('index')

@login_required
def profile(request):

    return render(request, 'profile.html')

@login_required    
def rate_portfolio(request):
    if request.method == 'GET':
        return render(request,'rate_portfolio.html')
        print("GET")
    elif request.method == 'POST':
        # TODO: implement input validation/errors
        # TODO: option to get tickers and weights from users portfolios
        # TODO: plots
        # tickers = []
        tickers = request.POST.getlist('tickers[]')

        start = request.POST.get('start')
        end = request.POST.get('end')
        try:
            # Calling the API through the module and calculating log RoR
            start = datetime(2017, 11, 1)
            end = datetime(2018, 11, 20)

            # tickers = ['PG','MSFT','F','GE']

            mydata = pd.DataFrame()
            for t in tickers:
                mydata[t] = iex.get_historical_data(t, start=start, end=end, output_format='pandas')['close']

            # Normalization to 100 - displays the behavior of a stock
            # (mydata / mydata.iloc[0] * 100).plot(figsize = (15,6));
            # plt.show()

            # Prices
            # mydata.plot(figsize=(15,6))
            # plt.show()

            returns = (mydata / mydata.shift(1)) - 1
            
            # Calculate percentage from amount of stocks that user has. - even by default
            weights = np.array([1/len(tickers)] * len(tickers))

            annual_returns = returns.mean() * 250

            np.dot(annual_returns, weights)
            pfolio_1 = str(round(np.dot(annual_returns,weights), 5) * 100) + ' %'
            
            # Plotting
            # df.reset_index(inplace = True)
            # df['date'] = pd.to_datetime(df['date'])
            # source = ColumnDataSource(df)
            # plot = figure(x_axis_type='datetime',title="Close prices")
            # plot.line(x='date', y='close', source=source)
            # script, div = components(plot)
        except:
            return render(request,'rate_portfolio.html',{'error_msg':error_msg})
    
        return render(request, 'rate_portfolio.html',{'pfolio':pfolio_1,'start':start,'end':end})

    return render(request, 'rate_portfolio.html')


