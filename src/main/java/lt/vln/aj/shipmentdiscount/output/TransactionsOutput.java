package lt.vln.aj.shipmentdiscount.output;

import lt.vln.aj.shipmentdiscount.transaction.TransactionForDiscount;

import java.util.List;

/**
 * @author Aurelijus Jurkus
 * @since 2023-08-29
 */
public interface TransactionsOutput {
    String output(List<TransactionForDiscount> transactionList);
}
