package com.ezypay.subscription.controller;

import com.ezypay.subscription.model.Amount;
import com.ezypay.subscription.model.SubscriptionRequest;
import com.ezypay.subscription.model.SubscriptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class SubscriptionController {
    private static final List<String> FREQ = new ArrayList<String>(){{
        add("DAILY");
        add("WEEKLY");
        add("MONTHLY");
    }};
    private static final List<String> DAY = new ArrayList<String>(){{
        add("SUNDAY");
        add("MONDAY");
        add("TUESDAY");
        add("WEDNESDAY");
        add("THURSDAY");
        add("FRIDAY");
        add("SATURDAY");
    }};

    @PostMapping("/subscription")
    public ResponseEntity getSubscription(@RequestBody SubscriptionRequest request){
        SubscriptionResponse response = new SubscriptionResponse();
        if(validateRequest(request)){
            LocalDate startDate = convertStringDateToDateFormat(request.getStartDate());
            LocalDate endState = convertStringDateToDateFormat(request.getEndDate());

            if(!checkIfDurationisThreeMonthsOrLess(startDate, endState)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End Date is outside of 3 months");
            }

            if(!FREQ.contains(request.getFrequency().toUpperCase())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Frequency is not a valid input, please use either DAILY, WEEKLY OR MONTHLY");
            }else if(!DAY.contains(request.getDayOfWeek().toUpperCase())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Day of week is not a valid input, please use SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY");
            }
            response.setSubscription_type(request.getFrequency());
            List<String> invoiceDates = determineInvoiceDates(startDate, endState, request.getDayOfWeek(), request.getFrequency());
            response.setInvoice_dates(invoiceDates);
            Amount amount = new Amount();
            amount.setValue(request.getAmountValue());
            amount.setCurrency(request.getCurrency());
            response.setAmount(amount);
            response.setId(createUniqueId());
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing request fields");
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private boolean validateRequest(SubscriptionRequest request){
        if(null == request){
            return false;
        }
        else if(request.getAmountValue() < 0
                || StringUtils.isEmpty(request.getCurrency())
                || StringUtils.isEmpty(request.getStartDate())
                || StringUtils.isEmpty(request.getEndDate())){
            return false;
        }
        return true;
    }

    private LocalDate convertStringDateToDateFormat(String date){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        return localDate;
    }

    private boolean checkIfDurationisThreeMonthsOrLess(LocalDate startDate, LocalDate endDate){
        if(endDate.isBefore(startDate.plusMonths(3)) || endDate.isEqual(startDate.plusMonths(3))){
            return true;
        }
        return false;
    }

    private List<String> determineInvoiceDates(LocalDate startDate, LocalDate endDate, String dayOfWeek, String frequency){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<String> invoiceDates = new ArrayList<>();
        if(frequency.toUpperCase().equals("WEEKLY")){
            while(!startDate.getDayOfWeek().toString().toUpperCase().equals(dayOfWeek.toUpperCase())){
                startDate = startDate.plusDays(1);
            }
            invoiceDates.add(startDate.format(formatter));
        }
        createInvoiceListDate(startDate, endDate, frequency, formatter, invoiceDates);
        return invoiceDates;
    }

    private void createInvoiceListDate(LocalDate startDate, LocalDate endDate, String frequency, DateTimeFormatter formatter, List<String> invoiceDates){
        if(!StringUtils.isEmpty(frequency) && frequency.toUpperCase().equals("WEEKLY")){
            while(startDate.isBefore(endDate)){
                startDate = startDate.plusWeeks(1);
                if(startDate.isAfter(endDate)){
                    break;
                }
                invoiceDates.add(startDate.format(formatter));
            }
        }else if(!StringUtils.isEmpty(frequency) && frequency.toUpperCase().equals("DAILY")){
            while(startDate.isBefore(endDate)){
                startDate = startDate.plusDays(1);
                if(startDate.isAfter(endDate)){
                    break;
                }
                invoiceDates.add(startDate.format(formatter));
            }
        }else if(!StringUtils.isEmpty(frequency) && frequency.toUpperCase().equals("MONTHLY")){
            while(startDate.isBefore(endDate)){
                startDate = startDate.plusMonths(1);
                if(startDate.isAfter(endDate)){
                    break;
                }
                invoiceDates.add(startDate.format(formatter));
            }
        }
    }

    private String createUniqueId(){
        return UUID.randomUUID().toString();
    }

}
