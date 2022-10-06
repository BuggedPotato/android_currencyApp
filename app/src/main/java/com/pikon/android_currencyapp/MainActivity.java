package com.pikon.android_currencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pikon.android_currencyapp.Currency.CurrencyData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		getDataFoo();
	}


	private void getDataFoo() {
		String url_str = "https://api.exchangerate.host/latest";

		try {
			URL url = new URL( url_str );
			HttpURLConnection request = (HttpURLConnection)url.openConnection();
			request.connect();

			JsonParser jp = new JsonParser();
			JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
			JsonObject jsonobj = root.getAsJsonObject();

			String req_result = jsonobj.get("result").getAsString();
//			Log.d( "DEBUG", req_result );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}


	private void getData() {
		Call<CurrencyData> call = RetrofitCurrency.getClient().getLatest( "EUR" );
		call.enqueue( new Callback<CurrencyData>() {
			@Override
			public void onResponse( Call<CurrencyData> call, Response<CurrencyData> response ) {
				Log.d( "DEBUG", response.body().toString() );
			}

			@Override
			public void onFailure( Call<CurrencyData> call, Throwable t ) {
				t.printStackTrace();
			}
		} );
	}
}