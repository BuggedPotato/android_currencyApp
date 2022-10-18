package com.pikon.android_currencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewCurrencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_new_currency );
        ActionBar actionBar = getSupportActionBar();
        if( actionBar != null ){
            actionBar.setTitle( "Add a currency" );
            actionBar.setDisplayHomeAsUpEnabled( true );
        }
        setList();
    }

    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if( item.getItemId() == android.R.id.home )
            this.finish();
        return super.onOptionsItemSelected( item );
    }

    private void setList(){
        ArrayList<CurrencyData> currencyData = new ArrayList<>();
        Gson gson = new Gson();

        Call<JsonObject> call = RetrofitCurrency.getClient().getSymbols();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Set<Map.Entry<String, JsonElement>> arr = response.body().getAsJsonObject("symbols").entrySet();
                for( Map.Entry<String, JsonElement> obj : arr ){
                    CurrencyData currency = gson.fromJson( obj.getValue(), CurrencyData.class );
                    Log.d( "DEBUG", currency.toString() );
                    currencyData.add( currency );
                }
                ListView lvCurrency = (ListView) findViewById( R.id.lvCurrency );
                lvCurrency.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick( AdapterView<?> adapterView, View view, int i, long l ) {
                        Intent intent = new Intent( NewCurrencyActivity.this, MainActivity.class );
                        intent.putExtra( "code", currencyData.get( i ).getCode() );
                        startActivity( intent );
                    }
                } );
                CurrencyListAdapter adapter = new CurrencyListAdapter( NewCurrencyActivity.this, currencyData,  null );
                lvCurrency.setAdapter( adapter );
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText( getApplicationContext(), "Could not connect to the network", Toast.LENGTH_LONG ).show();
            }
        });
    }
}