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
import widgets.errormessages.ErrorMessages;
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
public class OpenPricingUploadNegativeTests extends BaseTest {

    private Publisher publisher;
    private OpenPricingPage openPricingPage;
    private List<OpenPricing> openPricingList;
    private EditOpenPricingSidebar openPricingSidebar;
    private UpdateExistingOpenPricingRulesSidebar openPricingUploadSidebar;

    private List<String> rulesName = new ArrayList<>();

    private final static String RESOURCES_DIRECTORY = "src/test/resources/csvfiles/openpricing/";

    private final static String NON_CSV_FILE = "non-csv.shell";

    private final static String ERROR_UNSUPPORTED_MEDIA_TYPE = "{ \"code\": 415, \"message\": \"unsupported media type\", " +
            "\"error\": \"The uploaded file is not a CSV file\" }";

    public OpenPricingUploadNegativeTests() {
        openPricingPage = new OpenPricingPage();
        openPricingUploadSidebar = new UpdateExistingOpenPricingRulesSidebar();
        openPricingSidebar = new EditOpenPricingSidebar();
    }

    @BeforeClass
    public void createTestData() {

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

    @Test(description = "Negative: The uploaded file is not a CSV file")
    public void uploadIsNotCSVNegative() {

        uploadData(NON_CSV_FILE);

        testStart()
                .waitAndValidate(visible, openPricingPage.getToasterMessage().getPanelError())
                .clickOnWebElement(openPricingPage.getToasterMessage().getViewErrorDetails())
                .validate(openPricingPage.getToasterMessage().getMessageError(), ERROR_UNSUPPORTED_MEDIA_TYPE)
                .clickOnWebElement(openPricingPage.getToasterMessage().getRemoveIcon())
                .waitAndValidate(not(visible), openPricingPage.getToasterMessage().getPanelError())
                .testEnd();
        closeSideBar();
    }

    @Test(description = "Negative: check errors if required fields are not selected")
    public void checkRequiredFields() {
        var errorsList = openPricingUploadSidebar.getErrorAlert().getErrorsList();

        testStart()
                .and("Open Upload sidebar")
                .clickOnWebElement(openPricingPage.getUploadOpenPricingButton())
                .clickOnWebElement(openPricingPage.getUpdateOpenPricingLink())
                .waitSideBarOpened()
                .and("Click 'Update Pricing Rules'")
                .clickOnWebElement(openPricingUploadSidebar.getUpdateExistingRulesButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, openPricingUploadSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 2)
                .validateList(errorsList, List.of(
                        ErrorMessages.PUBLISHER_NAME_ERROR_ALERT.getText(),
                        ErrorMessages.CSV_FILE_ERROR_ALERT.getText())
                )
                .then("Validate error under the 'Publisher' field")
                .validate(openPricingUploadSidebar.getErrorAlertByFieldName("Publisher Name"), ErrorMessages.PUBLISHER_NAME_ERROR_ALERT.getText())
                .validate(openPricingUploadSidebar.getErrorAlertByFieldName("CSV"), ErrorMessages.CSV_FILE_ERROR_ALERT.getText())
                .and(String.format("Select Publisher '%s'", publisher.getName()))
                .selectFromDropdown(openPricingUploadSidebar.getPublisherInput(),
                        openPricingUploadSidebar.getPublisherNameDropdownItems(), publisher.getName())
                .then("Validate error under the 'Publisher field' disappeared")
                .waitAndValidate(not(visible), openPricingUploadSidebar.getErrorAlertByFieldName("Publisher Name"))
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.CSV_FILE_ERROR_ALERT.getText())
                )
                .uploadFileFromDialog(openPricingUploadSidebar.getCsvFile(), RESOURCES_DIRECTORY + "/by too large int.csv")
                .then("Validate errors disappeared")
                .waitAndValidate(not(visible), openPricingUploadSidebar.getErrorAlertByFieldName("CSV"))
                .waitAndValidate(not(visible), openPricingUploadSidebar.getErrorAlertByFieldName("Publisher Name"))
                .validate(not(visible), openPricingUploadSidebar.getErrorAlert().getErrorPanel())
                .testEnd();
        closeSideBar();
    }

    @Test(description = "Negative: check errors if reselect file")
    public void checkErrorsDisappearAfterSelectedFile() {
        var errorsList = openPricingUploadSidebar.getErrorAlert().getErrorsList();

        testStart()
                .and("Open Upload sidebar")
                .clickOnWebElement(openPricingPage.getUploadOpenPricingButton())
                .clickOnWebElement(openPricingPage.getUpdateOpenPricingLink())
                .waitSideBarOpened()
                .and(String.format("Select Publisher '%s'", publisher.getName()))
                .selectFromDropdown(openPricingUploadSidebar.getPublisherInput(),
                        openPricingUploadSidebar.getPublisherNameDropdownItems(), publisher.getName())
                .uploadFileFromDialog(openPricingUploadSidebar.getCsvFile(), RESOURCES_DIRECTORY + "/by no data.csv")
                .and("Click 'Update Pricing Rules'")
                .clickOnWebElement(openPricingUploadSidebar.getUpdateExistingRulesButton())
                .then("Validate error under the 'CSV' field appeared")
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        "the CSV file contains no data")
                )
                .and("Select CSV again")
                .uploadFileFromDialog(openPricingUploadSidebar.getCsvFile(), RESOURCES_DIRECTORY + "/by too large int.csv")
                .then("Validate errors disappeared")
                .validate(not(visible), openPricingUploadSidebar.getErrorAlert().getErrorPanel())
                .testEnd();
        closeSideBar();
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

    @Step("Close Sidebar")
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
    private void deleteRulesIfExist(List<String> rulesName) {

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
