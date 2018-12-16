from django import forms
from .models import User

class SignupForm(forms.ModelForm):
    
    class Meta:
        model = User
        fields = ('username','email','firstname','lastname','password',)
        widgets = {
            'password': forms.PasswordInput(),
        }

class LoginForm(forms.ModelForm):
    
    class Meta:
        model = User
        fields = ("username",'password',)
        widgets = {
            'password': forms.PasswordInput(),
        }
