package lt.vln.aj.shipmentdiscount.transactionspecification;

/**
 * @author Aurelijus Jurkus
 * @since 2023-08-28
 */
public enum Carrier {

    LP("La Poste"),
    MR("Mondial Relay");

    public final String carrierName;

    Carrier(String carrierName) {
        this.carrierName = carrierName;
    }

}
