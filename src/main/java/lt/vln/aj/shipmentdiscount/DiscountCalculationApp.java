package lt.vln.aj.shipmentdiscount;

import lt.vln.aj.shipmentdiscount.discountrule.DiscountRule;
import lt.vln.aj.shipmentdiscount.discountrule.LSizeShipmentViaLpRule;
import lt.vln.aj.shipmentdiscount.discountrule.LowestSSizePriceRule;
import lt.vln.aj.shipmentdiscount.discountrule.MonthlyLimitRule;
import lt.vln.aj.shipmentdiscount.input.TransactionReaderFromTxtFile;
import lt.vln.aj.shipmentdiscount.input.TransactionsGetter;
import lt.vln.aj.shipmentdiscount.output.TransactionStringOutput;
import lt.vln.aj.shipmentdiscount.output.TransactionsOutput;

import java.math.BigDecimal;
import java.util.List;

/**
 * Assumption is that this module is intended for one user. In other words, the transactions provided for the discount
 * processing are only of one user.
 */
public class DiscountCalculationApp {

    public static void main(String[] args) {

        TransactionsGetter transactionsGetter = new TransactionReaderFromTxtFile("input.txt");
        List<DiscountRule> discountRuleList = List.of(new LowestSSizePriceRule(), new LSizeShipmentViaLpRule(3), new MonthlyLimitRule(new BigDecimal("10.00")));
        TransactionsOutput transactionsOutput = new TransactionStringOutput();

        Flow flow = new Flow(transactionsGetter, discountRuleList, transactionsOutput);
        String output = flow.doStuff();
        System.out.println("output = \n" + output);
    }

}