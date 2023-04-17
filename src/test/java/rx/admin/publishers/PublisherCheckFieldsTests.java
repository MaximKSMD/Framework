package rx.admin.publishers;

import pages.Path;
import rx.BaseTest;
import io.qameta.allure.Issue;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import org.testng.annotations.Listeners;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import widgets.errormessages.ErrorMessages;
import pages.admin.publisher.PublishersPage;
import com.codeborne.selenide.testng.ScreenShooter;
import widgets.admin.publisher.sidebar.CurrencyType;
import widgets.admin.publisher.sidebar.EditPublisherSidebar;

import java.util.List;

import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;
import static com.codeborne.selenide.Condition.*;


@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Publishers")
public class PublisherCheckFieldsTests extends BaseTest {

    private PublishersPage publisherPage;
    private EditPublisherSidebar editPublisherSidebar;

    public PublisherCheckFieldsTests() {
        publisherPage = new PublishersPage();
        editPublisherSidebar = new EditPublisherSidebar();
    }


    @BeforeMethod
    public void login() {

        testStart()
                .given()
                .openDirectPath(Path.PUBLISHER)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, publisherPage.getNuxtProgress())
                .clickOnWebElement(publisherPage.getCreatePublisherButton())
                .waitSideBarOpened()
                .testEnd();
    }

    @Issue("GS-3186")
    @Test(description = "Check fields by default")
    public void checkDefaultFields() {

        testStart()
                .then("Validate fields by default")
                .validate(enabled, editPublisherSidebar.getActiveToggle())
                .validateAttribute(editPublisherSidebar.getActiveToggle(), "aria-checked", "true")
                .validate(enabled, editPublisherSidebar.getNameInput())
                .validate(editPublisherSidebar.getNameInput(), "")
                .validate(not(exist), editPublisherSidebar.getExternalIdInput())
                .validate(enabled, editPublisherSidebar.getAdOpsEmail())
                .validate(editPublisherSidebar.getAdOpsEmail(), "")
                .validate(enabled, editPublisherSidebar.getAdOpsPerson())
                .validate(editPublisherSidebar.getAdOpsPerson(), "")
                .validate(enabled, editPublisherSidebar.getDomainInput())
                .validate(editPublisherSidebar.getDomainInput(), "")
                .validate(enabled, editPublisherSidebar.getCategories())
                .testEnd();
    }

    @Issue("GS-3186")
    @Test(description = "Check required fields")
    public void checkRequiredFields() {
        var errorsList = editPublisherSidebar.getErrorAlert().getErrorsList();

        testStart()
                .then("Validate required fields")
                .and("Click 'Save' with all empty fields")
                .clickOnWebElement(editPublisherSidebar.getSaveButton())
                .then("Validate errors for all required fields in Error Panel (Publisher Name, Currency, Ad Ops Person, Domain, Ad Ops Email)")
                .waitAndValidate(visible, editPublisherSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 5)
                .validateList(errorsList, List.of(
                        ErrorMessages.NAME_ERROR_ALERT.getText(),
                        ErrorMessages.DOMAIN_ERROR_ALERT.getText(),
                        ErrorMessages.ADD_OPS_EMAIL_ERROR_ALERT.getText(),
                        ErrorMessages.ADD_OPS_PERSON_ERROR_ALERT.getText(),
                        ErrorMessages.CURRENCY_ERROR_ALERT.getText()))
                .setValue(editPublisherSidebar.getNameInput(), "randomAutoPub")
                .then("Validate error under the 'Publisher field' disappeared")
                .waitAndValidate(not(visible), editPublisherSidebar.getErrorAlertByFieldName("Publisher Name"))
                .and("Click 'Save'")
                .clickOnWebElement(editPublisherSidebar.getSaveButton())
                .then("Validate errors for 4 required fields in Error Panel (Currency, Ad Ops Person, Domain, Ad Ops Email)")
                .validateListSize(errorsList, 4)
                .validateList(errorsList, List.of(
                        ErrorMessages.DOMAIN_ERROR_ALERT.getText(),
                        ErrorMessages.ADD_OPS_EMAIL_ERROR_ALERT.getText(),
                        ErrorMessages.ADD_OPS_PERSON_ERROR_ALERT.getText(),
                        ErrorMessages.CURRENCY_ERROR_ALERT.getText()))
                .setValue(editPublisherSidebar.getAdOpsPerson(), "randomAutoPerson")
                .then("Validate error under the 'Ad Ops Person field' disappeared")
                .waitAndValidate(not(visible), editPublisherSidebar.getErrorAlertByFieldName("Ad Ops Person"))
                .and("Click 'Save'")
                .clickOnWebElement(editPublisherSidebar.getSaveButton())
                .then("Validate errors for 3 required fields in Error Panel (Currency, Ad Ops Emai, Domainl)")
                .validateListSize(errorsList, 3)
                .validateList(errorsList, List.of(
                        ErrorMessages.DOMAIN_ERROR_ALERT.getText(),
                        ErrorMessages.ADD_OPS_EMAIL_ERROR_ALERT.getText(),
                        ErrorMessages.CURRENCY_ERROR_ALERT.getText()))
                .setValue(editPublisherSidebar.getAdOpsEmail(), "randomAutoPub@gmail.com")
                .then("Validate error under the 'Ad Ops Email field' disappeared")
                .waitAndValidate(not(visible), editPublisherSidebar.getErrorAlertByFieldName("Ad Ops Email"))
                .and("Click 'Save'")
                .clickOnWebElement(editPublisherSidebar.getSaveButton())
                .then("Validate errors for 2 required fields in Error Panel (Currency, Domain)")
                .validateListSize(errorsList, 2)
                .validateList(errorsList, List.of(
                        ErrorMessages.DOMAIN_ERROR_ALERT.getText(),
                        ErrorMessages.CURRENCY_ERROR_ALERT.getText()))
                .setValue(editPublisherSidebar.getDomainInput(), "randomDomain")
                .then("Validate error under the 'Domain' field remains")
                .waitAndValidate(visible, editPublisherSidebar.getErrorAlertByFieldName("Domain"))
                .setValueWithClean(editPublisherSidebar.getDomainInput(), "https://gmail.com")
                .then("Validate error under the 'Domain' field disappeared")
                .waitAndValidate(not(visible), editPublisherSidebar.getErrorAlertByFieldName("Domain"))
                .and("Click 'Save'")
                .clickOnWebElement(editPublisherSidebar.getSaveButton())
                .then("Validate errors for 1 required fields in Error Panel (Domain)")
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.CURRENCY_ERROR_ALERT.getText()))
                .then("Select Currency 'USD'")
                .selectFromDropdown(editPublisherSidebar.getCurrencyDropdown(),
                        editPublisherSidebar.getCurrencyDropdownItems(), CurrencyType.USD.getType())
                .then("Validate error under the 'Currency' disappeared")
                .waitAndValidate(not(visible), editPublisherSidebar.getErrorAlertByFieldName("Currency"))
                .then("Select Currency 'EUR'")
                .selectFromDropdown(editPublisherSidebar.getCurrencyDropdown(),
                        editPublisherSidebar.getCurrencyDropdownItems(), CurrencyType.EUR.getType())
                .then("Validate error under the 'Currency' disappeared")
                .waitAndValidate(not(visible), editPublisherSidebar.getErrorAlertByFieldName("Currency"))
                .then("Select Currency 'JPY'")
                .selectFromDropdown(editPublisherSidebar.getCurrencyDropdown(),
                        editPublisherSidebar.getCurrencyDropdownItems(), CurrencyType.JPY.getType())
                .then("Validate error under the 'Currency' disappeared")
                .waitAndValidate(not(visible), editPublisherSidebar.getErrorAlertByFieldName("Currency"))
                .then("Select Currency 'RUB'")
                .selectFromDropdown(editPublisherSidebar.getCurrencyDropdown(),
                        editPublisherSidebar.getCurrencyDropdownItems(), CurrencyType.RUB.getType())
                .then("Validate error under the 'Currency' disappeared")
                .waitAndValidate(not(visible), editPublisherSidebar.getErrorAlertByFieldName("Currency"))
                .testEnd();
    }

    @Issue("GS-3186")
    @Test(description = "Check required fields")
    public void checkRequiredFieldsWithInvalidInputs() throws InterruptedException {
        var errorsList = editPublisherSidebar.getErrorAlert().getErrorsList();

        testStart()
                .then("Validate required fields")
                .and("Click 'Save' with all empty fields")
                .clickOnWebElement(editPublisherSidebar.getSaveButton())
                .then("Validate errors for all required fields in Error Panel (Publisher Name, Currency, Ad Ops Person, Ad Ops Email)")
                .waitAndValidate(visible, editPublisherSidebar.getErrorAlert().getErrorPanel())
                .validateListSize(errorsList, 5)
                .validateList(errorsList, List.of(
                        ErrorMessages.NAME_ERROR_ALERT.getText(),
                        ErrorMessages.DOMAIN_ERROR_ALERT.getText(),
                        ErrorMessages.ADD_OPS_EMAIL_ERROR_ALERT.getText(),
                        ErrorMessages.ADD_OPS_PERSON_ERROR_ALERT.getText(),
                        ErrorMessages.CURRENCY_ERROR_ALERT.getText()))
                .setValue(editPublisherSidebar.getNameInput(), "")
                .then("Validate error under the 'Publisher field' remains ")
                .waitAndValidate(visible, editPublisherSidebar.getErrorAlertByFieldName("Name"))
                .setValue(editPublisherSidebar.getNameInput(), "randomAutoPub")
                .then("Validate error under the 'Publisher field' disappeared ")
                .waitAndValidate(not(visible), editPublisherSidebar.getErrorAlertByFieldName("Name"))
                .and("Click 'Save'")
                .clickOnWebElement(editPublisherSidebar.getSaveButton())
                .then("Validate errors for 4 required fields in Error Panel (Currency, Ad Ops Person, Domain, Ad Ops Email)")
                .validateListSize(errorsList, 4)
                .validateList(errorsList, List.of(
                        ErrorMessages.DOMAIN_ERROR_ALERT.getText(),
                        ErrorMessages.ADD_OPS_EMAIL_ERROR_ALERT.getText(),
                        ErrorMessages.ADD_OPS_PERSON_ERROR_ALERT.getText(),
                        ErrorMessages.CURRENCY_ERROR_ALERT.getText()))
                .setValue(editPublisherSidebar.getAdOpsPerson(), "")
                .then("Validate error under the 'Ad Ops Person field' remains")
                .waitAndValidate(visible, editPublisherSidebar.getErrorAlertByFieldName("Ad Ops Person"))
                .setValue(editPublisherSidebar.getAdOpsPerson(), "randomAutoPerson")
                .then("Validate error under the 'Ad Ops Person field' disappeared")
                .waitAndValidate(not(visible), editPublisherSidebar.getErrorAlertByFieldName("Ad Ops Person"))
                .and("Click 'Save'")
                .clickOnWebElement(editPublisherSidebar.getSaveButton())
                .then("Validate errors for 3 required fields in Error Panel (Currency, Domain, Ad Ops Email)")
                .validateListSize(errorsList, 3)
                .validateList(errorsList, List.of(
                        ErrorMessages.DOMAIN_ERROR_ALERT.getText(),
                        ErrorMessages.ADD_OPS_EMAIL_ERROR_ALERT.getText(),
                        ErrorMessages.CURRENCY_ERROR_ALERT.getText()))
                .setValue(editPublisherSidebar.getAdOpsEmail(), "randomAutoPub")
                .then("Validate error under the 'Ad Ops Email field' remains")
                .waitAndValidate(visible, editPublisherSidebar.getErrorAlertByFieldName("Ad Ops Email"))
                .setValue(editPublisherSidebar.getAdOpsEmail(), "randomAutoPub@gmail.com")
                .then("Validate error under the 'Ad Ops Email field' disappeared")
                .waitAndValidate(not(visible), editPublisherSidebar.getErrorAlertByFieldName("Ad Ops Email"))
                .and("Click 'Save'")
                .clickOnWebElement(editPublisherSidebar.getSaveButton())
                .then("Validate errors for 2 required fields in Error Panel (Currency, Domain)")
                .validateListSize(errorsList, 2)
                .validateList(errorsList, List.of(
                        ErrorMessages.DOMAIN_ERROR_ALERT.getText(),
                        ErrorMessages.CURRENCY_ERROR_ALERT.getText()))
                .setValue(editPublisherSidebar.getDomainInput(), "randomDomain")
                .then("Validate error under the 'Domain' field remains")
                .waitAndValidate(visible, editPublisherSidebar.getErrorAlertByFieldName("Domain"))
                .setValueWithClean(editPublisherSidebar.getDomainInput(), "https://gmail.com")
                .then("Validate error under the 'Domain' field disappeared")
                .waitAndValidate(not(visible), editPublisherSidebar.getErrorAlertByFieldName("Domain"))
                .and("Click 'Save'")
                .clickOnWebElement(editPublisherSidebar.getSaveButton())
                .then("Validate errors for 1 required fields in Error Panel (Domain)")
                .validateListSize(errorsList, 1)
                .validateList(errorsList, List.of(
                        ErrorMessages.CURRENCY_ERROR_ALERT.getText()))
                .setValue(editPublisherSidebar.getCurrency(), "CAD")
                .then("Validate error under the 'Currency' remains")
                .waitAndValidate(visible, editPublisherSidebar.getErrorAlertByFieldName("Currency"))
                .then("Select Currency 'USD'")
                .clearField(editPublisherSidebar.getCurrency())
                .clickOnWebElement(editPublisherSidebar.getCurrencyDropdown())
                .selectFromDropdown(editPublisherSidebar.getCurrencyDropdown(),
                        editPublisherSidebar.getCurrencyDropdownItems(), CurrencyType.USD.getType())
                .then("Validate error under the 'Currency' disappeared")
                .waitAndValidate(not(visible), editPublisherSidebar.getErrorAlertByFieldName("Currency"))
                .then("Select Currency 'EUR'")
                .selectFromDropdown(editPublisherSidebar.getCurrencyDropdown(),
                        editPublisherSidebar.getCurrencyDropdownItems(), CurrencyType.EUR.getType())
                .then("Validate error under the 'Currency' disappeared")
                .waitAndValidate(not(visible), editPublisherSidebar.getErrorAlertByFieldName("Currency"))
                .then("Select Currency 'JPY'")
                .selectFromDropdown(editPublisherSidebar.getCurrencyDropdown(),
                        editPublisherSidebar.getCurrencyDropdownItems(), CurrencyType.JPY.getType())
                .then("Validate error under the 'Currency' disappeared")
                .waitAndValidate(not(visible), editPublisherSidebar.getErrorAlertByFieldName("Currency"))
                .then("Select Currency 'RUB'")
                .selectFromDropdown(editPublisherSidebar.getCurrencyDropdown(),
                        editPublisherSidebar.getCurrencyDropdownItems(), CurrencyType.RUB.getType())
                .then("Validate error under the 'Currency' disappeared")
                .waitAndValidate(not(visible), editPublisherSidebar.getErrorAlertByFieldName("Currency"))
                .testEnd();
    }

    @AfterMethod
    private void logout() {
        testStart()
                .and("Close Publisher Sidebar")
                .clickOnWebElement(editPublisherSidebar.getCloseIcon())
                .waitSideBarClosed()
                .and("Logout")
                .logOut()
                .testEnd();
    }
}
