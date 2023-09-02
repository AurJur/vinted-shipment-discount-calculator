package lt.vln.aj.shipmentdiscount.discountrule;

import lt.vln.aj.shipmentdiscount.transaction.Transaction;
import lt.vln.aj.shipmentdiscount.transaction.TransactionForDiscount;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static lt.vln.aj.shipmentdiscount.transactionspecification.Carrier.LP;
import static lt.vln.aj.shipmentdiscount.transactionspecification.Size.L;

/**
 * With current implementation should be used as one class instance per user.
 * Assumptions:
 * 2015-02-01 L LP//NOT FREE01
 * 2015-02-01 L LP//NOT FREE02
 * 2015-02-01 L LP//FREE
 * 2015-02-01 L LP//NOT FREE01
 * 2015-02-01 L LP//NOT FREE02
 * 2015-02-01 L LP//NOT FREE03 BUT FREE APPLICABLE FOR FIRST SHIPMENT ON A NEW MONTH
 * 2015-03-01 L LP//FREE
 * 2015-03-01 L LP//NOT FREE01
 * 2015-03-01 L LP//NOT FREE02
 * 2015-03-01 L LP//NOT FREE03 BUT FREE APPLICABLE FOR FIRST SHIPMENT ON A NEW MONTH
 * 2015-03-01 L LP//NOT FREE04 BUT FREE APPLICABLE FOR FIRST SHIPMENT ON A NEW MONTH
 * 2015-04 NO SHIPMENTS
 * 2015-05-01 L LP//FREE
 * 2015-05-01 L LP//NOT FREE01
 *
 * @author Aurelijus Jurkus
 * @since 2023-08-28
 */
public class LSizeShipmentViaLpRule implements DiscountRule {

    private final Integer freeShipmentNumber;
    private final List<YearMonth> discountProvidedMonthList = new ArrayList<>();
    private int shipmentCounter = 0;

    public LSizeShipmentViaLpRule(Integer freeShipmentNumber) {
        this.freeShipmentNumber = freeShipmentNumber;
    }

    @Override
    public TransactionForDiscount apply(TransactionForDiscount t) {
        Transaction tr = t.transactionWithRegularPrice().transaction();
        BigDecimal discountApplicable = t.discountApplicable();
        if (tr.carrier() == LP && tr.size() == L) {
            shipmentCounter++;
            YearMonth yearMonth = YearMonth.of(tr.date().getYear(), tr.date().getMonth());
            if (shipmentCounter >= freeShipmentNumber && !discountProvidedMonthList.contains(yearMonth)) {
                shipmentCounter = 0;
                discountProvidedMonthList.add(yearMonth);
                discountApplicable = t.discountApplicable().add(t.transactionWithRegularPrice().regularPrice());
            }
        }
        return new TransactionForDiscount(
                t.transactionWithRegularPrice(),
                discountApplicable,
                t.discountProvided());
    }

}
