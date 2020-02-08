package com.ezypay.subscription.model;

public class Amount {
    public int value;
    public String currency;

    public Amount(){}

    public Amount(int value, String currency){
        this.value = value;
        this.currency = currency;
    }
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
