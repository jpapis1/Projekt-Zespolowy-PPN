from django.contrib.auth.hashers import check_password
from django.contrib.auth.models import User
from django.contrib.auth.hashers import make_password
from trades.models import User



class MyBackend:
    def authenticate(self, request, username=None, password=None):
        # Check the username/password and return a user.
        
        user = User.objects.get(username=username)
        print(user.password)
        print(password)
        print(check_password(password,make_password(password)))
        password = check_password(password,user.password)
        if password:
            return user

        return None