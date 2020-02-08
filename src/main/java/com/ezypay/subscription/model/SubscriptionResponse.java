package com.ezypay.subscription.model;

import java.util.List;

public class SubscriptionResponse {
    public String id;
    public Amount amount;
    public String subscription_type;
    public List<String> invoice_dates;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getSubscription_type() {
        return subscription_type;
    }

    public void setSubscription_type(String subscription_type) {
        this.subscription_type = subscription_type;
    }

    public List<String> getInvoice_dates() {
        return invoice_dates;
    }

    public void setInvoice_dates(List<String> invoice_dates) {
        this.invoice_dates = invoice_dates;
    }



}
