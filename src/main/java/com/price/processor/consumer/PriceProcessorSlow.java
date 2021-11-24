package com.price.processor.consumer;

import com.price.processor.PriceProcessor;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PriceProcessorSlow implements PriceProcessor {

    private final long DELAY_MLS = 1000L;
    @Getter
    private final Map<String, Double> deliveredRates = new ConcurrentHashMap<>();

    @Override
    public void onPrice(String ccyPair, double rate) {
        try {
            Thread.sleep(DELAY_MLS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("delivered slowly: " + ccyPair + " " + rate);
        deliveredRates.put(ccyPair, rate);
    }

    @Override
    public void subscribe(PriceProcessor priceProcessor) {

    }

    @Override
    public void unsubscribe(PriceProcessor priceProcessor) {

    }
}
