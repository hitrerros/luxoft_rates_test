package com.price.processor.publisher;

import com.price.processor.PriceProcessor;
import com.price.processor.consumer.CurrencyPairConsumer;
import com.price.processor.domain.CurrencyPair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PriceThrottler implements PriceProcessor {

    private final Map<PriceProcessor, CurrencyPairConsumer> observers = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void subscribe(PriceProcessor priceProcessor) {
        observers.put(priceProcessor, new CurrencyPairConsumer(priceProcessor));
    }

    @Override
    public void unsubscribe(PriceProcessor priceProcessor) {
        observers.remove(priceProcessor);
    }

    @Override
    public void onPrice(String ccyPair, double rate) {
        observers.forEach((processor, consumer) -> {
            CurrencyPair currencyPair = CurrencyPair.of(ccyPair, rate);
            consumer.getCurrencyPairQueue().offer(currencyPair);

            System.out.println("sent " + currencyPair + "to queue");
            executorService.submit(consumer);
        });
    }
}
