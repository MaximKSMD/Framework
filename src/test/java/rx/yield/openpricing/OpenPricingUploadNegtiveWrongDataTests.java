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

import java.util.ArrayList;
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
public class OpenPricingUploadNegtiveWrongDataTests extends BaseTest {

    private Publisher publisher;
    private OpenPricingPage openPricingPage;
    private List<OpenPricing> openPricingList;
    private EditOpenPricingSidebar openPricingSidebar;
    private UpdateExistingOpenPricingRulesSidebar openPricingUploadSidebar;

    private List<String> rulesName = new ArrayList<>();

    private final String RESOURCES_DIRECTORY = "src/test/resources/csvfiles/openpricing/";

    public OpenPricingUploadNegtiveWrongDataTests() {
        openPricingPage = new OpenPricingPage();
        openPricingUploadSidebar = new UpdateExistingOpenPricingRulesSidebar();
        openPricingSidebar = new EditOpenPricingSidebar();
    }

    @BeforeClass
    private void createTestData() {

        publisher = publisher()
                .createNewPublisher(captionWithSuffix("000000autoPub2"))
                .build()
                .getPublisherResponse();

        deleteRulesIfExist(List.of("upload auto one", "upload auto two", "upload auto three"));

        openPricingList = List.of(createOpenPricing("upload auto one", 4.44),
                                    createOpenPricing("upload auto two", 4.44),
                                    createOpenPricing("upload auto three", 4.44));

        testStart()
                .given("Open Open Pricing page")
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .testEnd();
    }

    @DataProvider(name = "Negative Upload")
    public Object[][] uploadFilesNegative() {
        return new Object[][]{
                {"by too large decimal.csv", "Upload CSV update zero floorPrice by too large decimal", "Row 3, Floor Price '9999999.99': A valid price is between 0.00 and 999,999.99"},
                {"by too large int.csv", "Upload CSV update zero floorPrice by too large int", "Row 3, Floor Price '99999999.00': A valid price is between 0.00 and 999,999.99"},
                {"by negative integer.csv", "Upload CSV update zero floorPrice by negative int", "Row 3, Floor Price '-999999.00': A valid price is between 0.00 and 999,999.99"},
                {"by negative decimal.csv", "Upload CSV update zero floorPrice by negative decimal", "Row 3, Floor Price '-9999.99': A valid price is between 0.00 and 999,999.99"},
                {"by negative zero.csv", "Upload CSV update zero floorPrice by negative zero", "Row 3, Floor Price '-0.01': A valid price is between 0.00 and 999,999.99"},
                {"by string value.csv", "Upload CSV update zero floorPrice by String value", "the CSV file is incorrectly formatted"},
                {"by empty floor price.csv", "Upload CSV update floorPrice by empty value", "Row 3, Floor Price '': not a valid value"},
                {"by empty open pricing name.csv", "Upload CSV update by empty open-pricing-name value", "Row 3, Rule Name '': could not match the open pricing Rule Name to the publisher"},
                {"by null floor price.csv", "Upload CSV update floorPrice by null value", "Row 3, Floor Price 'null': not a valid value"},
                {"by empty floor price header.csv", "Upload CSV incorrect floor price header", "The CSV file has incorrect headers"},
                {"by incorrect quantity of headers.csv", "Upload CSV incorrect quantity of headers", "The CSV file is incorrectly formatted"},
                {"by incorrect quantity of colunms.csv", "Upload CSV incorrect quantity of columns", "The CSV file does not have the correct number of columns"},
                {"by empty CSV file.csv", "Upload empty CSV file", "The CSV file is empty"},
                {"by no data.csv", "Upload CSV file with no data", "The CSV file contains no data"},
                {"by non-existent among others.csv", "Upload CSV update by non-existent pricing among others",
                        "Row 4, Rule Name 'upload auto four': could not match the open pricing Rule Name to the publisher"},
                {"by empty pricing name header.csv", "Upload CSV with empty pricing name header", "the CSV file has incorrect headers"},
                {"duplicated rule names.csv", "Upload CSV update by duplicated pricing names", "Row 3, Floor Price 'upload auto one': the same Rule Name already exists in row 2"},
        };
    }

    @Test(description = "Negative: Update existing open pricing rules", dataProvider = "Negative Upload")
    public void updateExistingOpenPricingRulesNegative(String filename, String descr, String errorMsg) {

        log.info(descr);

        uploadData(filename);
        checkErrorAlert(errorMsg);
        closeSideBar();

        openPricingList
                .stream()
                .forEach(e ->
                        checkDataIsNotChanged(e.getName()));
    }

    @Step("Check Error Message")
    private void checkErrorAlert(String errorMsg) {
        var errorsList = openPricingUploadSidebar.getErrorAlert().getErrorsList();

        testStart()
                .then("Validate errors in Error Panel")
                .waitAndValidate(visible, openPricingUploadSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(errorMsg))
                .testEnd();
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

    @Step("Check Floor Price is not changed")
    private void checkDataIsNotChanged(String ruleName) {

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
                        "4.44")
                .testEnd();
        closeSideBar();
    }

    @Step("Close sidebar")
    private void closeSideBar() {

        testStart()
                .and("Close sidebar")
                .clickOnWebElement(openPricingUploadSidebar.getCloseIcon())
                .waitSideBarClosed()
                .testEnd();
    }

    @AfterClass(alwaysRun = true)
    private void deletePublisher() {

        testStart()
                .logOut()
                .testEnd();

        if (publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(publisher.getId())
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted publisher %s", publisher.getId()));

        deleteOpenPricingRules();
    }

    private void deleteOpenPricingRules() {

        if (openPricingList.size() > 0) {
            for (OpenPricing rule : openPricingList) {
                if (openPricing()
                        .setCredentials(USER_FOR_DELETION)
                        .deleteOpenPricing(rule.getId())
                        .build()
                        .getResponseCode() == HttpStatus.SC_NO_CONTENT)
                    log.info(String.format("Deleted open pricing %s", rule.getId()));
            }
        }
    }

    private OpenPricing createOpenPricing(String name, Double floorPrice) {

        return openPricing()
                .createNewOpenPricing(name, floorPrice, publisher)
                .build()
                .getOpenPricingResponse();
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

    @Step("Delete rules")
    private void deleteRulesIfExist(List<String> rulesName){

        for (String name : rulesName) {

            List<OpenPricing> rules = OpenPricingPrecondition.openPricing()
                    .getOpenPricingWithFilter(Map.of("name",name))
                    .build()
                    .getOpenPricingGetAllResponse()
                    .getItems();

            deleteOpenPricingRules(rules);
        }
    }
}
