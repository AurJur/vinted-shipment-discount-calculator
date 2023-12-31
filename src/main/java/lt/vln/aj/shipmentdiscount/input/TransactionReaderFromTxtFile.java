package lt.vln.aj.shipmentdiscount.input;

import lt.vln.aj.shipmentdiscount.DiscountCalculationApp;
import lt.vln.aj.shipmentdiscount.transaction.Transaction;
import lt.vln.aj.shipmentdiscount.transactionspecification.Carrier;
import lt.vln.aj.shipmentdiscount.transactionspecification.Size;
import lt.vln.aj.shipmentdiscount.transactionspecification.Status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static lt.vln.aj.shipmentdiscount.transactionspecification.Status.IGNORED;
import static lt.vln.aj.shipmentdiscount.transactionspecification.Status.OK;

/**
 * @author Aurelijus Jurkus
 * @since 2023-08-28
 */
public record TransactionReaderFromTxtFile(String fileName) implements TransactionsGetter {

    @Override
    public List<Transaction> getTransactions() {
        try (InputStream in = DiscountCalculationApp.class.getResourceAsStream("/" + fileName)) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in)))) {
                Stream<String> lines = reader.lines();
                return lines
                        .map(this::stringLineToObject)
                        .toList();
            }
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Check input file.");
        }
    }

    private Transaction stringLineToObject(String line) {
        String[] split = line.split(" ");
        LocalDate date = getDate(split);
        Size size = getSize(split);
        Carrier carrier = getCarrier(split);
        Status status = (date != null && size != null && carrier != null) ? OK : IGNORED;
        date = status.equals(OK) ? date : null;
        size = status.equals(OK) ? size : null;
        carrier = status.equals(OK) ? carrier : null;
        return Transaction.builder()
                .status(status)
                .originalLine(line)
                .date(date)
                .size(size)
                .carrier(carrier).build();
    }

    private LocalDate getDate(String[] split) {
        try {
            return LocalDate.parse(split[0]);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private Size getSize(String[] split) {
        try {
            return Size.valueOf(split[1]);
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    private Carrier getCarrier(String[] split) {
        try {
            return Carrier.valueOf(split[2]);
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

}
