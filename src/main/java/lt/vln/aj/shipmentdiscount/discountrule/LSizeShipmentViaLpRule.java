package lt.vln.aj.shipmentdiscount.discountrule;

import lt.vln.aj.shipmentdiscount.transaction.Transaction;
import lt.vln.aj.shipmentdiscount.transaction.TransactionForDiscount;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import static lt.vln.aj.shipmentdiscount.transactionspecification.Carrier.LP;
import static lt.vln.aj.shipmentdiscount.transactionspecification.Size.L;

/**
 * @author Aurelijus Jurkus
 * @since 2023-08-28
 */
public class LSizeShipmentViaLpRule implements DiscountRule {

    private final Integer maxFreeShipmentsInAMonth;
    private final Map<YearMonth, Integer> discountProvidedMonthMap = new HashMap<>();

    public LSizeShipmentViaLpRule(Integer maxFreeShipmentsInAMonth) {
        this.maxFreeShipmentsInAMonth = maxFreeShipmentsInAMonth;
    }

    @Override
    public TransactionForDiscount apply(TransactionForDiscount t) {
        Transaction tr = t.transactionWithRegularPrice().transaction();
        if (tr.carrier() == LP && tr.size() == L) {
            YearMonth yearMonth = YearMonth.of(tr.date().getYear(), tr.date().getMonth());
            if (!discountProvidedMonthMap.containsKey(yearMonth)) {
                discountProvidedMonthMap.put(yearMonth, 0);
            }
            Integer count = discountProvidedMonthMap.get(yearMonth);
            discountProvidedMonthMap.put(yearMonth, ++count);
            BigDecimal discountApplicable;
            if (count.equals(maxFreeShipmentsInAMonth)) {
                discountApplicable = t.discountApplicable().add(t.transactionWithRegularPrice().regularPrice());
            } else {
                discountApplicable = t.discountApplicable();
            }
            return new TransactionForDiscount(
                    t.transactionWithRegularPrice(),
                    discountApplicable,
                    t.discountProvided());
        } else {
            return new TransactionForDiscount(
                    t.transactionWithRegularPrice(),
                    t.discountApplicable(),
                    t.discountProvided());
        }
    }

}
