package com.pikon.android_currencyapp.Currency;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrencyConversionData {

    @SerializedName("motd")
    @Expose
    private Motd motd;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("query")
    @Expose
    private Query query;
    @SerializedName("info")
    @Expose
    private Info info;
    @SerializedName("historical")
    @Expose
    private Boolean historical;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("result")
    @Expose
    private Double result;

    public Motd getMotd() {
        return motd;
    }

    public void setMotd(Motd motd) {
        this.motd = motd;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Boolean getHistorical() {
        return historical;
    }

    public void setHistorical(Boolean historical) {
        this.historical = historical;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

}
