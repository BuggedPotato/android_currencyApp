package com.pikon.android_currencyapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.scichart.charting.visuals.SciChartSurface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> currencies;
    private ArrayList<CurrencyData> currencyData = new ArrayList<>();
    private String base;
    private EditText etAmount;
    private CurrencyListAdapter listAdapter;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        if( getSupportActionBar() != null )
            getSupportActionBar().hide();

        try {
            SciChartSurface.setRuntimeLicenseKey("9oQ6rAp4fFNn09eCAyb5bVBLmoK1qIKF/s8pJJcA79+O3RU/vy9pQsa/qx7k1biU0n4OuKTHV8CqLT9+TfKhPlTym+jZuMrZG6V1TNP/28bz5IJ7h8eY3E9uVvRAIRDq1tJzdP9osfB5ivtoCeRH4A1jkvEDTDxt0kRyKRKgdCO67Dxnuo1yjZz3IJFAadJzPO16uwg07sMhb29XUatiA60mI4HWCCyuPfUYxRTzfM2njzqIWJt5hpK08c4f+DzDKC1k6Usb1rgBMaIKQxaVjPeNXCIDeN+Ny7nZHYT9odNnVbWlpZ1XEGi1StxHp/k0u5lftRbYJeE1Uu65JegiDNd5DoRsVYxXHL8Kiqroygrtd1RvgpZJHtBX7CZz7wA5vqdgIdsJkEIMwbOQdnOO4EvTX+FNmx7wXWXvuJmJveu4EgGB5bj5vkl/nj0/R3lKPIZHDz/qPv6nhdSM/xHpXgbKkflQCDz+UX5XvWN95gY76XmxQsa6XbMrtsnMvddQsg18o7h0ntMVzYP4wAX1lvk3");
        } catch ( Exception e ) {
            e.printStackTrace();
            Log.e( "DEBUG", "Could not set chart licence key" );
        }

        etAmount = (EditText) findViewById( R.id.etAmount );

        currencies = getSavedPreferences( "codes" );
        String newCode = getIntent().getStringExtra( "code" );
        if ( newCode != null ) {
            currencies.add( newCode );
            saveToPreferences( "codes", currencies );
        }

        base = "EUR";
        ( (FloatingActionButton) findViewById( R.id.fabAddCurrency ) ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Intent i = new Intent( MainActivity.this, NewCurrencyActivity.class );
                startActivity( i );
            }
        } );

