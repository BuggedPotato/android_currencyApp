package com.pikon.android_currencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_graph );

        ActionBar actionBar = getSupportActionBar();
        if( actionBar != null ){
            actionBar.setTitle( "Price changes" );
            actionBar.setDisplayHomeAsUpEnabled( true );
        }
    }

    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if( item.getItemId() == android.R.id.home )
            this.finish();
        return super.onOptionsItemSelected( item );
    }
}