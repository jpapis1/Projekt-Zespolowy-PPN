from django.shortcuts import render
from datetime import datetime
import iexfinance as iex
import json
import requests

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
        # TODO: implement financial info calls from the API
        
        company_json = requests.get("https://api.iextrading.com/1.0/stock/"+ticker+"/company")
        news_json = requests.get("https://api.iextrading.com/1.0/stock/"+ticker+"/news/last/5")
        
        try:
            company = json.loads(company_json.content)
            news = json.loads(news_json.content)
        except:
            error_msg = "Unknown Ticker"
            return render(request,'stock_form.html',{'error_msg':error_msg})
        
        return render(request,'stock_info.html',{'company':company,'news':news})

def rate(requests):
    # Pick and stay with one - log for single asset over time
    # Simple if multiple assets over the same timeframe
    # Simple Rate of return = (ending price (+dividend) - beg. price) / beg.price
    # Logarythmic ror = log (ending price / beg. price)
    # Annual return = [daily return+1]^365]*100

    
    return render(request, 'rate_single.html')