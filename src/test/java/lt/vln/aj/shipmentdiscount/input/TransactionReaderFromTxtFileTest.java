package lt.vln.aj.shipmentdiscount.input;

import lt.vln.aj.shipmentdiscount.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static lt.vln.aj.shipmentdiscount.transactionspecification.Carrier.LP;
import static lt.vln.aj.shipmentdiscount.transactionspecification.Carrier.MR;
import static lt.vln.aj.shipmentdiscount.transactionspecification.Size.*;
import static lt.vln.aj.shipmentdiscount.transactionspecification.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionReaderFromTxtFileTest {

    @Test
    void testGetTransactions_allValidVariationsOfShipments() {
        TransactionReaderFromTxtFile reader = new TransactionReaderFromTxtFile("input\\all-variations-of-shipments.txt");
        List<Transaction> actualTransactions = reader.getTransactions();
        List<Transaction> expectedTransactions = List.of(
                Transaction.builder()
                        .status(OK)
                        .originalLine("2015-02-01 S MR")
                        .date(LocalDate.of(2015, 2, 1))
                        .size(S)
                        .carrier(MR).build(),
                Transaction.builder()
                        .status(OK)
                        .originalLine("2015-02-02 M MR")
                        .date(LocalDate.of(2015, 2, 2))
                        .size(M)
                        .carrier(MR).build(),
                Transaction.builder()
                        .status(OK)
                        .originalLine("2015-02-03 L MR")
                        .date(LocalDate.of(2015, 2, 3))
                        .size(L)
                        .carrier(MR).build(),
                Transaction.builder()
                        .status(OK)
                        .originalLine("2015-02-04 S LP")
                        .date(LocalDate.of(2015, 2, 4))
                        .size(S)
                        .carrier(LP).build(),
                Transaction.builder()
                        .status(OK)
                        .originalLine("2015-02-05 M LP")
                        .date(LocalDate.of(2015, 2, 5))
                        .size(M)
                        .carrier(LP).build(),
                Transaction.builder()
                        .status(OK)
                        .originalLine("2015-02-06 L LP")
                        .date(LocalDate.of(2015, 2, 6))
                        .size(L)
                        .carrier(LP).build());

        assertThat(actualTransactions.size()).as("Number of transactions should be 6.").isEqualTo(6);
        assertThat(actualTransactions).containsExactlyElementsOf(expectedTransactions);
    }

    @Test
    void testGetTransactions_nonexistentFile() {
        TransactionReaderFromTxtFile reader = new TransactionReaderFromTxtFile("input\\non-existent-file.txt");

        assertThatThrownBy(reader::getTransactions)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Check input value and input file.");
    }
}

