package com.price.processor;

import com.price.processor.consumer.PriceProcessorQuick;
import com.price.processor.consumer.PriceProcessorSlow;
import com.price.processor.publisher.PriceThrottler;

public class Main {

    public static void main(String[] args) {

        PriceProcessor priceThrottler = new PriceThrottler();
        PriceProcessor slowProcesor = new PriceProcessorSlow();
        PriceProcessor quickProcessor = new PriceProcessorQuick();

        priceThrottler.subscribe(slowProcesor);
        priceThrottler.subscribe(quickProcessor);

        priceThrottler.onPrice("RURUSD", 1.02d);
        priceThrottler.onPrice("RURUSD", 1.04d);
        priceThrottler.onPrice("RURUSD", 1.05d);
        priceThrottler.onPrice("RURUSD", 1.06d);

        priceThrottler.onPrice("EURUSD", 2.04d);
        priceThrottler.onPrice("RUREUR", 666.04d);

        priceThrottler.unsubscribe(slowProcesor);
        priceThrottler.unsubscribe(quickProcessor);
    }
}
