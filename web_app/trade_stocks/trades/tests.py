from django.http import HttpRequest
from django.test import SimpleTestCase
from django.urls import reverse
import datetime
from dateutil.relativedelta import relativedelta
import iexfinance
# Create your tests here.

from . import views
from . import logic

class StatusCodeTests(SimpleTestCase):
    # Check if all pages are available.

    def test_home_page_status_code(self):
        response = self.client.get('/')
        self.assertEquals(response.status_code, 200)

    def test_markets_page_status_code(self):
        response = self.client.get('/markets')
        self.assertEquals(response.status_code, 200)

    def test_company_form_page_status_code(self):
        response = self.client.get('/company/form')
        self.assertEquals(response.status_code, 200)

    def test_rate_single_page_status_code(self):
        response = self.client.get('/rate_single')
        self.assertEquals(response.status_code, 200)

    def test_rate_portfolio_page_status_code(self):
        response = self.client.get('/rate_portfolio')
        self.assertEquals(response.status_code, 200)

    def test_linear_regression_page_status_code(self):
        response = self.client.get('/linear_regression')
        self.assertEquals(response.status_code, 200)

    def test_monte_carlo_page_status_code(self):
        response = self.client.get('/monte_carlo')
        self.assertEquals(response.status_code, 200)

    def test_tickers_page_status_code(self):
        response = self.client.get('/tickers')
        self.assertEquals(response.status_code, 200)

    def test_about_page_status_code(self):
        response = self.client.get('/about')
        self.assertEquals(response.status_code, 200)

    def test_simulator_status_code(self):
        response = self.client.get('/simulator')
        self.assertEquals(response.status_code, 200)

    def test_features_status_code(self):
        response = self.client.get('/features')
        self.assertEquals(response.status_code, 200)

    def test_signup_status_code(self):
        response = self.client.get('/signup')
        self.assertEquals(response.status_code, 200)

    def test_login_status_code(self):
        response = self.client.get('/login')
        self.assertEquals(response.status_code, 200)


class SingleRorTests(SimpleTestCase):
    # Test for start date before limit
    def test_single_ror_function_start_before_limit(self):
        ticker = "AAPL"
        start = "2010-01-01"
        end = datetime.date.today()
        self.assertRaises(ValueError, logic.single_ror, ticker,start,end)

    # Test for start date before limit in POST parameters
    def test_single_ror_response_start_before_limit(self):
        response = self.client.post('/rate_single',{'start':'2010-01-01',
        'end':datetime.date.today(), 'ticker':'AAPL'})
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'rate_single.html')

    # Test for POST request with no params
    def test_single_ror_response_no_params(self):
        response = self.client.post('/rate_single',{})
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'rate_single_form.html')    

    # Test for non existing ticker in POST parameters
    def test_single_ror_response_invalid_ticker(self):
        response = self.client.post('/rate_single',{'start':'2010-01-01',
        'end':datetime.date.today(), 'ticker':'AAAAAAAAPL'})
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'rate_single_form.html')

    
    # Test for end date in the future in POST parameters
    def test_single_ror_response_end_date_after_limit(self):
        response = self.client.post('/rate_single',{'start':'2010-01-01',
        'end':datetime.date.today() + relativedelta(years=2), 'ticker':'AAPL'})
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'rate_single.html')


