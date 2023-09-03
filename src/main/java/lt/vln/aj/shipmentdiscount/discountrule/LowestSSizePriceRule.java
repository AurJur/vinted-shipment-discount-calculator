package lt.vln.aj.shipmentdiscount.discountrule;

import lt.vln.aj.shipmentdiscount.transaction.TransactionForDiscount;
import lt.vln.aj.shipmentdiscount.util.RegularShippingPriceProvider;

import java.math.BigDecimal;

import static lt.vln.aj.shipmentdiscount.transactionspecification.Carrier.LP;
import static lt.vln.aj.shipmentdiscount.transactionspecification.Carrier.MR;
import static lt.vln.aj.shipmentdiscount.transactionspecification.Size.S;

/**
 * @author Aurelijus Jurkus
 * @since 2023-08-28
 */
public class LowestSSizePriceRule implements DiscountRule {

    @Override
    public TransactionForDiscount apply(TransactionForDiscount t) {
        if (isSizeS(t)) {
            BigDecimal discountApplicable = getDiscountApplicable(t);
            if (discountApplicable.compareTo(BigDecimal.ZERO) > 0) {
                return new TransactionForDiscount(
                        t.transactionWithRegularPrice(),
                        t.discountApplicable().add(discountApplicable),
                        t.discountProvided());
            }
        }
        return t;
    }

    private boolean isSizeS(TransactionForDiscount t) {
        return t.transactionWithRegularPrice().transaction().size() == S;
    }

    private BigDecimal getDiscountApplicable(TransactionForDiscount t) {
        BigDecimal lowest = RegularShippingPriceProvider.getFor(LP, S).min(RegularShippingPriceProvider.getFor(MR, S));
        BigDecimal subtract = t.transactionWithRegularPrice().regularPrice().subtract(lowest);
        if (subtract.compareTo(BigDecimal.ZERO) > 0) {
            return subtract;
        } else {
            return new BigDecimal("0.00");
        }
    }

}
