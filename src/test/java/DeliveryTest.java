import com.price.processor.PriceProcessor;
import com.price.processor.consumer.PriceProcessorQuick;
import com.price.processor.consumer.PriceProcessorSlow;
import com.price.processor.publisher.PriceThrottler;
import org.junit.Assert;
import org.junit.Test;

public class DeliveryTest {

    @Test
    public void deliveredLatest() {

        PriceProcessor priceThrottler = new PriceThrottler();
        PriceProcessorSlow slowProcesor = new PriceProcessorSlow();
        PriceProcessorQuick quickProcessor = new PriceProcessorQuick();

        priceThrottler.subscribe(slowProcesor);
        priceThrottler.subscribe(quickProcessor);

        priceThrottler.onPrice("RURUSD", 1.01d);
        priceThrottler.onPrice("RURUSD", 1.02d);
        priceThrottler.onPrice("RURUSD", 1.04d);
        priceThrottler.onPrice("RURUSD", 1.05d);
        priceThrottler.onPrice("RURUSD", 1.06d);

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(1.06d, slowProcesor.getDeliveredRates().get("RURUSD"), 0.0);
        Assert.assertEquals(1.06d, quickProcessor.getDeliveredRates().get("RURUSD"), 0.0);
    }

}
