from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.decorators import login_required
from django.contrib.auth.hashers import make_password
from sklearn.model_selection import train_test_split
from django.views.decorators.csrf import csrf_exempt
from sklearn.linear_model import LinearRegression
from iexfinance import get_historical_data
from bokeh.models import ColumnDataSource
from .forms import SignupForm, LoginForm
from django.shortcuts import redirect
from trades.logic import monte_carlo, linear_reg, portfolio_rate, single_ror, logo_url
from django.shortcuts import render
from bokeh.embed import components
from bokeh.plotting import figure
from sklearn import preprocessing
from trades.models import User
from datetime import datetime
import iexfinance as iex
import pandas as pd
import numpy as np
import requests
import json

error_msg = "Invalid Parameters."

def index(request):
    return render(request, 'index.html')

def about(request):
    return render(request, 'about.html')

def simulator(request):
    return render(request, 'simulator.html')

def features(request):
    return render(request, 'features.html')

def chart(request):
    response = requests.get("https://api.iextrading.com/1.0/stats/historical/daily?last=14")
    data = json.loads(response.content)

    return render(request,'chart.html',{'data':data})

@csrf_exempt
def company(request):
    if request.method == 'GET':
        return render(request,'company_form.html')
    elif request.method == 'POST':
        ticker = request.POST.get('ticker')
        return redirect('/company/'+ticker) 

def company_ticker(request,ticker):
    try:
        company_json = requests.get("https://api.iextrading.com/1.0/stock/"+ticker+"/company")
        logo_json = requests.get("https://api.iextrading.com/1.0/stock/"+ticker+"/logo")
        news_json = requests.get("https://api.iextrading.com/1.0/stock/"+ticker+"/news/last/5")
        try:
            company = json.loads(company_json.content)
            news = json.loads(news_json.content)
        except:
            error_msg = "Unknown Ticker"
            return render(request,'company_form.html',{'error_msg':error_msg})
        
        logo = logo_url(ticker)

        return render(request,'company_info.html',{'company':company,'news':news,'logo':logo})

    except:
        return render(request,'company_form.html',{'error_msg':"error_occured"})       

def rate_single(request):
    if request.method == 'GET':
        return render(request,'rate_single_form.html')
    elif request.method == 'POST':
        ticker = request.POST.get('ticker')
        start = request.POST.get('start')
        end = request.POST.get('end')
        try:
            annual_log_return, script, div = single_ror(ticker, start, end)
        except:
            return render(request,'rate_single_form.html',{'error_msg':error_msg})
        
        logo = logo_url(ticker)

        return render(request, 'rate_single.html',{'annual_return':annual_log_return, 'script':script,
        'div':div,'start':start,'end':end,'ticker':ticker,'logo':logo})

def signup(request):
    if request.method == 'POST':
        form = SignupForm(request.POST)
        failed_msg = "Registration failed."
        
        if (form.is_valid()):
            # TODO: create a function and move it to the separate file
            user = form.save(commit=False)
            pw = form.cleaned_data['password']
            user.password = make_password(pw)
            user.idbroker = 6
            user.funds = 3000
            user.idpermission = 2
            user.is_authenticated = False
            user.save()
            success_msg = "Registration Successful!"
            return render(request,'registration_result.html',{'success_msg':success_msg})
        return render(request,'registration_result.html',{'failed_msg':failed_msg})
    elif request.method == 'GET':
        form = SignupForm()
        return render(request,'registration_form.html',{'form': form})

def loginuser(request):
    if request.method == 'POST':
        username = request.POST['username']
        password = request.POST['password']
        user = authenticate(request, username=username, password=password)
        if user is not None:
            login(request, user)
            return redirect('index')
        else:
            # Return an 'invalid login' error message.           
            return render(request,'login.html',{'error_msg':"Wrong Username or Password."})

    elif request.method == 'GET':
        form = LoginForm()
        return render(request,'login.html',{'form': form})

    return render(request, 'login.html')

@login_required
def logout_view(request):
    logout(request)
    return redirect('index')

@login_required
def profile(request):
    return render(request, 'profile.html')

def rate_portfolio(request):
    if request.method == 'GET':
        return render(request,'rate_portfolio_form.html')
    elif request.method == 'POST':
        tickers = request.POST.getlist('tickers[]')
        start = request.POST.get('start')
        end = request.POST.get('end')
        try:
            # # TODO: Introduce option to change weights of stocks in a portfolio (values from sliders?)
            pfolio_1 = portfolio_rate(tickers, start, end)
        except:
            return render(request,'rate_portfolio_form.html',{'error_msg':error_msg})
    
        return render(request, 'rate_portfolio.html',{'pfolio':pfolio_1,'start':start,'end':end,'tickers':tickers})

def linear_regression(request):
    if request.method == 'GET':
        return render(request,'linear_form.html')
    elif request.method == 'POST':
        ticker = request.POST.get('ticker')
        start = request.POST.get('start')
        if(int(start[:4]) < int(datetime.now().year)-5):
            start = str(datetime.now().year-5)+"-01-01"
        end = request.POST.get('end')
        try:
            confidence, forecast_prediction = linear_reg(ticker,start,end)
        except:
            return render(request,'linear_form.html',{'error_msg':error_msg})
    
        logo = logo_url(ticker)

        return render(request, 'linear.html',{'forecast_pred':forecast_prediction,'start':start,'end':end,'confidence':confidence,'ticker':ticker,'logo':logo})


def monte_carlo_sim(request):
    if request.method == 'GET':
        return render(request,'montecarlo_form.html')
    elif request.method == 'POST':
        ticker = request.POST.get('ticker')
        start = request.POST.get('start')
        # Limiting date for API calls
        if(int(start[:4]) < 2015):
            start = "2015-01-01"
        end = request.POST.get('end')
        try:
            # TODO: Move logic to the separate file and add the sources - Maybe not
            sim = monte_carlo(start, end)
            sim.get_asset(ticker)
            sim.monte_carlo_sim(500, 180)
            graph = sim.line_graph()
            mean, maximum, minimum, std, describe = sim.key_stats()
        except:
            return render(request,'montecarlo_form.html',{'error_msg':error_msg})


        logo = logo_url(ticker)
        return render(request, 'montecarlo.html',{'graph':graph,'mean':mean,'max':maximum,'min':minimum,'std':std,
        'desc':describe,'start':start,'end':end,'ticker':ticker,'logo':logo})


def tickers(request):
    tickers_json = requests.get("https://api.iextrading.com/1.0/ref-data/symbols")
    tickers = tickers_json.json()
    return render(request,'tickers.html',{'tickers':tickers})

def custom_404(request):
    return render(request, '404.html', {}, status=404)