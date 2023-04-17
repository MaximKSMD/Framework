package rx.yield.openpricing;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.admin.user.UserDto;
import api.dto.rx.yield.openpricing.OpenPricing;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import pages.Path;
import pages.yield.openpricing.OpenPricingPage;
import rx.BaseTest;
import widgets.errormessages.ErrorMessages;
import widgets.yield.openPricing.sidebar.UpdateExistingOpenPricingRulesSidebar;
import zutils.FileUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static api.preconditionbuilders.OpenPricingPrecondition.openPricing;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static api.preconditionbuilders.UsersPrecondition.user;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.*;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Open Pricing")
public class OpenPricingSingleAdminUploadSidebarTests extends BaseTest {

    private OpenPricingPage openPricingPage;
    private UpdateExistingOpenPricingRulesSidebar openPricingSidebar;
    private Publisher publisher;
    private UserDto singleUser;

    private final static String RESOURCES_DIRECTORY = "src/test/resources/csvfiles/openpricing/";

    private List<OpenPricing> rules = new ArrayList<>();
    private Map<String, String> expectedRules = new HashMap<>();

    Map<String, String> fileDataMap = new HashMap<>();

    private final static String[] FILE_HEADER = {"Rule Name", "Floor Price"};
    private final static String TEMPLATE_FILE_NAME = "open-pricing-template.csv";
    private final static String RULES_FILE_NAME = "open-pricing.csv";

    public OpenPricingSingleAdminUploadSidebarTests() {
        openPricingPage = new OpenPricingPage();
        openPricingSidebar = new UpdateExistingOpenPricingRulesSidebar();
    }

    @BeforeClass
    private void createTestData() {
        publisher = publisher()
                .createNewPublisher(captionWithSuffix("000000000autoPub1"))
                .build()
                .getPublisherResponse();

        singleUser = user()
                .createSinglePublisherUser(publisher.getId())
                .build()
                .getUserResponse();


        rules = List.of(createOpenPricingRule(true, 15.00),
                        createOpenPricingRule(true, 0.00),
                        createOpenPricingRule(true, 6.33),
                        createOpenPricingRule(false, 999999.99));

        rules.stream().forEach(rule -> expectedRules.put(rule.getName(), convertFloorPrice(rule.getFloorPrice().toString())));
    }

    @BeforeMethod
    private void loginUI() {

        testStart()
                .given("Open Open Pricing page")
                .openDirectPath(Path.OPEN_PRICING)
                .logIn(TEMP_USER)
                .waitAndValidate(disappear, openPricingPage.getNuxtProgress())
                .and("Open Upload")
                .clickOnWebElement(openPricingPage.getUploadOpenPricingButton())
                .clickOnWebElement(openPricingPage.getUpdateOpenPricingLink())
                .waitSideBarOpened()
                .testEnd();
    }


    @Test(description = "Single Admin: check elements on sidebar")
    public void checkUploadSlideElementsByDefaultSingleAdmin() {

        testStart()
                .then(String.format("Publisher Name contains publisher name %s", publisher.getName()))
                .validate(openPricingSidebar.getPublisherInput().getText(), publisher.getName())
                .clickOnWebElement(openPricingSidebar.getPublisherNameDropdown())
                .validate(openPricingSidebar.getPublisherNameDropdownItems().size(), 1)
                .then("CSV template is visible")
                .validate(visible, openPricingSidebar.getDownloadCSVTemplateButton())
                .then("Download Existing Open Pricing Rules should be visible")
                .validate(visible, openPricingSidebar.getDownloadExistingOpenPricingRulesButton())
                .then("'Upload your CSV file' text is present")
                .validate(visible, openPricingSidebar.getUploadCSVText())
                .then("File input is present")
                .validate(visible, openPricingSidebar.getCsvFileInput())
                .validate(openPricingSidebar.getCsvFileInput().getText(), "")
                .validate(not(visible), openPricingSidebar.getCsvFileClearIcon())
                .then("Update Pricing Rules button is visible")
                .validate(visible, openPricingSidebar.getUpdateExistingRulesButton())
                .uploadFileFromDialog(openPricingSidebar.getCsvFile(), RESOURCES_DIRECTORY + "/by too large int.csv")
                .validate(visible, openPricingSidebar.getCsvFileClearIcon())
                .and("Clear CSV input")
                .clickOnWebElement(openPricingSidebar.getCsvFileClearIcon())
                .validate(openPricingSidebar.getCsvFileInput().getText(), "")
                .testEnd();
    }

