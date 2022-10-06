package com.pikon.android_currencyapp.Currency;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Motd {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("url")
    @Expose
    private String url;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Motd{" +
                "url='" + url + '\'' +
                '}';
    }
}
