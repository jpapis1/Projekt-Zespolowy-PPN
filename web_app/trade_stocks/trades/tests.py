# from django.test import TestCase
from django.http import HttpRequest
from django.test import SimpleTestCase
from django.urls import reverse
import datetime
from dateutil.relativedelta import relativedelta
# Create your tests here.

from . import views
from . import logic


class HomePageTests(SimpleTestCase):

    # Check if pages are available.

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


    # Check if single_ror responds to start date before limit
    def test_single_ror_function_start_before_limit(self):
        ticker = "AAPL"
        start = "2010-01-01"
        end = datetime.date.today()
        # annual_log_return, script, div = logic.single_ror(ticker, start, end)
        # self.assertEquals(annual_log_return, False)
        self.assertRaises(ValueError, logic.single_ror, ticker,start,end)

    # Check if single_ror with invalid date input still returns a valid response.
    def test_single_ror_response_start_before_limit(self):
        response = self.client.post('/rate_single',{'start':'2010-01-01','end':datetime.date.today(), 'ticker':'AAPL'})
        # annual_log_return, script, div = logic.single_ror(ticker, start, end)
        # self.assertEquals(annual_log_return, False)
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'rate_single.html')
        # self.assertRaises(ValueError, logic.single_ror, ticker,start,end)
        # 
    def test_single_ror_response_no_params(self):
        response = self.client.post('/rate_single',{})
        # annual_log_return, script, div = logic.single_ror(ticker, start, end)
        # self.assertEquals(annual_log_return, False)
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'rate_single_form.html')    

    def test_single_ror_response_invalid_ticker(self):
        response = self.client.post('/rate_single',{'start':'2010-01-01','end':datetime.date.today(), 'ticker':'AAAAAAAAPL'})
        # annual_log_return, script, div = logic.single_ror(ticker, start, end)
        # self.assertEquals(annual_log_return, False)
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'rate_single_form.html')

    def test_single_ror_response_end_date_after_limit(self):
        response = self.client.post('/rate_single',{'start':'2010-01-01','end':datetime.date.today() + relativedelta(years=2), 'ticker':'AAPL'})
        # annual_log_return, script, div = logic.single_ror(ticker, start, end)
        # self.assertEquals(annual_log_return, False)
        self.assertEquals(response.status_code, 200)
        self.assertTemplateUsed(response, 'rate_single.html')  

    # def test_view_url_by_name(self):
    #     response = self.client.get(reverse('index'))
    #     self.assertEquals(response.status_code, 200)

    # def test_view_uses_correct_template(self):
    #     response = self.client.get(reverse('index'))
    #     self.assertEquals(response.status_code, 200)
    #     self.assertTemplateUsed(response, 'index.html')