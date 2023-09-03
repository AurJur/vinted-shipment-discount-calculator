package lt.vln.aj.shipmentdiscount;

import lt.vln.aj.shipmentdiscount.discountrule.DiscountRule;
import lt.vln.aj.shipmentdiscount.input.TransactionsGetter;
import lt.vln.aj.shipmentdiscount.output.TransactionsOutput;
import lt.vln.aj.shipmentdiscount.transaction.Transaction;
import lt.vln.aj.shipmentdiscount.transaction.TransactionForDiscount;
import lt.vln.aj.shipmentdiscount.transaction.TransactionWithRegularPrice;
import lt.vln.aj.shipmentdiscount.util.RegularShippingPriceProvider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aurelijus Jurkus
 * @since 2023-08-28
 */
public class Flow {

    private final TransactionsGetter transactionsGetter;
    private final List<DiscountRule> discountRuleList;
    private final TransactionsOutput transactionsOutput;

    public Flow(
            TransactionsGetter transactionsGetter,
            List<DiscountRule> discountRuleList,
            TransactionsOutput transactionsOutput) {
        this.transactionsGetter = transactionsGetter;
        this.discountRuleList = discountRuleList;
        this.transactionsOutput = transactionsOutput;
    }

    public String calculate() {
        List<Transaction> gotTransactions = transactionsGetter.getTransactions();
        List<TransactionWithRegularPrice> transactionsWithRegularPrices
                = new RegularShippingPriceProvider().getRegularPricesFor(gotTransactions);
        List<TransactionForDiscount> transactionsForDiscount = new ArrayList<>();
        for (TransactionWithRegularPrice transactionWithRegularPrice : transactionsWithRegularPrices) {
            transactionsForDiscount
                    .add(new TransactionForDiscount(
                            transactionWithRegularPrice,
                            new BigDecimal("0.00"),
                            new BigDecimal("0.00")));
        }

        for (DiscountRule discountRule : discountRuleList) {
            transactionsForDiscount.replaceAll(discountRule::apply);
        }

        return transactionsOutput.output(transactionsForDiscount);
    }

}
