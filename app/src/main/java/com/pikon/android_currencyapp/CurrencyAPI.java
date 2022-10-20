package com.pikon.android_currencyapp;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyAPI {
    @GET( "latest" )
    Call<JsonObject> getLatest(
            @Query( "base" ) String base,
            @Query( "symbols" ) String symbols
    );

    @GET( "symbols" )
    Call<JsonObject> getSymbols();

    @GET( "timeseries" )
    Call<JsonObject> getSeriesData(
            @Query( "base" ) String base,
            @Query( "start_date" ) String start,
            @Query( "end_date" ) String end,
            @Query( "symbols" ) String symbols
    );
}
