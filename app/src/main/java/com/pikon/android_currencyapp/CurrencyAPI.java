package com.pikon.android_currencyapp;

import com.pikon.android_currencyapp.Currency.CurrencyData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyAPI {
    @GET( "latest" )
    Call<CurrencyData> getLatest(
            @Query( "base" ) String base
    );

//    @GET( "weather?appid=0450d012acf0cc600edbcf1377ca48e1&units=metric" )
//    Call<WeatherData> getCity( @Query( "q" ) String name );
}
