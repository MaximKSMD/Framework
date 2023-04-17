package rx.yield.openpricing;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.yield.openpricing.OpenPricing;
import api.preconditionbuilders.OpenPricingPrecondition;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import pages.Path;
import pages.yield.openpricing.OpenPricingPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;
import widgets.yield.openPricing.sidebar.EditOpenPricingSidebar;
import widgets.yield.openPricing.sidebar.UpdateExistingOpenPricingRulesSidebar;
import zutils.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static api.preconditionbuilders.OpenPricingPrecondition.openPricing;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Open Pricing")
public class OpenPricingUploadTests extends BaseTest {

    private OpenPricingPage openPricingPage;
    private UpdateExistingOpenPricingRulesSidebar openPricingUploadSidebar;
    private EditOpenPricingSidebar openPricingSidebar;
    private Publisher publisher;
    private List<OpenPricing> openPricingList;
    Map<String, String> fileDataMap = new HashMap<>();
    private final static String RESOURCES_DIRECTORY = "src/test/resources/csvfiles/openpricing/";

    public OpenPricingUploadTests() {
        openPricingPage = new OpenPricingPage();
        openPricingUploadSidebar = new UpdateExistingOpenPricingRulesSidebar();
        openPricingSidebar = new EditOpenPricingSidebar();
    }

    @BeforeClass
    private void createTestData() {

        publisher = publisher()
                .createNewPublisher(captionWithSuffix("000000000autoPub1"))
                .build()
                .getPublisherResponse();

        openPricingList = new ArrayList<>();

        testStart()
                .given("Open Open Pricing page")
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .testEnd();
    }

    @DataProvider(name = "Positive Upload")
    public Object[][] uploadFiles() {
        return new Object[][]{
                {"3 out of 3.csv", 0.00, ""},
                {"1 out of 3 decimal.csv", 1.50, ""},
                {"decimal into int.csv", 1.15, "Upload CSV update decimal floorPrice to int"},
                {"zero decimal.csv", 0.00, "Upload CSV update by zero decimal floorPrice"},
                {"zero to int.csv", 0.00, "Upload CSV update zero floorPrice to int"},
                {"zero decimal.csv", 0.00, "Upload CSV update zero floorPrice to max decimal"},
        };
    }

    @Test(description = "Positive: Update existing open pricing rules", dataProvider = "Positive Upload")
    public void updateExistingOpenPricingRulesPositive(String filename, Double floorPrice, String descr) {

        log.info(descr);

        getDataFromFile(filename);
        deleteIfExists();
        createOpenPricing(floorPrice);
        uploadData(filename);

        fileDataMap
                .entrySet()
                .stream()
                .forEach(e ->
                        validateFloorPrice(e.getKey()));
    }

    @Step("Upload Data")
    private void uploadData(String filename) {

        testStart()
                .and("Open Upload")
                .clickOnWebElement(openPricingPage.getUploadOpenPricingButton())
                .clickOnWebElement(openPricingPage.getUpdateOpenPricingLink())
                .waitSideBarOpened()
                .and(String.format("Select Publisher %s", publisher.getName()))
                .selectFromDropdown(openPricingUploadSidebar.getPublisherNameDropdown(),
                        openPricingUploadSidebar.getPublisherNameDropdownItems(), publisher.getName())
                .uploadFileFromDialog(openPricingUploadSidebar.getCsvFile(), RESOURCES_DIRECTORY + filename)
                .and("Click 'Update Existing Open Pricing Rules'")
                .clickOnWebElement(openPricingUploadSidebar.getUpdateExistingRulesButton())
                .testEnd();
    }


    @Step("Validate floor price updated")
    private void validateFloorPrice(String ruleName) {

        var tableData = openPricingPage.getOpenPricingTable().getTableData();

        testStart()
                .setValueWithClean(tableData.getSearch(), ruleName)
                .pressEnterKey(tableData.getSearch())
                .then("Wait data loading")
                .validateContainsText(tableData.getCellByPositionInTable(ColumnNames.NAME, 0), ruleName)
                .and("Open Sidebar and check data")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, ruleName)
                .waitSideBarOpened()
                .then("Check Floor Price")
                .validateAttribute(openPricingSidebar.getFloorPriceField().getFloorPriceInput(), "value",
                       fileDataMap.get(ruleName))
                .and("Close Open Pricing Sidebar")
                .clickOnWebElement(openPricingSidebar.getCloseIcon())
                .waitSideBarClosed()
                .testEnd();
    }

    @AfterMethod(alwaysRun = true)
    private void deleteTestData() {
        deleteOpenPricingRules(openPricingList);
    }

    @AfterClass(alwaysRun = true)
    private void deletePublisher() {

        if (publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(publisher.getId())
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted publisher %s", publisher.getId()));
    }

    private void deleteOpenPricingRules(List<OpenPricing> rulesList) {

        for (OpenPricing rule : rulesList) {
            if (openPricing()
                    .setCredentials(USER_FOR_DELETION)
                    .deleteOpenPricing(rule.getId())
                    .build()
                    .getResponseCode() == HttpStatus.SC_NO_CONTENT)
                log.info(String.format("Deleted open pricing %s", rule.getId()));
        }
    }

    private void getDataFromFile(String fileName) {

        List<String[]> fileData = FileUtils.getAllDataFromCSVWithoutHeader(RESOURCES_DIRECTORY, fileName);

        fileData.stream().forEach(
                row -> {
                    log.info(row[0]);
                    if (!row[0].isEmpty()) fileDataMap.put(row[0], row[1]);
                });
    }

    private void deleteIfExists() {

        fileDataMap
                .entrySet()
                .stream()
                .forEach(e -> findRuleAndDelete(e.getKey()));
    }

    private void findRuleAndDelete(String ruleName) {

        List<OpenPricing> rules = OpenPricingPrecondition.openPricing()
                .getOpenPricingWithFilter(Map.of("name", ruleName))
                .build()
                .getOpenPricingGetAllResponse()
                .getItems();

        deleteOpenPricingRules(rules);
    }

    private void createOpenPricing(Double floorPrice) {

        fileDataMap
                .entrySet()
                .stream()
                .forEach(e -> createOpenPricing(e.getKey(), floorPrice, publisher));
    }

    private void createOpenPricing(String name, Double floorPrice, Publisher publisher) {

        openPricingList.add(openPricing()
                .createNewOpenPricing(name, floorPrice, publisher)
                .build()
                .getOpenPricingResponse());
    }
}
