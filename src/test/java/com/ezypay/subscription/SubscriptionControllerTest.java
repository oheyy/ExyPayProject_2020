package com.ezypay.subscription;

import com.ezypay.subscription.controller.SubscriptionController;
import com.ezypay.subscription.model.SubscriptionRequest;
import org.junit.jupiter.api.Test;

class SubscriptionControllerTest {

    @Test
    void getSubscription() {
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setAmountValue(20);
        subscriptionRequest.setCurrency("AUD");
        subscriptionRequest.setFrequency("WEEKLY");
        subscriptionRequest.setStartDate("01/02/2018");
        subscriptionRequest.setEndDate("01/03/2018");
        subscriptionRequest.setDayOfWeek("TUESDAY");
        SubscriptionController controller = new SubscriptionController();
        controller.getSubscription(subscriptionRequest);

    }
}