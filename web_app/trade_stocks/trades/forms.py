from django import forms
from .models import User

    # iduser = models.IntegerField(db_column='idUser', primary_key=True)  # Field name made lowercase.
    # email = models.CharField(unique=True, max_length=255, blank=True, null=True)
    # firstname = models.CharField(db_column='firstName', max_length=255, blank=True, null=True)  # Field name made lowercase.
    # funds = models.FloatField()
    # lastname = models.CharField(db_column='lastName', max_length=255, blank=True, null=True)  # Field name made lowercase.
    # password = models.CharField(max_length=255, blank=True, null=True)
    # username = models.CharField(unique=True, max_length=255, blank=True, null=True)
    # idbroker = models.IntegerField(db_column='idBroker', blank=True, null=True)  # Field name made lowercase.
    # idpermission = models.IntegerField(db_column='idPermission', blank=True, null=True)  # Field name made lowercase.

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