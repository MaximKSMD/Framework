package rx.admin.users;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.admin.user.UserRole;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.admin.users.UsersPage;
import rx.BaseTest;
import widgets.admin.users.sidebar.CreateUserSidebar;
import widgets.common.table.ColumnNames;
import widgets.common.table.Statuses;

import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;
import static zutils.FakerUtils.randomMail;

@Slf4j
@Listeners({ScreenShooter.class})
public class UsersCreationTests extends BaseTest {
    private UsersPage usersPage;
    private Publisher publisher;
    private CreateUserSidebar createUserSidebar;

    public UsersCreationTests() {

    	usersPage = new UsersPage();
        createUserSidebar = new CreateUserSidebar();
    }

    @BeforeClass
    private void initAndLogin() {
        testStart()
                .given()
                .openDirectPath(Path.USER)
                .clickBrowserRefreshButton()
                .logIn(TEST_USER)
                .waitAndValidate(disappear, usersPage.getNuxtProgress())
                .clickBrowserRefreshButton()
                .testEnd();
    }

    @Test(description = "Create Single-Publisher active User")
    public void createActiveSingleUser() {
        var userName = captionWithSuffix("autoUserSingle");
        var emailAddress = randomMail();
        var status = Statuses.ACTIVE.name();

        createSingleUser(userName, emailAddress, status);
        validateSingleUser(userName, emailAddress, status);
    }

    @Test(description = "Create Cross-Publisher active User")
    public void createActiveCrossUser() {
        var userName = captionWithSuffix("autoUserCross");
        var emailAddress = randomMail();
        var status = Statuses.ACTIVE.name();

        createCrossUser(userName, emailAddress, status);
        validateNonSingleUser(userName, emailAddress, status, "Cross-Publishers");
    }

    @Test(description = "Create Admin-Publisher active User")
    public void createActiveAdminUser() {
        var userName = captionWithSuffix("autoUserAdmin");
        var emailAddress = randomMail();
        var status = Statuses.ACTIVE.name();

        createAdminUser(userName, emailAddress, status);
        validateNonSingleUser(userName, emailAddress, status, "Admin");
    }

    @Test(description = "Create Single-Publisher inactive User")
    public void createInactiveSingleUser() {
        var userName = captionWithSuffix("autoUserSingleInactive");
        var emailAddress = randomMail();
        var status = Statuses.INACTIVE.name();

        createSingleUser(userName, emailAddress, status);
        validateSingleUser(userName, emailAddress, status);
    }

    @Test(description = "Create Cross-Publisher active User")
    public void createInactiveCrossUser() {
        var userName = captionWithSuffix("autoUserCrossInactive");
        var emailAddress = randomMail();
        var status = Statuses.INACTIVE.name();
        createCrossUser(userName, emailAddress, status);
        validateNonSingleUser(userName, emailAddress, status, "Cross-Publishers");
    }

    @Test(description = "Create Admin-Publisher active User")
    public void createInactiveAdminUser() {
        var userName = captionWithSuffix("autoUserAdminInactive");
        var emailAddress = randomMail();
        var status = Statuses.INACTIVE.name();
        createAdminUser(userName, emailAddress, status);
        validateNonSingleUser(userName, emailAddress, status, "Admin");
    }

