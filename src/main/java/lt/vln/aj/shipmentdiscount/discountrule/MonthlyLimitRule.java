package lt.vln.aj.shipmentdiscount.discountrule;

import lt.vln.aj.shipmentdiscount.transaction.Transaction;
import lt.vln.aj.shipmentdiscount.transaction.TransactionForDiscount;
import lt.vln.aj.shipmentdiscount.transactionspecification.Status;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Aurelijus Jurkus
 * @since 2023-08-28
 */
public class MonthlyLimitRule implements DiscountRule {

    private final BigDecimal freeAmountPerMonth;
    private final Map<YearMonth, BigDecimal> discountProvidedMonthMap = new HashMap<>();

    public MonthlyLimitRule(BigDecimal freeAmountPerMonth) {
        this.freeAmountPerMonth = freeAmountPerMonth;
    }

    @Override
    public TransactionForDiscount apply(TransactionForDiscount t) {
        Transaction tr = t.transactionWithRegularPrice().transaction();
        if (tr.status().equals(Status.OK)) {
            YearMonth yearMonth = YearMonth.of(tr.date().getYear(), tr.date().getMonth());
            if (!discountProvidedMonthMap.containsKey(yearMonth)) {
                discountProvidedMonthMap.put(yearMonth, freeAmountPerMonth);
            }
            BigDecimal discountAvailableForMonth = discountProvidedMonthMap.get(yearMonth);
            BigDecimal discountApplicable = t.discountApplicable();
            BigDecimal discountProvided;
            if (discountApplicable.compareTo(discountAvailableForMonth) <= 0) {
                discountProvided = discountApplicable;
                discountAvailableForMonth = discountAvailableForMonth.subtract(discountApplicable);
                discountApplicable = new BigDecimal("0.00");
            } else {
                discountProvided = discountAvailableForMonth;
                discountAvailableForMonth = new BigDecimal("0.00");
                discountApplicable = discountApplicable.subtract(discountProvided);
            }
            discountProvidedMonthMap.put(yearMonth, discountAvailableForMonth);
            return new TransactionForDiscount(
                    t.transactionWithRegularPrice(),
                    discountApplicable,
                    discountProvided);
        }
        return t;
    }

}
