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
from sklearn.linear_model import LinearRegression
from sklearn import preprocessing, cross_validation, svm
from iexfinance import get_historical_data
from sklearn.model_selection import train_test_split
from trades.logic import monte_carlo
import plotly
# Debugging
import sys

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
        return render(request,'company_form.html')
    elif request.method == 'POST':
        ticker = request.POST.get('ticker')
        return redirect('/company/'+ticker) 

def company_ticker(request,ticker):
    print("TICKER")
    print(ticker)
    try:
        # ticker = request.POST.get('ticker')
        company_json = requests.get("https://api.iextrading.com/1.0/stock/"+ticker+"/company")
        news_json = requests.get("https://api.iextrading.com/1.0/stock/"+ticker+"/news/last/5")
        try:
            company = json.loads(company_json.content)
            news = json.loads(news_json.content)
        except:
            error_msg = "Unknown Ticker"
            return render(request,'company_form.html',{'error_msg':error_msg})
            
        return render(request,'company_info.html',{'company':company,'news':news})

    except:
        return render(request,'company_form.html',{'error_msg':"error_occured"})       

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
            # print(pw)
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
            # print(user.id)
            # print(User.objects.get(pk=user.id))

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
        # print("GET")
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
            # start = datetime(2017, 11, 1)
            # end = datetime(2018, 11, 20)

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



def linear_regression(request):
    if request.method == 'GET':
        return render(request,'linear.html')
        # print("GET")
    elif request.method == 'POST':
        # TODO: implement input validation/errors
        # TODO: option to get tickersfrom users portfolios
        # TODO: plots
        
        ticker = request.POST.get('ticker')

        start = request.POST.get('start')
        if(int(start[:4]) < 2013):
            start = "2013-01-01"
        end = request.POST.get('end')
        # end = datetime.today().strftime('%Y-%m-%d')
        print(ticker)
        try:
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
            print("confidence: ", confidence)

            # Predictions for next 30 days.
            forecast_prediction = clf.predict(X_forecast)
            print(forecast_prediction)
            # TODO: Calculate general direction of the stock prices.
        except:
            print(sys.exc_info()[0])
            return render(request,'linear.html',{'error_msg':error_msg})
    
        return render(request, 'linear.html',{'forecast_pred':forecast_prediction,'start':start,'end':end,'confidence':confidence,'ticker':ticker})

    return render(request, 'linear.html')

def monte_carlo_sim(request):
    if request.method == 'GET':
        return render(request,'montecarlo.html')
        # print("GET")
    elif request.method == 'POST':
        # TODO: implement input validation/errors
        # TODO: option to get tickers from users portfolios
        # TODO: plots
        
        ticker = request.POST.get('ticker')

        start = request.POST.get('start')
        if(int(start[:4]) < 2013):
            start = "2013-01-01"
        end = request.POST.get('end')
        # end = datetime.today().strftime('%Y-%m-%d')
        print(ticker)
        try:
            # Calling the API through the module

            # df = iex.get_historical_data(ticker,start=start, end=end, output_format='pandas')['close']
            sim = monte_carlo(start, end)
 
            #symbols = ['AAPL', 'KO', 'HD', 'PM']
            #weights = [1000,1000,2000,3000]
            
            #sim.get_portfolio(symbols, weights)
            sim.get_asset(ticker)
        
            sim.monte_carlo_sim(1000, 200)
            response = sim.line_graph()
            # sim.histogram()
            sim.key_stats()
        except:
            print(sys.exc_info()[0])
            return render(request,'montecarlo.html',{'error_msg':error_msg})
    
        return render(request, 'montecarlo.html',{})

    return render(request,'montecarlo.html')



# ==================================================================================================================================================================    

# def markowitz(request):
#     if request.method == 'GET':
#         return render(request,'rate_portfolio.html')
#         # print("GET")
#     elif request.method == 'POST':
#         # TODO: implement input validation/errors
#         # TODO: option to get tickers and weights from users portfolios
#         # TODO: plots
#         print("POSTMARKOWITZ")
#         tickers = request.POST.getlist('tickers[]')

#         num_assets = len(tickers)
        
#         start = request.POST.get('start')
#         end = request.POST.get('end')
#         try:
#             # Calling the API through the module and calculating log RoR
#             start = datetime(2017, 11, 1)
#             end = datetime(2018, 11, 20)

#             mydata = pd.DataFrame()
#             for t in tickers:
#                 mydata[t] = iex.get_historical_data(t, start=start, end=end, output_format='pandas')['close']

            
#             # Calculate percentage from amount of stocks that user has. - even by default
#             log_returns = np.log(mydata / mydata.shift(1))
#             pfolio_returns = []
#             pfolio_volatilities = []

#             for x in range (1000):
#                 weights = np.random.random(num_assets)
#                 weights /= np.sum(weights)
#                 pfolio_returns.append(np.sum(weights * log_returns.mean()) * 250)
#                 pfolio_volatilities.append(np.sqrt(np.dot(weights.T, np.dot(log_returns.cov() * 250, weights))))
                
#             pfolio_returns = np.array(pfolio_returns)
#             pfolio_volatilities = np.array(pfolio_volatilities)

#             pfolio_returns, pfolio_volatilities

#             portfolios = pd.DataFrame({'Return': pfolio_returns,'Volatility': pfolio_volatilities})

#             all_rate_pfolio = []
#             all_rate_pfolio = pfolio_returns/pfolio_volatilities
#             for item in all_rate_pfolio:
#                 print(item)
#             best_rate_pfolio = max(all_rate_pfolio)

#             # print(best_rate_pfolio.sort()[-1])

#             # portfolios.plot(x='Volatility', y='Return',kind='scatter',figsize = (10,6));
#             # plt.xlabel('Expected Volatility')
#             # plt.ylabel('Expected Return')   
            
#         except:
#             return render(request,'rate_portfolio.html',{'error_msg':error_msg})
    
#         return render(request, 'rate_portfolio.html',{'best_rate_pfolio':best_rate_pfolio,'start':start,'end':end})

#     return render(request, 'rate_portfolio.html')