class RatePortfolioTests(SimpleTestCase):
    # Test for start date before limit
    def test_portfolio_rate_function_start_before_limit(self):
        tickers = ["AAPL"]
        start = "2010-01-01"
        end = datetime.date.today()
        self.assertRaises(ValueError, logic.portfolio_rate, tickers,start,end)

    # Test for start date before limit in POST parameters
    def test_portfolio_rate_response_start_before_limit(self):
        response = self.client.post('/rate_portfolio',{'start':'2010-01-01',
        'end':datetime.date.today(), 'tickers[]':['AAPL']})
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'rate_portfolio.html')

    # Test for POST request with no params
    def test_portfolio_rate_response_no_params(self):
        response = self.client.post('/rate_portfolio',{})
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'rate_portfolio_form.html')    

    # Test for non existing ticker in POST parameters
    def test_portfolio_rate_response_invalid_ticker(self):
        response = self.client.post('/rate_portfolio',{'start':datetime.date.today() 
        - relativedelta(years=3),'end':datetime.date.today(), 'tickers[]':['AAAAAAAAPL']})
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'rate_portfolio_form.html')

    # Test for end date in the future in POST parameters
    def test_portfolio_rate_response_end_date_after_limit(self):
        response = self.client.post('/rate_portfolio',{'start':datetime.date.today() 
        - relativedelta(years=3),'end':datetime.date.today() + relativedelta(years=2), 'tickers[]':['AAPL']})
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'rate_portfolio.html')


class LinearRegressionTests(SimpleTestCase):
    # Test for start date before limit
    def test_linear_reg_function_start_before_limit(self):
        ticker = "AAPL"
        start = "2010-01-01"
        end = datetime.date.today()
        self.assertRaises(ValueError, logic.linear_reg, ticker,start,end)

    # Test for start date before limit in POST parameters
    def test_linear_reg_response_start_before_limit(self):
        response = self.client.post('/linear_regression',{'start':'2010-01-01',
        'end':datetime.date.today(), 'ticker':'AAPL'})
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'linear.html')

    # Test for POST request with no params
    def test_linear_reg_response_no_params(self):
        response = self.client.post('/linear_regression',{})
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'linear_form.html')    

    # Test for non existing ticker in POST parameters
    def test_linear_reg_response_invalid_ticker(self):
        response = self.client.post('/linear_regression',{'start':'2010-01-01',
        'end':datetime.date.today(), 'ticker':'AAAAAAAAPL'})
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'linear_form.html')

    # Test for end date in the future in POST parameters
    def test_linear_reg_response_end_date_after_limit(self):
        response = self.client.post('/linear_regression',{'start':'2010-01-01',
        'end':datetime.date.today() + relativedelta(years=2), 'ticker':'AAPL'})
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'linear.html')


class MonteCarloTests(SimpleTestCase):

    # Test monte carlo constructor with date before limit.
    def test_monte_carlo_function_start_before_limit(self):
        ticker = "AAPL"
        start = datetime.date.today() - relativedelta(years=10)
        end = datetime.date.today()

        sim = logic.monte_carlo(start, end)
        
        self.assertRaises(ValueError, sim.get_asset, ticker)
    
    # Test get_asset method with invalid ticker.
    def test_monte_carlo_function_invalid_ticker(self):
        ticker = "AAAAAAAAAAAAAAAAAAAAAPL"
        start = datetime.date.today() - relativedelta(years=3)
        end = datetime.date.today()
        sim = logic.monte_carlo(start, end)
        self.assertRaises(ValueError, sim.get_asset, ticker)

    # Test for start date before limit in POST parameters
    def test_monte_carlo_response_start_before_limit(self):
        response = self.client.post('/monte_carlo',{'start':'2010-01-01',
        'end':datetime.date.today(), 'ticker':'AAPL'})
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'montecarlo.html')

    # Test for POST request with no params
    def test_monte_carlo_response_no_params(self):
        response = self.client.post('/monte_carlo',{})
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'montecarlo_form.html')    

    # Test for non existing ticker in POST parameters
    def test_monte_carlo_response_invalid_ticker(self):
        response = self.client.post('/monte_carlo',{'start':'2010-01-01',
        'end':datetime.date.today(), 'ticker':'AAAAAAAAPL'})
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'montecarlo_form.html')

    # Test for end date in the future in POST parameters
    def test_monte_carlo_response_end_date_after_limit(self):
        response = self.client.post('/monte_carlo',{'start':'2010-01-01',
        'end':datetime.date.today() + relativedelta(years=2), 'ticker':'AAPL'})
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'montecarlo.html')