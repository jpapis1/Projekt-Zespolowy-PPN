from django import forms
from .models import User

#     iduser = models.IntegerField(db_column='idUser', primary_key=True)  # Field name made lowercase.
#     brokersprofitmargin = models.FloatField(db_column='brokersProfitMargin')  # Field name made lowercase.
#     email = models.CharField(unique=True, max_length=255, blank=True, null=True)
#     firstname = models.CharField(db_column='firstName', max_length=255, blank=True, null=True)  # Field name made lowercase.
#     funds = models.FloatField()
#     handlingfee = models.FloatField(db_column='handlingFee')  # Field name made lowercase.
#     lastname = models.CharField(db_column='lastName', max_length=255, blank=True, null=True)  # Field name made lowercase.
#     # Password is mandatory
#     password = models.CharField(max_length=255, blank=True, null=True)
#     taxrate = models.FloatField(db_column='taxRate')  # Field name made lowercase.
#     username = models.CharField(unique=True, max_length=255, blank=True, null=True)
#     idpermission = models.IntegerField(db_column='idPermission', blank=True, null=True)  # Field name made lowercase.

class SignupForm(forms.ModelForm):
    
    class Meta:
        model = User
        fields = ('username','email','firstname','lastname','password',)
        widgets = {
            'password': forms.PasswordInput(),
        }