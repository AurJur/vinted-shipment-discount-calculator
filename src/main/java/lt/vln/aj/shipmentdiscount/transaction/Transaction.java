package lt.vln.aj.shipmentdiscount.transaction;

import lt.vln.aj.shipmentdiscount.transactionspecification.Carrier;
import lt.vln.aj.shipmentdiscount.transactionspecification.Size;
import lt.vln.aj.shipmentdiscount.transactionspecification.Status;

import java.time.LocalDate;

/**
 * @author Aurelijus Jurkus
 * @since 2023-08-29
 */
public record Transaction(Status status,
                          String originalLine,
                          LocalDate date,
                          Size size,
                          Carrier carrier) {
}


