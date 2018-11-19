from django.contrib.auth.hashers import check_password
from django.contrib.auth.models import User
from django.contrib.auth.hashers import make_password
from trades.models import User



class MyBackend:
    def authenticate(self, request, username=None, password=None):
        # Check the username/password and return a user.
        
        user = User.objects.get(username=username)

        success = check_password(password,user.password)
        print("Change")
        print(user.is_authenticated)
        user.is_authenticated = True
        print(user.is_authenticated)
        user.save()
        if success:
            return user

        return None
    
    def get_user(self, user_id):
        try:
            print(User.objects.get(pk=user_id))
            return User.objects.get(pk=user_id)
        except User.DoesNotExist:
            return None
