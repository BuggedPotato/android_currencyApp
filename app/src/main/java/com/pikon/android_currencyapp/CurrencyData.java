package com.pikon.android_currencyapp;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class CurrencyData {
    private String code;
    @SerializedName("description")
    private String name;
    private double rate;
    private double amount;

    public CurrencyData( String code, String name, double rate ) {
        this.code = code;
        this.name = name;
        this.rate = rate;
        this.amount = 1;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate( double rate ) {
        this.rate = rate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount( double amount ) {
        this.amount = amount;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format( "{ code: %s, name: %s, rate: %f, amount: %f }", code, name, rate, amount );
    }
}
