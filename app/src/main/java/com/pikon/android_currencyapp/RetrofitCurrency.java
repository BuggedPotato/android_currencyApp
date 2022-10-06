package com.pikon.android_currencyapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCurrency {
    private static Retrofit retrofit;
    public static CurrencyAPI getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.exchangerate.host/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create( CurrencyAPI.class );
    }
}