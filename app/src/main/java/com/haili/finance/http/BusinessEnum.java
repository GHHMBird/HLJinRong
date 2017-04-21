package com.haili.finance.http;


public enum BusinessEnum {
    BUSINESS_USER(0),BUSINESS_INDEX(1),BUSINESS_MANAGE(2),BUSINESS_PROPERTY(3);

    private final int value;

    BusinessEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
