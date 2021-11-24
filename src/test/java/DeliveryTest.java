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
            Thread.sleep(20000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(1.06d, slowProcesor.getDeliveredRates().get("RURUSD"), 0.0);
        Assert.assertEquals(1.06d, quickProcessor.getDeliveredRates().get("RURUSD"), 0.0);
    }

    @Test
    public void deliveredSlowly() {

        PriceProcessor priceThrottler = new PriceThrottler();
        PriceProcessorSlow slowProcesor = new PriceProcessorSlow();

        priceThrottler.subscribe(slowProcesor);

        priceThrottler.onPrice("RURUSD", 1.01d);
        priceThrottler.onPrice("EURUSD", 1.02d);
        priceThrottler.onPrice("GPBUSD", 1.04d);
        priceThrottler.onPrice("RURGPB", 1.05d);
        priceThrottler.onPrice("EURGPB", 1.07d);

        try {
            Thread.sleep(20000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(5, slowProcesor.getDeliveredRates().size());
    }

}
