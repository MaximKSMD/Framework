package rx.admin.users;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.admin.user.UserDto;
import api.dto.rx.admin.user.UserRole;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.admin.users.UsersPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;
import widgets.admin.users.sidebar.EditUserSidebar;

import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static api.preconditionbuilders.UsersPrecondition.*;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
public class UsersEditTests extends BaseTest {

    private UsersPage usersPage;
    private UserDto user;
    private Publisher publisher;
    private EditUserSidebar editUserSidebar;


    public UsersEditTests() {
        usersPage = new UsersPage();
        editUserSidebar = new EditUserSidebar();
    }

    @BeforeClass
    public void initAndLogin() {
        testStart()
                .given()
                .openDirectPath(Path.USER)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, usersPage.getNuxtProgress())
                .testEnd();
    }

    @Test(description = "Create Single-publisher Active User and update to Cross-Publisher and deactivate user")
    public void editSinglePublisherUserToCross() {
        user = createSingleUser();
        editUserToCrossPublisher(user);
    }

    @Test(description = "Create Single-publisher Active User and update to Admin-Publisher and deactivate user")
    public void editSinglePublisherUserToAdmin() {
        user = createSingleUser();
        editUserToAdminPublisher(user);
    }

    @Test(description = "Create Cross-publisher Active User and update to Admin-Publisher and deactivate user")
    public void editCrossPublisherUserToAdmin() {
        user = createUser(UserRole.CROSS_PUBLISHER);
        editUserToAdminPublisher(user);
    }

    @Test(description = "Create Cross-publisher Active User and update to Single-Publisher and deactivate user")
    public void editCrossPublisherUserToSingle() {
        user = createUser(UserRole.CROSS_PUBLISHER);
        editUserToSinglePublish(user);
    }

    @Test(description = "Create Admin-publisher Active User and update to Cross-Publisher and deactivate user")
    public void editAdminPublisherUserToCross() {
        user = createUser(UserRole.ADMIN);
        editUserToCrossPublisher(user);
    }

    @Test(description = "Create Cross-publisher Active User and update to Single-Publisher and deactivate user")
    public void editAdminPublisherUserToSingle() {
        user = createUser(UserRole.ADMIN);
        editUserToSinglePublish(user);
    }

    @Step("Create Single User via Api")
    private UserDto createSingleUser() {
        publisher = createPublisher();

        return user()
                .createSinglePublisherUser(publisher.getId())
                .build()
                .getUserResponse();
    }
    private UserDto createUser(UserRole role) {

            return user()
                    .createNewUser(role)
                    .build()
                    .getUserResponse();
    }

    private Publisher createPublisher() {

        return publisher()
                .createNewPublisher(captionWithSuffix("00001-autoPub"))
                .build()
                .getPublisherResponse();
    }

    @Step("Edit User to single publisher user")
    private void editUserToSinglePublish(UserDto user) {

        var tableData = usersPage.getUsersTable().getTableData();
        var tablePagination = usersPage.getUsersTable().getTablePagination();
        var userNameUpdated = String.format("%s_%s", user.getName(), "updated");
        var emailUpdated = String.format("%s_%s", user.getName(), "updated@test.com");
        publisher = createPublisher();

        testStart()
                .clickBrowserRefreshButton()
                .and(String.format("Search user %s", user.getName()))
                .setValueWithClean(tableData.getSearch(), user.getName())
                .pressEnterKey(tableData.getSearch())
                .and("Open Sidebar and check data")
                .then("Validate that text in table footer '1-1 of 1")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, user.getName())
                .waitSideBarOpened()
                .then("Check all fields")
                .validateAttribute(editUserSidebar.getActiveToggle(), "aria-checked", "true")
                .validateAttribute(editUserSidebar.getUsernameInput(), "value", user.getName())
                .validate(editUserSidebar.getEmailInput().getText(), user.getMail())
                .clickOnWebElement(editUserSidebar.getSaveButton())
                .and("Error message is absent")
                .waitAndValidate(not(visible), editUserSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .clickBrowserRefreshButton()
                .setValueWithClean(tableData.getSearch(), user.getName())
                .pressEnterKey(tableData.getSearch())
                .and("Open Sidebar and check data")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, user.getName())
                .waitSideBarOpened()
                .then("Edit all fields")
                .selectRadioButton(editUserSidebar.getSinglePublisherRadioButton())
                .setValueWithClean(editUserSidebar.getUsernameInput(), userNameUpdated)
                .setValueWithClean(editUserSidebar.getEmailInput(), emailUpdated)
                .selectFromDropdown(editUserSidebar.getPublisherNameInput(),
                        editUserSidebar.getPublisherDropdownItems(), publisher.getName())
                .turnToggleOff(editUserSidebar.getActiveToggle())
                .clickOnWebElement(editUserSidebar.getSaveButton())
                .and("Error message is absent")
                .waitAndValidate(not(visible), editUserSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .and("Toaster Error message is absent")
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .and("Search updated user")
                .setValueWithClean(tableData.getSearch(), userNameUpdated)
                .pressEnterKey(tableData.getSearch())
                .and("Open Sidebar and check data")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, userNameUpdated)
                .waitSideBarOpened()
                .then("Check all fields")
                .validateAttribute(editUserSidebar.getActiveToggle(), "aria-checked", "false")
                .validateAttribute(editUserSidebar.getUsernameInput(), "value", userNameUpdated)
                .and("Click Save")
                .clickOnWebElement(editUserSidebar.getSaveButton())
                .waitAndValidate(not(visible), editUserSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .testEnd();
    }

    @Step("Edit User to cross-publisher user")
    private void editUserToCrossPublisher(UserDto user) {

        var tableData = usersPage.getUsersTable().getTableData();
        var tablePagination = usersPage.getUsersTable().getTablePagination();
        var userNameUpdated = String.format("%s_%s", user.getName(), "updated");
        var emailUpdated = String.format("%s_%s", user.getName(), "updated@test.com");

        testStart()
                .clickBrowserRefreshButton()
                .and(String.format("Search user %s", user.getName()))
                .setValueWithClean(tableData.getSearch(), user.getName())
                .pressEnterKey(tableData.getSearch())
                .and("Open Sidebar and check data")
                .then("Validate that text in table footer '1-1 of 1")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, user.getName())
                .waitSideBarOpened()
                .then("Check all fields")
                .validateAttribute(editUserSidebar.getActiveToggle(), "aria-checked", "true")
                .validateAttribute(editUserSidebar.getUsernameInput(), "value", user.getName())
                .validate(editUserSidebar.getEmailInput().getText(), user.getMail())
                .clickOnWebElement(editUserSidebar.getSaveButton())
                .and("Error message is absent")
                .waitAndValidate(not(visible), editUserSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .clickBrowserRefreshButton()
                .setValueWithClean(tableData.getSearch(), user.getName())
                .pressEnterKey(tableData.getSearch())
                .and("Open Sidebar and check data")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, user.getName())
                .waitSideBarOpened()
                .and("Edit all fields")
                .turnToggleOff(editUserSidebar.getActiveToggle())
                .setValueWithClean(editUserSidebar.getUsernameInput(), userNameUpdated)
                .setValueWithClean(editUserSidebar.getEmailInput(), emailUpdated)
                .selectRadioButton(editUserSidebar.getCrossPublisherRadioButton())
                .selectRadioButton(editUserSidebar.getCrossPublisherRadioButton())
                .clickOnWebElement(editUserSidebar.getSaveButton())
                .and("Error message is absent")
                .waitAndValidate(not(visible), editUserSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .and("Toaster Error message is absent")
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .and("Search updated user")
                .setValueWithClean(tableData.getSearch(), userNameUpdated)
                .pressEnterKey(tableData.getSearch())
                .and("Open Sidebar and check data")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, userNameUpdated)
                .waitSideBarOpened()
                .then("Check all fields")
                .validateAttribute(editUserSidebar.getActiveToggle(), "aria-checked", "false")
                .validateAttribute(editUserSidebar.getUsernameInput(), "value", userNameUpdated)
                .and("Click Save")
                .clickOnWebElement(editUserSidebar.getSaveButton())
                .waitAndValidate(not(visible), editUserSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .clickOnWebElement(tableData.getClear())
                .then("Validate that text in table footer '1-20 of X")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-20 of ")
                .testEnd();
    }

    @Step("Edit User")
    private void editUserToAdminPublisher(UserDto user) {

        var tableData = usersPage.getUsersTable().getTableData();
        var tablePagination = usersPage.getUsersTable().getTablePagination();
        var userNameUpdated = String.format("%s_%s", user.getName(), "updated");
        var emailUpdated = String.format("%s_%s", user.getName(), "updated@test.com");

        testStart()
                .clickBrowserRefreshButton()
                .and(String.format("Search user %s", user.getName()))
                .setValueWithClean(tableData.getSearch(), user.getName())
                .pressEnterKey(tableData.getSearch())
                .and("Open Sidebar and check data")
                .then("Validate that text in table footer '1-1 of 1")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, user.getName())
                .waitSideBarOpened()
                .then("Check all fields")
                .validateAttribute(editUserSidebar.getActiveToggle(), "aria-checked", "true")
                .validateAttribute(editUserSidebar.getUsernameInput(), "value", user.getName())
                .validate(editUserSidebar.getEmailInput().getText(), user.getMail())
                .clickOnWebElement(editUserSidebar.getSaveButton())
                .and("Error message is absent")
                .waitAndValidate(not(visible), editUserSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .clickBrowserRefreshButton()
                .setValueWithClean(tableData.getSearch(), user.getName())
                .pressEnterKey(tableData.getSearch())
                .and("Open Sidebar and check data")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, user.getName())
                .waitSideBarOpened()
                .and("Edit all fields")
                .turnToggleOff(editUserSidebar.getActiveToggle())
                .setValueWithClean(editUserSidebar.getUsernameInput(), userNameUpdated)
                .setValueWithClean(editUserSidebar.getEmailInput(), emailUpdated)
                .selectRadioButton(editUserSidebar.getCrossPublisherRadioButton())
                .selectRadioButton(editUserSidebar.getAdminRadioButton())
                .clickOnWebElement(editUserSidebar.getSaveButton())
                .and("Error message is absent")
                .waitAndValidate(not(visible), editUserSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .and("Toaster Error message is absent")
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .and("Search new media")
                .setValueWithClean(tableData.getSearch(), userNameUpdated)
                .pressEnterKey(tableData.getSearch())
                .and("Open Sidebar and check data")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, userNameUpdated)
                .waitSideBarOpened()
                .then("Check all fields")
                .validateAttribute(editUserSidebar.getActiveToggle(), "aria-checked", "false")
                .validateAttribute(editUserSidebar.getUsernameInput(), "value", userNameUpdated)
                .and("Click Save")
                .clickOnWebElement(editUserSidebar.getSaveButton())
                .waitAndValidate(not(visible), editUserSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), usersPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .clickOnWebElement(tableData.getClear())
                .then("Validate that text in table footer '1-20 of X")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-20 of ")
                .testEnd();
    }

    @AfterTest(alwaysRun = true)
    private void deleteEntities() {
        deleteUser(user.getId());
        if(publisher != null)
           deletePublisher(publisher.getId());
    }

    private void deleteUser(Integer id) {
        user()
                .setCredentials(USER_FOR_DELETION)
                .deleteUser(id)
                .build();
    }
    private void deletePublisher(int id) {
        publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(id)
                .build();
    }

    @AfterClass
    public void logout() {
        testStart()
                .logOut()
                .testEnd();
    }
}