    private void createSingleUser(String userName, String emailAddress, String status) {
        publisher = createPublisher();
        testStart()
                .clickBrowserRefreshButton()
                .clickOnWebElement(usersPage.getCreateUserButton())
                .waitSideBarOpened()
                .setValueWithClean(createUserSidebar.getUsernameInput(), userName)
                .setValueWithClean(createUserSidebar.getEmailInput(), emailAddress)
                .setValueWithClean(createUserSidebar.getPasswordInput(), "Password1")
                .selectFromDropdown(createUserSidebar.getPublisherNameInput(),
                        createUserSidebar.getPublisherDropdownItems(), publisher.getName())
                .and("Fill Name")
                .testEnd();

        if(status.equals(Statuses.INACTIVE.name()))
        {
            testStart()
                    .turnToggleOff(createUserSidebar.getActiveToggle())
                    .testEnd();

        }
        testStart()
                .clickOnWebElement(createUserSidebar.getSaveButton())
                .and("Error message is absent")
                .waitAndValidate(not(visible), createUserSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .and("Toaster Error message is absent")
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .testEnd();
    }

    private void createCrossUser(String userName, String emailAddress, String status) {
        publisher = createPublisher();
        testStart()
                .clickOnWebElement(usersPage.getCreateUserButton())
                .waitSideBarOpened()
                .setValueWithClean(createUserSidebar.getUsernameInput(), userName)
                .setValueWithClean(createUserSidebar.getEmailInput(), emailAddress)
                .setValueWithClean(createUserSidebar.getPasswordInput(), "Password1")
                .selectRadioButton(createUserSidebar.getCrossPublisherRadioButton())
                .testEnd();

        if(status.equals(Statuses.INACTIVE.name()))
        {
            testStart()
                    .turnToggleOff(createUserSidebar.getActiveToggle())
                    .testEnd();

        }
        testStart()
                .clickOnWebElement(createUserSidebar.getSaveButton())
                .and("Error message is absent")
                .waitAndValidate(not(visible), createUserSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .and("Toaster Error message is absent")
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .testEnd();
    }

    private void createAdminUser(String userName, String emailAddress, String status) {
        publisher = createPublisher();
        testStart()
                .clickOnWebElement(usersPage.getCreateUserButton())
                .waitSideBarOpened()
                .setValueWithClean(createUserSidebar.getUsernameInput(), userName)
                .setValueWithClean(createUserSidebar.getEmailInput(), emailAddress)
                .setValueWithClean(createUserSidebar.getPasswordInput(), "Password1")
                .selectRadioButton(createUserSidebar.getAdminRadioButton())
                .testEnd();
        if(status.equals(Statuses.INACTIVE.name()))
        {
            testStart()
                    .turnToggleOff(createUserSidebar.getActiveToggle())
                    .testEnd();

        }
        testStart()
                .clickOnWebElement(createUserSidebar.getSaveButton())
                .and("Error message is absent")
                .waitAndValidate(not(visible), createUserSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .and("Toaster Error message is absent")
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .testEnd();
    }

    @Step("Create User {0} with user type {1} and status {3}")
    private void validateSingleUser(String userName, String emailAddress, String status) {
        var tableData = usersPage.getUsersTable().getTableData();
        var tableOptions = usersPage.getUsersTable().getShowHideColumns();
        var tablePagination = usersPage.getUsersTable().getTablePagination();

        testStart()
                .when("Search new user")
                .setValueWithClean(tableData.getSearch(), userName)
                .pressEnterKey(tableData.getSearch())
                .then("Validate that text in table footer '1-1 of 1")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .and("Open Sidebar and check data")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, userName)
                .waitSideBarOpened()
                .then("Check all fields")
                .validateAttribute(createUserSidebar.getUsernameInput(), "value", userName)
                .validateAttribute(createUserSidebar.getEmailInput(), "value", emailAddress)
                .validate(createUserSidebar.getPublisherNameInput(), publisher.getName())
                .and("Click Save")
                .clickOnWebElement(createUserSidebar.getSaveButton())
                .waitAndValidate(not(visible), createUserSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .and("Toaster Error message is absent")
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .and("Show column 'Created Date'")
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .then("Validate data in table")
                .validate(tableData.getCellByRowValue(ColumnNames.STATUS, ColumnNames.NAME, userName), status)
                .validate(tableData.getCellByRowValue(ColumnNames.PUBLISHER, ColumnNames.NAME, userName), publisher.getName())
                .validate(tableData.getCellByRowValue(ColumnNames.EMAIL, ColumnNames.NAME, userName), emailAddress)
                .validate(tableData.getCellByRowValue(ColumnNames.ROLE, ColumnNames.NAME, userName), UserRole.SINGLE_PUBLISHER.getDefinition())
                .clickOnWebElement(tableData.getClear())
                .then("Validate that text in table footer '1-20 of X")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-20 of ")
                .testEnd();
    }

    @Step("Create User {0} with user type {1} and status {3}")
    private void validateNonSingleUser(String userName, String emailAddress, String status, String role) {
        var tableData = usersPage.getUsersTable().getTableData();
        var tableOptions = usersPage.getUsersTable().getShowHideColumns();
        var tablePagination = usersPage.getUsersTable().getTablePagination();

        testStart()
                .when("Search new user")
                .setValueWithClean(tableData.getSearch(), userName)
                .pressEnterKey(tableData.getSearch())
                .then("Validate that text in table footer '1-1 of 1")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .and("Open Sidebar and check data")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, userName)
                .waitSideBarOpened()
                .then("Check all fields")
                .validateAttribute(createUserSidebar.getUsernameInput(), "value", userName)
                .validateAttribute(createUserSidebar.getEmailInput(), "value", emailAddress)
                .and("Click Save")
                .clickOnWebElement(createUserSidebar.getSaveButton())
                .waitAndValidate(not(visible), createUserSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .and("Toaster Error message is absent")
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .and("Show column 'Created Date'")
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .then("Validate data in table")
                .validate(tableData.getCellByRowValue(ColumnNames.STATUS, ColumnNames.NAME, userName), status)
                .validate(tableData.getCellByRowValue(ColumnNames.EMAIL, ColumnNames.NAME, userName), emailAddress)
                .validate(tableData.getCellByRowValue(ColumnNames.ROLE, ColumnNames.NAME, userName), role)
                .clickOnWebElement(tableData.getClear())
                .then("Validate that text in table footer '1-20 of X")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-20 of ")
                .testEnd();
    }

    private Publisher createPublisher() {

        return publisher()
                .createNewPublisher(captionWithSuffix("00003-autoPub"))
                .build()
                .getPublisherResponse();
    }

    @AfterTest(alwaysRun = true)
    private void deleteEntities() {
        if(publisher != null)
            deletePublisher(publisher.getId());
    }

    private void deletePublisher(int id) {
        publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(id)
                .build();
    }

    @AfterClass(alwaysRun = true)
    public void logout(){
        testStart()
                .and("Logout")
                .logOut()
                .testEnd();
    }

}
