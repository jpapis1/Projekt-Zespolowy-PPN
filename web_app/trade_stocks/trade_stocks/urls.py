"""trade_stocks URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, re_path
from trades import views
from django.conf.urls import url
# from django.contrib.auth import views as auth_views

# TODO: Refactor urls maybe
urlpatterns = [
    path('admin/', admin.site.urls),
    url(r'^$',views.index, name='index'),
    url(r'markets',views.chart, name='markets'),
    url(r'company/form',views.company, name='company'),
    re_path(r'^company/(?P<ticker>[A-Z]+)',views.company_ticker, name='company_ticker'),
    url(r'rate_single',views.rate_single, name='rate_single'),
    url(r'signup',views.signup, name='signup'),
    url(r'login',views.loginuser, name='loginuser'),
    url(r'logout',views.logout_view, name='logout_view'),
    url(r'profile',views.profile, name='profile'),
    url(r'rate_portfolio', views.rate_portfolio, name='rate_portfolio'),
    url(r'linear_regression',views.linear_regression, name='linear_regression'),
    url(r'monte_carlo',views.monte_carlo_sim, name='monte_carlo_sim'),
    url(r'tickers', views.tickers, name='tickers'),
    url(r'about', views.about, name='about'),
    url(r'simulator', views.simulator, name='simulator'),
    url(r'features', views.features, name='features'),
    # url(r'markowitz', views.markowitz, name='markowitz'),


]
