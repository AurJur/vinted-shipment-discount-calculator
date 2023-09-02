package lt.vln.aj.shipmentdiscount.input;

import lt.vln.aj.shipmentdiscount.DiscountCalculationApp;
import lt.vln.aj.shipmentdiscount.transaction.Transaction;
import lt.vln.aj.shipmentdiscount.transactionspecification.Carrier;
import lt.vln.aj.shipmentdiscount.transactionspecification.Size;
import lt.vln.aj.shipmentdiscount.transactionspecification.Status;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
public class TransactionReaderFromTxtFile implements TransactionsGetter {

    private final String fileName;

    public TransactionReaderFromTxtFile(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Transaction> getTransactions() {
        try {
            Path path = Paths.get(Objects.requireNonNull(DiscountCalculationApp.class.getClassLoader().getResource(fileName)).toURI());
            try (Stream<String> lines = Files.lines(path)) {
                return lines
                        .map(this::stringLineToObject)
                        .toList();
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
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
        return new Transaction(status, line, date, size, carrier);
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
