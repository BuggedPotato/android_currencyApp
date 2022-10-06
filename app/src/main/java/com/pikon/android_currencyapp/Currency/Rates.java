package com.pikon.android_currencyapp.Currency;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;
import java.util.stream.Collectors;

public class Rates {
	public Map<String, Double> rates;

	public static String mapToString( Map<String, Double> map ) {
		return map.keySet().stream()
				.map( k -> k + ": " + map.get( k ) )
				.collect( Collectors.joining( ", ", "{ ", " }" ) );
	}

	@Override
	public String toString() {
		return "Rates{" +
				"rates=" + Rates.mapToString( rates ) +
				'}';
	}
}