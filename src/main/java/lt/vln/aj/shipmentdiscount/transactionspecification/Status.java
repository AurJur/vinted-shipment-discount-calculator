package lt.vln.aj.shipmentdiscount.transactionspecification;

/**
 * @author Aurelijus Jurkus
 * @since 2023-08-29
 */
public enum Status {

    OK("Ok"),
    IGNORED("Ignored");

    private final String statusText;

    Status(String statusText) {
        this.statusText = statusText;
    }

    public String getText() {
        return this.statusText;
    }
}