    @Test(description = "Download Template")
    public void downloadTemplate() {

        testStart()
                .then("Select Publisher")
                .clickOnWebElement(openPricingSidebar.getDownloadCSVTemplateIcon())
                .waitFileDownloading(TEMPLATE_FILE_NAME)
                .validateFileHeader(TEMPLATE_FILE_NAME, FILE_HEADER)
                .deleteFilesByName(TEMPLATE_FILE_NAME)
                .testEnd();
    }

    @Test(description = "Download Existing Open Pricing .csv")
    public void downloadExistingOpenPricing() {

        testStart()
                .and(String.format("Select Publisher %s", publisher.getName()))
                .selectFromDropdown(openPricingSidebar.getPublisherNameDropdown(),
                        openPricingSidebar.getPublisherNameDropdownItems(), publisher.getName())
                .clickOnWebElement(openPricingSidebar.getDownloadExistingOpenPricingRulesIcon())
                .waitFileDownloading(RULES_FILE_NAME)
                .validateFileHeader(RULES_FILE_NAME, FILE_HEADER)
                .testEnd();
        validateFileData(RULES_FILE_NAME);
    }

    @Test(description = "Negative: check errors if file is not selected")
    public void checkRequiredFields() {
        var errorsList = openPricingSidebar.getErrorAlert().getErrorsList();

        testStart()
                .and("Click 'Update Pricing Rules'")
                .clickOnWebElement(openPricingSidebar.getUpdateExistingRulesButton())
                .then("Validate errors for all required fields in Error Panel")
                .waitAndValidate(visible, openPricingSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(ErrorMessages.CSV_FILE_ERROR_ALERT.getText()))
                .then("Validate error under the 'Publisher' field")
                .waitAndValidate(not(visible), openPricingSidebar.getErrorAlertByFieldName("Publisher Name"))
                .validate(openPricingSidebar.getErrorAlertByFieldName("CSV"), ErrorMessages.CSV_FILE_ERROR_ALERT.getText())
                .uploadFileFromDialog(openPricingSidebar.getCsvFile(), RESOURCES_DIRECTORY + "/by too large int.csv")
                .then("Validate errors disappeared")
                .waitAndValidate(not(visible), openPricingSidebar.getErrorAlertByFieldName("CSV"))
                .waitAndValidate(not(visible), openPricingSidebar.getErrorAlertByFieldName("Publisher Name"))
                .validate(not(visible), openPricingSidebar.getErrorAlert().getErrorPanel())
                .testEnd();
    }

    @Step("Validate File Data")
    private void validateFileData(String filename) {

        getDataFromFile(filename);

        testStart()
                .validateMapsAreEqual(fileDataMap, expectedRules)
                .testEnd();
    }

    @AfterMethod
    private void logout() {

        testStart()
                .and("Close sidebar")
                .clickOnWebElement(openPricingSidebar.getCloseIcon())
                .waitSideBarClosed()
                .logOut()
                .testEnd();
    }

    @AfterClass(alwaysRun = true)
    private void deleteTestData() {

        testStart()
                .deleteFilesByName(RULES_FILE_NAME)
                .deleteFilesByName(TEMPLATE_FILE_NAME)
                .testEnd();
        deleteRules();
        deletePublisher(publisher.getId());
        deleteUser(singleUser.getId());
    }


    private void deletePublisher(int id) {

        if (publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(id)
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted publisher %s", publisher.getId()));
    }

    private void deleteUser(int id) {

        if (user()
                .setCredentials(USER_FOR_DELETION)
                .deleteUser(id)
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted user %s", publisher.getId()));
    }

    private void deleteRules() {

        for (OpenPricing rule : rules) {
            if (openPricing()
                    .setCredentials(USER_FOR_DELETION)
                    .deleteOpenPricing(rule.getId())
                    .build()
                    .getResponseCode() == HttpStatus.SC_NO_CONTENT)
                log.info(String.format("Deleted open pricing rule %s", rule.getId()));
        }
    }

    private OpenPricing createOpenPricingRule(Boolean isEnabled, Double floorPrice) {

        return openPricing()
                .createNewOpenPricing(captionWithSuffix("autoRule"), floorPrice, publisher, isEnabled)
                .build()
                .getOpenPricingResponse();
    }

    private void getDataFromFile(String fileName) {

        List<String[]> fileData = FileUtils.getAllDataFromCSVWithoutHeader(Configuration.downloadsFolder, fileName);

        fileData.stream().forEach(
                row -> { log.info(row[0]);
                    if (!row[0].isEmpty()) fileDataMap.put(row[0], convertFloorPrice(row[1]));
        });
    }

    @Step("Convert floor price value")
    private String convertFloorPrice(String floorPrice){

        return new DecimalFormat("0.##").format(Double.parseDouble(floorPrice));
    }
}
