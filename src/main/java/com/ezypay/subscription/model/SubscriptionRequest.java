package com.ezypay.subscription.model;

public class SubscriptionRequest {

    public Integer amountValue;
    public String frequency;
    public String startDate;
    public String endDate;
    public String currency;
    public String dayOfWeek;

    public int getAmountValue() { return amountValue; }

    public void setAmountValue(Integer amountValue) {
        this.amountValue = amountValue;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getStartDate() { return startDate; }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDayOfWeek() { return dayOfWeek; }

    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }


}
