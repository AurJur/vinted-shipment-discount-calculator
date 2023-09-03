package lt.vln.aj.shipmentdiscount;

import lt.vln.aj.shipmentdiscount.discountrule.DiscountRule;
import lt.vln.aj.shipmentdiscount.discountrule.LSizeShipmentViaLpRule;
import lt.vln.aj.shipmentdiscount.discountrule.LowestSSizePriceRule;
import lt.vln.aj.shipmentdiscount.discountrule.MonthlyLimitRule;
import lt.vln.aj.shipmentdiscount.input.TransactionReaderFromTxtFile;
import lt.vln.aj.shipmentdiscount.input.TransactionsGetter;
import lt.vln.aj.shipmentdiscount.output.TransactionStringOutput;
import lt.vln.aj.shipmentdiscount.output.TransactionsOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Aurelijus Jurkus
 * @since 2023-08-28
 */
public class FlowTest {

    @ParameterizedTest
    @CsvSource({
            "originalInput.txt,originalInputExpectedOutput.txt",
    })
    public void testCalculate_originalInput(String inputFilename, String outputFilename) throws IOException, URISyntaxException {
        commonFlow(inputFilename, outputFilename);
    }

    @ParameterizedTest
    @CsvSource({
            "invalidInputs.txt,invalidInputsExpectedOutput.txt",
    })
    public void testCalculate_invalidInputs(String inputFilename, String outputFilename) throws IOException, URISyntaxException {
        commonFlow(inputFilename, outputFilename);
    }

    @ParameterizedTest
    @CsvSource({
            "lSizeShipmentViaLpRuleInput01.txt,lSizeShipmentViaLpRuleExpectedOutput01.txt",//checks general logic
            "lSizeShipmentViaLpRuleInput02.txt,lSizeShipmentViaLpRuleExpectedOutput02.txt",//checks how free shipment is carried on to another month
            "lSizeShipmentViaLpRuleInput03.txt,lSizeShipmentViaLpRuleExpectedOutput03.txt",//checks when discount can be applied only partially
    })
    public void testCalculate_variousLSizeShipmentViaLpInputs(String inputFilename, String outputFilename) throws IOException, URISyntaxException {
        commonFlow(inputFilename, outputFilename);
    }

    private void commonFlow(String inputFilename, String outputFilename) throws URISyntaxException, IOException {
        TransactionsGetter transactionGetter = new TransactionReaderFromTxtFile(inputFilename);
        List<DiscountRule> discountRuleList = List.of(
                new LowestSSizePriceRule(),
                new LSizeShipmentViaLpRule(),
                new MonthlyLimitRule());
        TransactionsOutput transactionOutput = new TransactionStringOutput();

        String actualOutputLine = new Flow(transactionGetter, discountRuleList, transactionOutput).calculate();
        List<String> actualOutputList = Arrays.stream(actualOutputLine.split("\n")).toList();

        Path path = Paths.get(Objects.requireNonNull(DiscountCalculationApp.class.getClassLoader()
                .getResource(outputFilename)).toURI());
        List<String> expectedOutputList;
        try (Stream<String> lines = Files.lines(path)) {
            expectedOutputList = lines.toList();
        }

        Assertions.assertEquals(expectedOutputList.size(), actualOutputList.size(), "Expected and actual output lists sizes do not match.");

        for (int i = 0; i < Math.max(actualOutputList.size(), expectedOutputList.size()); i++) {
            Assertions.assertEquals(expectedOutputList.get(i), actualOutputList.get(i), "At index " + i + ".");
        }
    }

}
