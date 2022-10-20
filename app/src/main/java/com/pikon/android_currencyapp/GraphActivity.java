package com.pikon.android_currencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.scichart.charting.model.RenderableSeriesCollection;
import com.scichart.charting.model.dataSeries.XyDataSeries;
import com.scichart.charting.visuals.SciChartSurface;
import com.scichart.charting.visuals.axes.AutoRange;
import com.scichart.charting.visuals.axes.DateAxis;
import com.scichart.charting.visuals.axes.NumericAxis;
import com.scichart.charting.visuals.renderableSeries.BaseRenderableSeries;
import com.scichart.charting.visuals.renderableSeries.FastColumnRenderableSeries;
import com.scichart.charting.visuals.renderableSeries.FastLineRenderableSeries;
import com.scichart.core.model.DateValues;
import com.scichart.core.model.DoubleValues;
import com.scichart.core.utility.DateIntervalUtil;
import com.scichart.drawing.common.SolidBrushStyle;
import com.scichart.drawing.common.SolidPenStyle;
import com.scichart.drawing.utility.ColorUtil;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraphActivity extends AppCompatActivity {

    private SciChartSurface chartSurface;
    private XyDataSeries<Date, Double> dataSeries;
    private String base;
    private String target;
    private boolean column;
    private String time;

    private final HashMap<String, Map.Entry<Date, Date>> TIME_CONSTS = new HashMap<String, Map.Entry<Date, Date>>(){{
       put( "3D", new SimpleEntry<>( new Date(new Date().getTime() - DateIntervalUtil.fromDays( 3 )), new Date() ) );
       put( "7D", new SimpleEntry<>( new Date(new Date().getTime() - DateIntervalUtil.fromDays( 7 )), new Date() ) );
       put( "2W", new SimpleEntry<>( new Date(new Date().getTime() - DateIntervalUtil.fromDays( 14 )), new Date() ) );
       put( "1M", new SimpleEntry<>( new Date(new Date().getTime() - DateIntervalUtil.fromDays( 30 )), new Date() ) );
       put( "3M", new SimpleEntry<>( new Date(new Date().getTime() - DateIntervalUtil.fromDays( 90 )), new Date() ) );
       put( "6M", new SimpleEntry<>( new Date(new Date().getTime() - DateIntervalUtil.fromDays( 180 )), new Date() ) );
       put( "1Y", new SimpleEntry<>( new Date(new Date().getTime() - DateIntervalUtil.fromDays( 365 )), new Date() ) );
    }};

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_graph );

        ActionBar actionBar = getSupportActionBar();
        if ( actionBar != null ) {
            actionBar.setTitle( "Price changes" );
            actionBar.setDisplayHomeAsUpEnabled( true );
        }

        String extraStr = getIntent().getStringExtra( "base" );
        String extraTarget = getIntent().getStringExtra( "target" );
        if ( extraStr != null )
            base = extraStr;
        if( extraTarget != null )
            target = extraTarget;
        column = false;
        time = "7D";

        chartSurface = (SciChartSurface) findViewById( R.id.chartSurface );
        DateAxis xAxis = new DateAxis( this );
        xAxis.setAxisTitle( "Date" );
        xAxis.setAutoRange( AutoRange.Always );
//        xAxis.setMajorDelta( new Date( 1000 * 60 * 60 * 24 ) );
        NumericAxis yAxis = new NumericAxis( this );
        yAxis.setAxisTitle( String.format( "Exchange rate (%s -> %s)", base, target) );
        yAxis.setAutoRange( AutoRange.Always );
        chartSurface.getXAxes().add( xAxis );
        chartSurface.getYAxes().add( yAxis );

        SwitchCompat swColumn = (SwitchCompat) findViewById( R.id.swColumn );
        swColumn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( CompoundButton compoundButton, boolean b ) {
                dataSeries.clear();
                column = b;
                drawChart( column, TIME_CONSTS.get( time ).getKey(), TIME_CONSTS.get( time ).getValue() );
            }
        } );

        String[] buttonLabels = Arrays.stream( TimeAgo.class.getEnumConstants() ).map( l -> l.getLabel() ).toArray( String[]::new );
        addButtons( buttonLabels );
        drawChart( column, TIME_CONSTS.get( time ).getKey(), TIME_CONSTS.get( time ).getValue() );
    }

    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if ( item.getItemId() == android.R.id.home )
            this.finish();
        return super.onOptionsItemSelected( item );
    }

    private void addButtons( String[] values ) {
        LinearLayout root = (LinearLayout) findViewById( R.id.llTimeButtons );
        for ( String text : values ) {
            Button btn = new Button( this );
            btn.setText( text );
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
            lp.weight = 1;
            btn.setBackgroundResource( R.drawable.btn_time );
            btn.setTextAppearance( R.style.btnTime );
            btn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View view ) {
                    time = text;
                    dataSeries.clear();
                    drawChart( column, TIME_CONSTS.get( text ).getKey(), TIME_CONSTS.get( text ).getValue() );
                }
            } );

            root.addView( btn, lp );
        }
    }

    private void drawChart( boolean column, Date start, Date end ) {
        if( column ){
            FastColumnRenderableSeries colSeries = new FastColumnRenderableSeries();
            RenderableSeriesCollection renderableSeries = chartSurface.getRenderableSeries();
            renderableSeries.add( colSeries );
            colSeries.setFillBrushStyle( new SolidBrushStyle( ColorUtil.Orange ) );
            colSeries.setStrokeStyle( new SolidPenStyle( ColorUtil.Orange, true, 1f, null ) );
            setDataSeries( colSeries, base, target, start, end );
        }
        else{
            FastLineRenderableSeries lineSeries = new FastLineRenderableSeries();
            RenderableSeriesCollection renderableSeries = chartSurface.getRenderableSeries();
            renderableSeries.add( lineSeries );
            lineSeries.setStrokeStyle( new SolidPenStyle( ColorUtil.Orange, true, 2f, null ) );
            setDataSeries( lineSeries, base, target, start, end );
        }
        chartSurface.zoomExtents();
    }

    private void setDataSeries( BaseRenderableSeries lineSeries, String locBase, String locTarget, Date start, Date end ) {
        dataSeries = new XyDataSeries<>( Date.class, Double.class );
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Call<JsonObject> call = RetrofitCurrency.getClient().getSeriesData( locBase, formatter.format( start ), formatter.format( end ), locTarget );
        call.enqueue( new Callback<JsonObject>() {
            @Override
            public void onResponse( Call<JsonObject> call, Response<JsonObject> response ) {
                Set<Map.Entry<String, JsonElement>> arr = response.body().getAsJsonObject( "rates" ).entrySet();

                DateValues dates = new DateValues();
                DoubleValues values = new DoubleValues();
                try {
                    for ( Map.Entry<String, JsonElement> obj : arr ) {
                        dates.add( formatter.parse( obj.getKey() ) );
                        values.add( obj.getValue().getAsJsonObject().getAsJsonPrimitive( target ).getAsDouble() );
                    }
                } catch ( ParseException e ) {
                    e.printStackTrace();
                }
                dataSeries.append( dates, values );
                lineSeries.setDataSeries( dataSeries );
            }

            @Override
            public void onFailure( Call<JsonObject> call, Throwable t ) {
                Toast.makeText( GraphActivity.this, "Error reading data", Toast.LENGTH_SHORT ).show();
            }
        } );
    }
}