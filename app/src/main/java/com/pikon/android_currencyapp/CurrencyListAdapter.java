package com.pikon.android_currencyapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYouListener;

import java.util.ArrayList;
import java.util.Locale;

public class CurrencyListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<CurrencyData> data;
    private Activity context;
    private String base;

    public CurrencyListAdapter( Context context, ArrayList<CurrencyData> data, String base ) {
        this.context = (Activity) context;
        this.inflater = LayoutInflater.from( context );
        this.data = data;
        this.base = base;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem( int i ) {
        return data.get( i );
    }

    @Override
    public long getItemId( int i ) {
        return 0;
    }

    @Override
    public View getView( int i, View view, ViewGroup viewGroup ) {
        view = inflater.inflate( R.layout.currency_list_item, null );
        ImageView icon = (ImageView) view.findViewById( R.id.ivIcon );
        ( (TextView) view.findViewById( R.id.tvCode ) ).setText( data.get( i ).getCode() );
        ( (TextView) view.findViewById( R.id.tvFullName ) ).setText( data.get( i ).getName() );
        if( base != null ){
            String worth = data.get( i ).getAmount() * data.get( i ).getRate() + " " + data.get( i ).getCode();
            ( (TextView) view.findViewById( R.id.tvWorth ) ).setText( worth );
            String rate = String.format( Locale.ENGLISH, "1 %s = %f %s", data.get(i).getCode(), 1 / data.get( i ).getRate(), base );
            ( (TextView) view.findViewById( R.id.tvRate ) ).setText( rate );
        }

        // TODO replace with a function i guess
        String src = "https://flagicons.lipis.dev/flags/4x3/" + data.get( i ).getCode().substring( 0, 2 ).toLowerCase() + ".svg";
        GlideToVectorYou
                .init()
                .with( context )
                .withListener( new GlideToVectorYouListener() {
                    @Override
                    public void onLoadFailed() {
                        Log.e( "DEBUG", data.get( i ).getCode() + " - load failed - " + src );
                    }

                    @Override
                    public void onResourceReady() {
                    }
                } )
                .setPlaceHolder( R.drawable.flag_blank, R.drawable.flag_blank )
                .load( Uri.parse( src ), icon );

        return view;
    }
}
