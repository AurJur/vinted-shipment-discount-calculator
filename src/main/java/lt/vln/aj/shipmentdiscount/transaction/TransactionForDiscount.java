package lt.vln.aj.shipmentdiscount.transaction;

import java.math.BigDecimal;

/**
 * @author Aurelijus Jurkus
 * @since 2023-08-29
 */
public record TransactionForDiscount(TransactionWithRegularPrice transactionWithRegularPrice,
                                     BigDecimal discountApplicable,
                                     BigDecimal discountProvided) {
}
