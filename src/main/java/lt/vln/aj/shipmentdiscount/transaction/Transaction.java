package lt.vln.aj.shipmentdiscount.transaction;

import lt.vln.aj.shipmentdiscount.transactionspecification.Carrier;
import lt.vln.aj.shipmentdiscount.transactionspecification.Size;
import lt.vln.aj.shipmentdiscount.transactionspecification.Status;

import java.time.LocalDate;

/**
 * @author Aurelijus Jurkus
 * @since 2023-08-29
 */
public class Transaction {

    private final Status status;
    private final String originalLine;
    private final LocalDate date;
    private final Size size;
    private final Carrier carrier;

    public Status status() {
        return status;
    }

    public String originalLine() {
        return originalLine;
    }

    public LocalDate date() {
        return date;
    }

    public Size size() {
        return size;
    }

    public Carrier carrier() {
        return carrier;
    }

    private Transaction(BuilderInner builderInner) {
        this.status = builderInner.status;
        this.originalLine = builderInner.originalLine;
        this.date = builderInner.date;
        this.size = builderInner.size;
        this.carrier = builderInner.carrier;
    }

    public static StatusInner builder() {
        return new BuilderInner();
    }

    public interface StatusInner {
        OriginalLineInner status(Status status);
    }

    public interface OriginalLineInner {
        DateInner originalLine(String originalLine);
    }

    public interface DateInner {
        SizeInner date(LocalDate date);
    }

    public interface SizeInner {
        CarrierInner size(Size size);
    }

    public interface CarrierInner {
        BuildInner carrier(Carrier carrier);
    }

    public interface BuildInner {
        Transaction build();
    }

    private static class BuilderInner implements
            StatusInner,
            OriginalLineInner,
            DateInner,
            SizeInner,
            CarrierInner,
            BuildInner {

        private Status status;
        private String originalLine;
        private LocalDate date;
        private Size size;
        private Carrier carrier;

        @Override
        public OriginalLineInner status(Status status) {
            this.status = status;
            return this;
        }

        @Override
        public DateInner originalLine(String originalLine) {
            this.originalLine = originalLine;
            return this;
        }

        @Override
        public SizeInner date(LocalDate date) {
            this.date = date;
            return this;
        }

        @Override
        public CarrierInner size(Size size) {
            this.size = size;
            return this;
        }

        @Override
        public BuildInner carrier(Carrier carrier) {
            this.carrier = carrier;
            return this;
        }

        @Override
        public Transaction build() {
            return new Transaction(this);
        }

    }

}


