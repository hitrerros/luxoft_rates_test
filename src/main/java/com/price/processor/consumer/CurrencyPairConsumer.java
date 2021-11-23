package com.price.processor.consumer;

import com.price.processor.PriceProcessor;
import com.price.processor.domain.CurrencyPair;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Data
public class CurrencyPairConsumer implements Runnable {

    private BlockingQueue<CurrencyPair> currencyPairQueue;
    private final PriceProcessor priceProcessor;

    public CurrencyPairConsumer(PriceProcessor priceProcessor) {
        this.priceProcessor = priceProcessor;
        this.currencyPairQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        while (true) {
            CurrencyPair pair = currencyPairQueue.peek();
            if (pair == null) {
                break;
            }

            List<CurrencyPair> bufferedPairs = new LinkedList<>();
            currencyPairQueue.drainTo(bufferedPairs);

            Map<String, Double> latestRates = new LinkedHashMap<>();
            bufferedPairs.forEach(k -> latestRates.put(k.getCcyPair(), k.getRate()));
            latestRates.forEach(priceProcessor::onPrice);
        }
    }
}
