package com.ezypay.subscription;

import com.ezypay.subscription.controller.SubscriptionController;
import com.ezypay.subscription.model.Amount;
import com.ezypay.subscription.model.ErrorResponse;
import com.ezypay.subscription.model.SubscriptionRequest;
import com.ezypay.subscription.model.SubscriptionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubscriptionControllerTest {

    @Test
    void test_within_three_months() {
        Amount amt = new Amount(20, "AUD");
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setAmountValue(20);
        subscriptionRequest.setCurrency("AUD");
        subscriptionRequest.setFrequency("WEEKLY");
        subscriptionRequest.setStartDate("01/02/2018");
        subscriptionRequest.setEndDate("01/03/2018");
        subscriptionRequest.setDayOfWeek("TUESDAY");
        SubscriptionController controller = new SubscriptionController();
        ResponseEntity response = controller.getSubscription(subscriptionRequest);

        assertEquals(true, response.getBody() instanceof SubscriptionResponse);
        assertEquals(HttpStatus.OK,response.getStatusCode());

        SubscriptionResponse subscriptionResponse = (SubscriptionResponse)response.getBody();
        List<String> expectedInvoiceDates = new ArrayList<>();
        expectedInvoiceDates.add("06/02/2018");
        expectedInvoiceDates.add("13/02/2018");
        expectedInvoiceDates.add("20/02/2018");
        expectedInvoiceDates.add("27/02/2018");
        assertEquals(4, subscriptionResponse.getInvoice_dates().size());
        assertEquals(expectedInvoiceDates, subscriptionResponse.getInvoice_dates());
        assertEquals(amt.getValue(), subscriptionResponse.getAmount().getValue());
        assertEquals(amt.getCurrency(), subscriptionResponse.getAmount().getCurrency());
        assertEquals("WEEKLY", subscriptionResponse.getSubscription_type());
    }

    @Test
    public void test_over_three_months(){
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setAmountValue(20);
        subscriptionRequest.setCurrency("AUD");
        subscriptionRequest.setFrequency("WEEKLY");
        subscriptionRequest.setStartDate("01/02/2018");
        subscriptionRequest.setEndDate("01/06/2018");
        subscriptionRequest.setDayOfWeek("TUESDAY");
        SubscriptionController controller = new SubscriptionController();
        ResponseEntity response = controller.getSubscription(subscriptionRequest);

        assertEquals(true, response.getBody() instanceof ErrorResponse);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Duration must not be greater than 3 months, please enter an endDate that is within or equals to 3 months", errorResponse.getErrorMessage());
    }

    @Test
    public void test_validateRequest_fail(){
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        SubscriptionController controller = new SubscriptionController();
        ResponseEntity response = controller.getSubscription(subscriptionRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(true, response.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Missing request fields", errorResponse.getErrorMessage());

    }

    @Test
    public void test_frequency_check_fail(){
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setAmountValue(20);
        subscriptionRequest.setCurrency("AUD");
        subscriptionRequest.setFrequency("DSA");
        subscriptionRequest.setStartDate("01/02/2018");
        subscriptionRequest.setEndDate("01/03/2018");
        subscriptionRequest.setDayOfWeek("TUESDAY");
        SubscriptionController controller = new SubscriptionController();
        ResponseEntity response = controller.getSubscription(subscriptionRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(true, response.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Frequency is not a valid input, please use either DAILY, WEEKLY OR MONTHLY", errorResponse.getErrorMessage());

    }

    @Test
    public void test_day_of_week_check_fail(){
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setAmountValue(20);
        subscriptionRequest.setCurrency("AUD");
        subscriptionRequest.setFrequency("WEEKLY");
        subscriptionRequest.setStartDate("01/02/2018");
        subscriptionRequest.setEndDate("01/04/2018");
        subscriptionRequest.setDayOfWeek("FDS");
        SubscriptionController controller = new SubscriptionController();
        ResponseEntity response = controller.getSubscription(subscriptionRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(true, response.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Day of week is not a valid input, please use SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY", errorResponse.getErrorMessage());

    }

    @Test
    public void test_case_insensitive_pass(){
        Amount amt = new Amount(20, "AUD");
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setAmountValue(20);
        subscriptionRequest.setCurrency("aUd");
        subscriptionRequest.setFrequency("weeklY");
        subscriptionRequest.setStartDate("01/02/2018");
        subscriptionRequest.setEndDate("01/03/2018");
        subscriptionRequest.setDayOfWeek("tuesDAY");
        SubscriptionController controller = new SubscriptionController();
        ResponseEntity response = controller.getSubscription(subscriptionRequest);

        assertEquals(true, response.getBody() instanceof SubscriptionResponse);
        assertEquals(HttpStatus.OK,response.getStatusCode());

        SubscriptionResponse subscriptionResponse = (SubscriptionResponse)response.getBody();

        assertEquals(4, subscriptionResponse.getInvoice_dates().size());
        assertEquals(amt.getValue(), subscriptionResponse.getAmount().getValue());
        assertEquals(amt.getCurrency(), subscriptionResponse.getAmount().getCurrency());
        assertEquals("WEEKLY", subscriptionResponse.getSubscription_type());
    }

    @Test
    void test_monthly_pass() {
        Amount amt = new Amount(50, "AUD");
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setAmountValue(50);
        subscriptionRequest.setCurrency("AUD");
        subscriptionRequest.setFrequency("MONTHLY");
        subscriptionRequest.setStartDate("01/02/2018");
        subscriptionRequest.setEndDate("01/05/2018");
        subscriptionRequest.setDayOfWeek("TUESDAY");
        SubscriptionController controller = new SubscriptionController();
        ResponseEntity response = controller.getSubscription(subscriptionRequest);

        assertEquals(true, response.getBody() instanceof SubscriptionResponse);
        assertEquals(HttpStatus.OK,response.getStatusCode());

        SubscriptionResponse subscriptionResponse = (SubscriptionResponse)response.getBody();

        assertEquals(3, subscriptionResponse.getInvoice_dates().size());
        assertEquals(amt.getValue(), subscriptionResponse.getAmount().getValue());
        assertEquals(amt.getCurrency(), subscriptionResponse.getAmount().getCurrency());
        assertEquals("MONTHLY", subscriptionResponse.getSubscription_type());
    }

    @Test
    void test_DAILY_pass() {
        Amount amt = new Amount(50, "AUD");
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setAmountValue(50);
        subscriptionRequest.setCurrency("AUD");
        subscriptionRequest.setFrequency("daily");
        subscriptionRequest.setStartDate("01/02/2018");
        subscriptionRequest.setEndDate("01/05/2018");
        subscriptionRequest.setDayOfWeek("TUESDAY");
        SubscriptionController controller = new SubscriptionController();
        ResponseEntity response = controller.getSubscription(subscriptionRequest);

        assertEquals(true, response.getBody() instanceof SubscriptionResponse);
        assertEquals(HttpStatus.OK,response.getStatusCode());

        SubscriptionResponse subscriptionResponse = (SubscriptionResponse)response.getBody();

        assertEquals(89, subscriptionResponse.getInvoice_dates().size());
        assertEquals(amt.getValue(), subscriptionResponse.getAmount().getValue());
        assertEquals(amt.getCurrency(), subscriptionResponse.getAmount().getCurrency());
        assertEquals("DAILY", subscriptionResponse.getSubscription_type());
    }
}