//        ( (FloatingActionButton) findViewById( R.id.fabGraphs ) ).setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick( View view ) {
//                Intent i = new Intent( MainActivity.this, GraphActivity.class );
//                i.putExtra( "base", base );
//                i.putExtra( "target", "PLN" );
//                startActivity( i );
//            }
//        } );

        setSpinner();
        setList();
    }


    private void setSpinner() {
        Call<JsonObject> call = RetrofitCurrency.getClient().getSymbols();
        call.enqueue( new Callback<JsonObject>() {
            @Override
            public void onResponse( Call<JsonObject> call, Response<JsonObject> response ) {
                String[] arr = response.body().getAsJsonObject( "symbols" ).keySet().toArray( new String[0] );
                Spinner spSpinner = (Spinner) findViewById( R.id.spSpinner );
                CurrencySpinnerAdapter adapter = new CurrencySpinnerAdapter( MainActivity.this, arr );
                spSpinner.setAdapter( adapter );
                int index = adapter.findItem( base );
                spSpinner.setSelection( index );

                spSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected( AdapterView<?> adapterView, View view, int i, long l ) {
                        base = spSpinner.getSelectedItem().toString();
                        if( listAdapter != null )
                            updateList( listAdapter );
                    }

                    @Override
                    public void onNothingSelected( AdapterView<?> adapterView ) {}
                } );
            }

            @Override
            public void onFailure( Call<JsonObject> call, Throwable t ) {
                Toast.makeText( getApplicationContext(), "Could not connect to the network", Toast.LENGTH_LONG ).show();
            }
        } );
    }

    private void setList() {
        if ( currencies.size() == 0 )
            return;
        Gson gson = new Gson();
        Call<JsonObject> call = RetrofitCurrency.getClient().getSymbols();
        call.enqueue( new Callback<JsonObject>() {
            @Override
            public void onResponse( Call<JsonObject> call, Response<JsonObject> response ) {
                Set<Map.Entry<String, JsonElement>> arr = response.body().getAsJsonObject( "symbols" ).entrySet();
                for ( Map.Entry<String, JsonElement> obj : arr ) {
                    if ( !currencies.contains( obj.getKey() ) )
                        continue;
                    CurrencyData currency = gson.fromJson( obj.getValue(), CurrencyData.class );
                    currencyData.add( currency );
                }

                call = RetrofitCurrency.getClient().getLatest( base, String.join( ",", currencies ) );
                call.enqueue( new Callback<JsonObject>() {
                    @Override
                    public void onResponse( Call<JsonObject> call, Response<JsonObject> response ) {
                        Set<Map.Entry<String, JsonElement>> arr = response.body().getAsJsonObject( "rates" ).entrySet();
                        int i = 0;
                        for ( Map.Entry<String, JsonElement> obj : arr ) {
                            Log.d( "DEBUG", String.valueOf( obj.getKey() ) );
                            currencyData.get( i ).setRate( obj.getValue().getAsDouble() );
                            currencyData.get( i ).setAmount( Double.parseDouble( etAmount.getText().toString() ) );
                            i++;
                        }
                        ListView lvExchangeRates = (ListView) findViewById( R.id.lvExchangeRates );
                        CurrencyListAdapter adapter = new CurrencyListAdapter( MainActivity.this, currencyData, base );
                        lvExchangeRates.setAdapter( adapter );
                        listAdapter = adapter;
                        lvExchangeRates.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick( AdapterView<?> adapterView, View view, int i, long l ) {
                                int toRemove = i;
                                Log.d( "DEBUG", currencyData.get( toRemove ).toString() );
                                Log.d( "DEBUG", currencies.get( toRemove ) );
                                new AlertDialog.Builder( MainActivity.this )
                                        .setTitle( "Are you sure?" )
                                        .setMessage( String.format( "Do you want to remove %s from the list?", currencyData.get( toRemove ).getCode() ) )
                                        .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick( DialogInterface dialogInterface, int i ) {
                                                currencies.remove( toRemove );
                                                currencyData.remove( toRemove );
                                                saveToPreferences( "codes", currencies );
                                                adapter.notifyDataSetChanged();
                                            }
                                        } )
                                        .setNegativeButton( "No", null ).show();
                                return true;
                            }
                        } );
                        lvExchangeRates.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l ) {
                                Intent intent = new Intent( MainActivity.this, GraphActivity.class );
                                intent.putExtra( "base", base );
                                intent.putExtra( "target", currencyData.get( i ).getCode() );
                                startActivity( intent );
                            }
                        } );

                        etAmount.addTextChangedListener( new TextWatcher() {
                            @Override
                            public void beforeTextChanged( CharSequence charSequence, int i, int i1, int i2 ) {
                            }

                            @Override
                            public void onTextChanged( CharSequence charSequence, int i, int i1, int i2 ) {
                                Log.d( "DEBUG", charSequence.toString() );
                                for ( CurrencyData obj : currencyData )
                                    obj.setAmount( Double.parseDouble( etAmount.getText().toString() ) );
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void afterTextChanged( Editable editable ) {
                            }
                        } );
                    }

                    @Override
                    public void onFailure( Call<JsonObject> call, Throwable t ) {
                        Toast.makeText( getApplicationContext(), "Could not connect to the network", Toast.LENGTH_LONG ).show();
                    }
                } );
            }

            @Override
            public void onFailure( Call<JsonObject> call, Throwable t ) {
                Toast.makeText( getApplicationContext(), "Could not connect to the network", Toast.LENGTH_LONG ).show();
            }
        } );
    }

    private void updateList( CurrencyListAdapter adapter ){
        if ( currencies.size() == 0 )
            return;
        Call<JsonObject> call = RetrofitCurrency.getClient().getLatest( base, String.join( ",", currencies ) );
        call.enqueue( new Callback<JsonObject>() {
            @Override
            public void onResponse( Call<JsonObject> call, Response<JsonObject> response ) {
                Set<Map.Entry<String, JsonElement>> arr = response.body().getAsJsonObject( "rates" ).entrySet();
                int i = 0;
                for ( Map.Entry<String, JsonElement> obj : arr ) {
                    Log.d( "DEBUG", String.valueOf( obj.getKey() ) );
                    currencyData.get( i ).setRate( obj.getValue().getAsDouble() );
                    currencyData.get( i ).setAmount( Double.parseDouble( etAmount.getText().toString() ) );
                    i++;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure( Call<JsonObject> call, Throwable t ) {
                Toast.makeText( getApplicationContext(), "Could not connect to the network", Toast.LENGTH_LONG ).show();
            }
        } );
    }

    private void saveToPreferences( String key, ArrayList<String> vals ) {
        SharedPreferences sharedPref = this.getPreferences( Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet( key, new HashSet<>( vals ) );
        editor.apply();
    }

    private ArrayList<String> getSavedPreferences( String key ) {
        SharedPreferences sharedPref = this.getPreferences( Context.MODE_PRIVATE );
        Set<String> set = sharedPref.getStringSet( key, new HashSet<String>() );
        ArrayList<String> arr = new ArrayList<>( set );
        Collections.sort( arr );
        return arr;
    }
}