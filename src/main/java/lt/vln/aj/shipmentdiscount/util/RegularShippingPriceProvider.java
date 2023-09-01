package lt.vln.aj.shipmentdiscount.util;

import lt.vln.aj.shipmentdiscount.transaction.Transaction;
import lt.vln.aj.shipmentdiscount.transaction.TransactionWithRegularPrice;
import lt.vln.aj.shipmentdiscount.transactionspecification.Carrier;
import lt.vln.aj.shipmentdiscount.transactionspecification.Size;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static lt.vln.aj.shipmentdiscount.transactionspecification.Carrier.LP;
import static lt.vln.aj.shipmentdiscount.transactionspecification.Carrier.MR;
import static lt.vln.aj.shipmentdiscount.transactionspecification.Size.*;

/**
 * @author Aurelijus Jurkus
 * @since 2023-08-28
 */
public class RegularShippingPriceProvider {

    private static final HashMap<CarrierAndSize, BigDecimal> priceMap = new HashMap<>();

    static {
        priceMap.put(new CarrierAndSize(LP, S), new BigDecimal("1.50"));
        priceMap.put(new CarrierAndSize(LP, M), new BigDecimal("4.90"));
        priceMap.put(new CarrierAndSize(LP, L), new BigDecimal("6.90"));

        priceMap.put(new CarrierAndSize(MR, S), new BigDecimal("2.00"));
        priceMap.put(new CarrierAndSize(MR, M), new BigDecimal("3.00"));
        priceMap.put(new CarrierAndSize(MR, L), new BigDecimal("4.00"));
    }

    public List<TransactionWithRegularPrice> getRegularPricesFor(List<Transaction> transactions) {
        return transactions
                .stream()
                .map(t -> new TransactionWithRegularPrice(
                        t,
                        getFor(t.carrier(), t.size())
                ))
                .toList();
    }

    public BigDecimal getFor(Carrier carrier, Size size) {//TODO private method?
        CarrierAndSize carrierAndSize = new CarrierAndSize(carrier, size);
        BigDecimal regularPrice = priceMap.get(carrierAndSize);
        return regularPrice;
    }

    private record CarrierAndSize(Carrier carrier,
                                  Size size) {
    }
}
