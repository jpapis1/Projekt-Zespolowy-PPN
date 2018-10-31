# This is an auto-generated Django model module.
# You'll have to do the following manually to clean this up:
#   * Rearrange models' order
#   * Make sure each model has one field with primary_key=True
#   * Make sure each ForeignKey has `on_delete` set to the desired behavior.
#   * Remove `managed = False` lines if you wish to allow Django to create, modify, and delete the table
# Feel free to rename the models, but don't rename db_table values or field names.
from django.db import models


class Permission(models.Model):
    idpermission = models.IntegerField(db_column='idPermission', primary_key=True)  # Field name made lowercase.
    name = models.CharField(unique=True, max_length=255, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'Permission'


class Transaction(models.Model):
    idtransaction = models.IntegerField(db_column='idTransaction', primary_key=True)  # Field name made lowercase.
    brokersprofitmargin = models.FloatField(db_column='brokersProfitMargin')  # Field name made lowercase.
    date = models.DateTimeField(blank=True, null=True)
    doesexists = models.TextField(db_column='doesExists')  # Field name made lowercase. This field type is a guess.
    handlingfee = models.FloatField(db_column='handlingFee')  # Field name made lowercase.
    isbuy = models.TextField(db_column='isBuy')  # Field name made lowercase. This field type is a guess.
    shortname = models.CharField(db_column='shortName', max_length=255, blank=True, null=True)  # Field name made lowercase.
    taxrate = models.FloatField(db_column='taxRate')  # Field name made lowercase.
    unitprice = models.FloatField(db_column='unitPrice')  # Field name made lowercase.
    units = models.FloatField()
    iduser = models.IntegerField(db_column='idUser', blank=True, null=True)  # Field name made lowercase.

    class Meta:
        managed = False
        db_table = 'Transaction'


class User(models.Model):
    iduser = models.IntegerField(db_column='idUser', primary_key=True)  # Field name made lowercase.
    brokersprofitmargin = models.FloatField(db_column='brokersProfitMargin')  # Field name made lowercase.
    email = models.CharField(unique=True, max_length=255, blank=True, null=True)
    firstname = models.CharField(db_column='firstName', max_length=255, blank=True, null=True)  # Field name made lowercase.
    funds = models.FloatField()
    handlingfee = models.FloatField(db_column='handlingFee')  # Field name made lowercase.
    lastname = models.CharField(db_column='lastName', max_length=255, blank=True, null=True)  # Field name made lowercase.
    # Password is mandatory
    password = models.CharField(max_length=255, blank=True, null=True)
    taxrate = models.FloatField(db_column='taxRate')  # Field name made lowercase.
    username = models.CharField(unique=True, max_length=255, blank=True, null=True)
    idpermission = models.IntegerField(db_column='idPermission', blank=True, null=True)  # Field name made lowercase.

    class Meta:
        managed = False
        db_table = 'User'


class HibernateSequence(models.Model):
    next_val = models.BigIntegerField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'hibernate_sequence'
