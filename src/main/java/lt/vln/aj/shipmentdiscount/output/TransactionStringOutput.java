package lt.vln.aj.shipmentdiscount.output;

import lt.vln.aj.shipmentdiscount.transaction.Transaction;
import lt.vln.aj.shipmentdiscount.transaction.TransactionForDiscount;

import java.math.BigDecimal;
import java.util.List;

import static lt.vln.aj.shipmentdiscount.transactionspecification.Status.OK;

/**
 * @author Aurelijus Jurkus
 * @since 2023-08-28
 */
public record TransactionStringOutput() implements TransactionsOutput {

    @Override
    public String output(List<TransactionForDiscount> transactionList) {
        StringBuilder sb = new StringBuilder();
        transactionList
                .forEach(t -> sb.append(doLine(t)).append("\n"));
        return sb.toString();
    }

    private StringBuilder doLine(TransactionForDiscount t) {
        Transaction tr = t.transactionWithRegularPrice().transaction();
        StringBuilder sb = new StringBuilder();
        if (tr.status().equals(OK)) {
            BigDecimal chargedAmount = t.transactionWithRegularPrice().regularPrice().subtract(t.discountProvided());
            return sb.append(tr.originalLine()).append(" ").append(chargedAmount).append(" ").append(getDiscountProvided(t));
        } else {
            return sb.append(tr.originalLine()).append(" ").append(tr.status().getText());
        }
    }

    private String getDiscountProvided(TransactionForDiscount t) {
        BigDecimal dp = t.discountProvided();
        return dp.compareTo(BigDecimal.ZERO) > 0 ? dp.toString() : "-";
    }

}
