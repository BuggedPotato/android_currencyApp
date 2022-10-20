package com.pikon.android_currencyapp;

public enum TimeAgo {
    THREE_DAYS("3D"),
    ONE_WEEK("7D"),
    TWO_WEEKS("2W"),
    ONE_MONTH("1M"),
    THREE_MONTHS("3M"),
    SIX_MONTHS("6M"),
    ONE_YEAR("1Y");

    private String label;

    TimeAgo(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
