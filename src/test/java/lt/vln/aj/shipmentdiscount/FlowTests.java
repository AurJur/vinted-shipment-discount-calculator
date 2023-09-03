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
import java.math.BigDecimal;
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
public class FlowTests {

    @ParameterizedTest
    @CsvSource({
            "input01.txt,output01.txt",
            "input02.txt,output02.txt",
            "input03.txt,output03.txt",
            "input04.txt,output04.txt",//wrong inputs test
            "input05.txt,output05.txt"//TEMP files, for test implementation/debugging
    })// need a test where LSizeShipment discount can be applied only partially.
    public void testCalculate(String inputFilename, String outputFilename) throws IOException, URISyntaxException {

        TransactionsGetter transactionGetter = new TransactionReaderFromTxtFile(inputFilename);
        List<DiscountRule> discountRuleList = List.of(
                new LowestSSizePriceRule(),
                new LSizeShipmentViaLpRule(3),
                new MonthlyLimitRule(new BigDecimal("10.00")));
        TransactionsOutput transactionOutput = new TransactionStringOutput();

        String actualOutputLine = new Flow(transactionGetter, discountRuleList, transactionOutput).calculate();
        List<String> actualOutputList = Arrays.stream(actualOutputLine.split("\n")).toList();

        Path path = Paths.get(Objects.requireNonNull(DiscountCalculationApp.class.getClassLoader()
                .getResource(outputFilename)).toURI());
        Stream<String> lines = Files.lines(path);
        List<String> expectedOutputList = lines.toList();

        Assertions.assertEquals(expectedOutputList.size(), actualOutputList.size(), "Expected and actual lists sizes do not match.");

        for (int i = 0; i < Math.max(actualOutputList.size(), expectedOutputList.size()); i++) {
            Assertions.assertEquals(expectedOutputList.get(i), actualOutputList.get(i), "At index " + i + ".");
        }
    }

}
