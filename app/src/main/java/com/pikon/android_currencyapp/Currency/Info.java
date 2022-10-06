
package com.pikon.android_currencyapp.Currency;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("rate")
    @Expose
    private Double rate;

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Info{" +
                "rate=" + rate +
                '}';
    }
}
