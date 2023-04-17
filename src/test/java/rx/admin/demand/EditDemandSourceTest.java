package rx.admin.demand;

import api.dto.rx.demandsource.DemandSource;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Link;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.admin.demand.DemandPage;
import rx.BaseTest;
import widgets.admin.demand.sidebar.EditDemandSidebar;
import widgets.common.table.ColumnNames;

import static api.preconditionbuilders.DemandSourcePrecondition.demandSource;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static java.lang.String.valueOf;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
public class EditDemandSourceTest extends BaseTest {

    private DemandPage demandPage;
    private DemandSource demandSource;
    private EditDemandSidebar editDemandSidebar;

    public EditDemandSourceTest() {
        demandPage = new DemandPage();
        editDemandSidebar = new EditDemandSidebar();
    }

    @BeforeClass
    public void createNewPublisher() {
        //Creating new demand source to edit Using API
        demandSource = createNewDsp();
        login();
    }

    @Step("Login to system")
    private void login(){
        testStart()
                .given("Login to SSP")
                .openDirectPath(Path.DEMAND)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, demandPage.getNuxtProgress())
                .waitAndValidate(disappear, demandPage.getTableProgressBar())
                .testEnd();
    }

    @Test
    public void checkDspSettingsTest() {
        testStart()
                .given("Searching Demand source")
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .then("Validate All Settings of Demand Source sidebar")
                .validateAttribute(editDemandSidebar.getInactiveRadioButton(), "aria-checked", "true")
                .validateAttribute(editDemandSidebar.getActiveRadioButton(), "aria-checked", "false")
                .validateAttribute(editDemandSidebar.getOnboardingRadioButton(), "aria-checked", "false")
                .then("Check inputs value")
                .validateAttribute(editDemandSidebar.getBidderInput(), "value", demandSource.getName())
                .validateAttribute(editDemandSidebar.getBidderInput(), "disabled", "true")
                .validateAttribute(editDemandSidebar.getRequestAdjustmentRateField().getRequestAdjustmentRateFieldInput(),
                        "value", valueOf(demandSource.getRequestAdjustmentRate()))
                .then("Check sidebar toggles")
                .validateAttribute(editDemandSidebar.getSyncRequiredToggle(), "aria-checked", "false")
                .validateAttribute(editDemandSidebar.getIdfaRequiredToggle(), "aria-checked", "false")
                .validateAttribute(editDemandSidebar.getTokenGenerationToggle(), "aria-checked", "false")
                .validateAttribute(editDemandSidebar.getPmpSupportToggle(), "aria-checked", "true")
                .validateAttribute(editDemandSidebar.getNonProgrammaticToggle(), "aria-checked", "false")
                .then("Check values of checkBoxes")
                .scrollIntoView(editDemandSidebar.getFormatLabel())
                .validateAttribute(editDemandSidebar.getCheckboxLabelByName("Banner"), "aria-checked", "true")
                .validateAttribute(editDemandSidebar.getCheckboxLabelByName("Native"), "aria-checked", "true")
                .validateAttribute(editDemandSidebar.getCheckboxLabelByName("Video"), "aria-checked", "false")
                .validateAttribute(editDemandSidebar.getCheckboxLabelByName("IOS"), "aria-checked", "true")
                .validateAttribute(editDemandSidebar.getCheckboxLabelByName("Android"), "aria-checked", "true")
                .validateAttribute(editDemandSidebar.getCheckboxLabelByName("Mobile Web"), "aria-checked", "true")
                .validateAttribute(editDemandSidebar.getCheckboxLabelByName("PC Web"), "aria-checked", "true")
                .validateAttribute(editDemandSidebar.getCheckboxLabelByName("CTV"), "aria-checked", "false")
                .then("Close sidebar")
                .clickOnWebElement(editDemandSidebar.getCloseIcon())
                .testEnd();
    }

    @Test
    public void editDspStatusOnboardingTest() {

        testStart()
                .given("Searching Demand source")
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .then("Update Status field to Onboarding of Demand Source sidebar")
                .selectRadioButton(editDemandSidebar.getOnboardingRadioButton())
                .scrollIntoView(editDemandSidebar.getSaveButton())
                .clickOnWebElement(editDemandSidebar.getSaveButton())
                .then("Edit bar should be closed")
                .waitAndValidate(not(visible), editDemandSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), demandPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .when()
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .validateAttribute(editDemandSidebar.getOnboardingRadioButton(), "aria-checked", "true")
                .clickOnWebElement(editDemandSidebar.getCloseIcon())
                .testEnd();
    }

    @Test
    @Link("https://rakutenadvertising.atlassian.net/browse/GS-3282")
    public void editDspAdjustmentRateTest() {

        testStart()
                .given("Searching Demand source")
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .then("Update Request Adjustment Rate field of Demand Source sidebar")
                .scrollIntoView(editDemandSidebar.getRequestAdjustmentRateField().getRequestAdjustmentRateFieldInput())
                .clickOnWebElement(editDemandSidebar.getRequestAdjustmentRateField().getRequestAdjustmentRateFieldInput())
                .setValueWithClean(editDemandSidebar.getRequestAdjustmentRateField().getRequestAdjustmentRateFieldInput(), "90")
                .scrollIntoView(editDemandSidebar.getSaveButton())
                .clickOnWebElement(editDemandSidebar.getSaveButton())
                .then("Edit bar should be closed")
                .waitAndValidate(not(visible), editDemandSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), demandPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .when()
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .validateAttribute(editDemandSidebar.getRequestAdjustmentRateField().getRequestAdjustmentRateFieldInput(), "value", "90")
                .clickOnWebElement(editDemandSidebar.getCloseIcon())
                .testEnd();
    }

    @Test
    public void editDspSyncRequiredTrueTest() {

        testStart()
                .given("Searching Demand source")
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .then("Update Sync Required True field of Demand Source sidebar")
                .turnToggleOn(editDemandSidebar.getSyncRequiredToggle())
                .scrollIntoView(editDemandSidebar.getSaveButton())
                .clickOnWebElement(editDemandSidebar.getSaveButton())
                .then("Edit bar should be closed")
                .waitAndValidate(not(visible), editDemandSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), demandPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .when()
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .validateAttribute(editDemandSidebar.getSyncRequiredToggle(), "aria-checked", "true")
                .clickOnWebElement(editDemandSidebar.getCloseIcon())
                .testEnd();
    }

    @Test
    public void editDspIDFARequiredTrueTest() {

        testStart()
                .given("Searching Demand source")
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .then("Update Sync Required True field of Demand Source sidebar")
                .turnToggleOn(editDemandSidebar.getIdfaRequiredToggle())
                .scrollIntoView(editDemandSidebar.getSaveButton())
                .clickOnWebElement(editDemandSidebar.getSaveButton())
                .then("Edit bar should be closed")
                .waitAndValidate(not(visible), editDemandSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), demandPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .when()
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .validateAttribute(editDemandSidebar.getIdfaRequiredToggle(), "aria-checked", "true")
                .clickOnWebElement(editDemandSidebar.getCloseIcon())
                .testEnd();
    }

    @Test
    public void editDspTokenGenerationTrueTest() {

        testStart()
                .given("Searching Demand source")
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .then("Update Sync Required True field of Demand Source sidebar")
                .turnToggleOn(editDemandSidebar.getTokenGenerationToggle())
                .scrollIntoView(editDemandSidebar.getSaveButton())
                .clickOnWebElement(editDemandSidebar.getSaveButton())
                .then("Edit bar should be closed")
                .waitAndValidate(not(visible), editDemandSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), demandPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .when()
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .validateAttribute(editDemandSidebar.getTokenGenerationToggle(), "aria-checked", "true")
                .clickOnWebElement(editDemandSidebar.getCloseIcon())
                .testEnd();
    }

    @Test
    public void editDspPMPRequiredTrueTest() {

        testStart()
                .given("Searching Demand source")
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .then("Update Sync Required True field of Demand Source sidebar")
                .turnToggleOn(editDemandSidebar.getPmpSupportToggle())
                .scrollIntoView(editDemandSidebar.getSaveButton())
                .clickOnWebElement(editDemandSidebar.getSaveButton())
                .then("Edit bar should be closed")
                .waitAndValidate(not(visible), editDemandSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), demandPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .when()
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .validateAttribute(editDemandSidebar.getPmpSupportToggle(), "aria-checked", "true")
                .clickOnWebElement(editDemandSidebar.getCloseIcon())
                .testEnd();
    }

    @Test
    public void editDspFormatAllTest() {

        testStart()
                .given("Searching Demand source")
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .then("Update Sync Required True field of Demand Source sidebar")
                .selectCheckBox(editDemandSidebar.getCheckboxByName("Banner"))
                .selectCheckBox(editDemandSidebar.getCheckboxByName("Native"))
                .selectCheckBox(editDemandSidebar.getCheckboxByName("Video"))
                .scrollIntoView(editDemandSidebar.getSaveButton())
                .clickOnWebElement(editDemandSidebar.getSaveButton())
                .then("Edit bar should be closed")
                .waitAndValidate(not(visible), editDemandSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), demandPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .when()
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .validateAttribute(editDemandSidebar.getCheckboxLabelByName("Banner"), "aria-checked", "true")
                .validateAttribute(editDemandSidebar.getCheckboxLabelByName("Native"), "aria-checked", "true")
                .validateAttribute(editDemandSidebar.getCheckboxLabelByName("Video"), "aria-checked", "true")
                .clickOnWebElement(editDemandSidebar.getCloseIcon())
                .testEnd();
    }

    @Test
    public void editDspPlatformAllTest() {

        testStart()
                .given("Searching Demand source")
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .then("Update Sync Required True field of Demand Source sidebar")
                .selectCheckBox(editDemandSidebar.getCheckboxByName("IOS"))
                .selectCheckBox(editDemandSidebar.getCheckboxByName("Android"))
                .selectCheckBox(editDemandSidebar.getCheckboxByName("Mobile Web"))
                .selectCheckBox(editDemandSidebar.getCheckboxByName("PC Web"))
                .selectCheckBox(editDemandSidebar.getCheckboxByName("CTV"))
                .scrollIntoView(editDemandSidebar.getSaveButton())
                .clickOnWebElement(editDemandSidebar.getSaveButton())
                .then("Edit bar should be closed")
                .waitAndValidate(not(visible), editDemandSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), demandPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .when()
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .validateAttribute(editDemandSidebar.getCheckboxLabelByName("IOS"), "aria-checked", "true")
                .validateAttribute(editDemandSidebar.getCheckboxLabelByName("Android"), "aria-checked", "true")
                .validateAttribute(editDemandSidebar.getCheckboxLabelByName("Mobile Web"), "aria-checked", "true")
                .validateAttribute(editDemandSidebar.getCheckboxLabelByName("PC Web"), "aria-checked", "true")
                .validateAttribute(editDemandSidebar.getCheckboxLabelByName("CTV"), "aria-checked", "true")
                .clickOnWebElement(editDemandSidebar.getCloseIcon())
                .testEnd();
    }

    @Test
    public void editDspDataCenterTest() {

        testStart()
                .given("Searching Demand source")
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .then("Update Sync Required True field of Demand Source sidebar")
                .selectFromDropdown(editDemandSidebar.getDataCenterDropdown(), editDemandSidebar.getPublisherNameDropdownItems(), "gcp asia-northeast1")
                .clickOnWebElement(editDemandSidebar.getSaveButton())
                .then("Edit bar should be closed")
                .waitAndValidate(not(visible), editDemandSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), demandPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .when()
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .validateAttribute(editDemandSidebar.getDataCenterDropdown(), "value", "gcp asia-northeast1")
                .clickOnWebElement(editDemandSidebar.getCloseIcon())
                .testEnd();
    }

    @Test
    public void editDspSyncUrlTest() {

        testStart()
                .given("Searching Demand source")
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .then("Update Request Adjustment Rate field of Demand Source sidebar")
                .setValueWithClean(editDemandSidebar.getSyncUrlInput(), "http://syncedit.com")
                .scrollIntoView(editDemandSidebar.getSaveButton())
                .clickOnWebElement(editDemandSidebar.getSaveButton())
                .then("Edit bar should be closed")
                .waitAndValidate(not(visible), editDemandSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), demandPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .when()
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .validateAttribute(editDemandSidebar.getSyncUrlInput(), "value", "http://syncedit.com")
                .clickOnWebElement(editDemandSidebar.getCloseIcon())
                .testEnd();
    }

    @Test
    public void editDspTimeoutTest() {

        testStart()
                .given("Searching Demand source")
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .then("Update Request Adjustment Rate field of Demand Source sidebar")
                .scrollIntoView(editDemandSidebar.getTimeoutMsField().getTimeoutMsFieldInput())
                .clickOnWebElement(editDemandSidebar.getTimeoutMsField().getTimeoutMsFieldInput())
                .setValueWithClean(editDemandSidebar.getTimeoutMsField().getTimeoutMsFieldInput(), "100")
                .scrollIntoView(editDemandSidebar.getSaveButton())
                .clickOnWebElement(editDemandSidebar.getSaveButton())
                .then("Edit bar should be closed")
                .waitAndValidate(not(visible), editDemandSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), demandPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .when()
                .setValueWithClean(demandPage.getDemandTable().getTableData().getSearch(),
                        demandSource.getName())
                .pressEnterKey(demandPage.getDemandTable().getTableData().getSearch())
                .clickOnTableCellLink(demandPage.getDemandTable().getTableData(), ColumnNames.NAME, demandSource.getName())
                .then("Wait till Demand Source sidebar will be opened")
                .waitSideBarOpened()
                .validateAttribute(editDemandSidebar.getTimeoutMsField().getTimeoutMsFieldInput(), "value", "100")
                .clickOnWebElement(editDemandSidebar.getCloseIcon())
                .testEnd();
    }

    @AfterClass()
    private void logOutAndDeleteData(){
        deleteDsp(demandSource.getId());
        logout();
    }

    @Step("Delete Demand Source")
    private void logout(){
        testStart()
                .logOut()
                .testEnd();
    }

    @Step("Create New Demand Source")
    private DemandSource createNewDsp(){

        return demandSource()
                .createDemandSource()
                .build()
                .getDemandSourceResponse();
    }

    @Step("Delete Demand Source")
    private void deleteDsp(int id){
        demandSource()
                .setCredentials(USER_FOR_DELETION)
                .deleteDemandSource(id)
                .build();
    }
}
