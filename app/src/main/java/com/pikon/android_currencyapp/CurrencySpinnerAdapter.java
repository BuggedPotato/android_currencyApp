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

import androidx.core.content.res.TypedArrayUtils;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYouListener;

import java.util.Arrays;
import java.util.Objects;

public class CurrencySpinnerAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private String[] data;
    private Activity context;

    public CurrencySpinnerAdapter(Context context, String[] data) {
        this.context = (Activity) context;
        this.inflater = LayoutInflater.from( context );
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public int findItem( String str ){
        for (int i = 0; i < data.length; i++)
            if(Objects.equals(data[i], str))
                return i;
        return -1;
    }

    //Uri.parse("https://flagicons.lipis.dev/flags/4x3/" + data[i].substring( 0, 2 ) + ".svg"
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate( R.layout.spinner_icon_item, null );
        ImageView icon = (ImageView) view.findViewById( R.id.ivIcon );
        ((TextView) view.findViewById( R.id.tvText )).setText( data[i] );
        String src = "https://flagicons.lipis.dev/flags/4x3/" + data[i].substring( 0, 2 ).toLowerCase() + ".svg";

        GlideToVectorYou
                .init()
                .with(context)
                .withListener(new GlideToVectorYouListener() {
                    @Override
                    public void onLoadFailed() {
                        Log.e( "DEBUG", data[i] + " - load failed - " + src );
                    }
                    @Override
                    public void onResourceReady() {
                    }
                })
                .setPlaceHolder( R.drawable.flag_blank, R.drawable.flag_blank )
                .load( Uri.parse( src ), icon );
        return view;
    }
}
