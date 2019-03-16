from django.contrib.auth.hashers import check_password
from django.contrib.auth.models import User
from django.contrib.auth.hashers import make_password
from trades.models import User



class MyBackend:
    def authenticate(self, request, username=None, password=None):
        # Check the username and password and return a user.
        
        user = User.objects.get(username=username)

        success = check_password(password,user.password)
        user.is_authenticated = True
        user.save()
        if success:
            return user

        return None
    
    def get_user(self, user_id):
        try:
            return User.objects.get(pk=user_id)
        except User.DoesNotExist:
            return None
