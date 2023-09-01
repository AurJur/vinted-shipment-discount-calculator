package lt.vln.aj.shipmentdiscount.discountrule;

import lt.vln.aj.shipmentdiscount.transaction.TransactionForDiscount;

/**
 * @author Aurelijus Jurkus
 * @since 2023-08-28
 */
public interface DiscountRule {
    TransactionForDiscount apply(TransactionForDiscount transaction);
}
