package lt.vln.aj.shipmentdiscount.input;

import lt.vln.aj.shipmentdiscount.transaction.Transaction;

import java.util.List;

/**
 * Assuming there could be different ways to import/read transactions to be added in the future.
 *
 * @author Aurelijus Jurkus
 * @since 2023-08-28
 */
public interface TransactionsGetter {

    List<Transaction> getTransactions();
}
