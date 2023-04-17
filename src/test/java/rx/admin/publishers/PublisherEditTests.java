package rx.admin.publishers;

import io.qameta.allure.Feature;
import pages.Path;
import rx.BaseTest;
import org.testng.annotations.*;
import widgets.common.table.Statuses;
import widgets.common.table.ColumnNames;
import pages.admin.publisher.PublishersPage;
import api.dto.rx.admin.publisher.Publisher;
import widgets.common.categories.CategoriesList;
import com.codeborne.selenide.testng.ScreenShooter;
import widgets.admin.publisher.sidebar.EditPublisherSidebar;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import static configurations.User.TEST_USER;
import static java.lang.String.format;
import static managers.TestManager.testStart;
import static com.codeborne.selenide.Condition.*;
import static zutils.FakerUtils.captionWithSuffix;
import static api.preconditionbuilders.PublisherPrecondition.publisher;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Publishers")
public class PublisherEditTests extends BaseTest {
    private Publisher publisher;
    private PublishersPage publishersPage;
    private EditPublisherSidebar editPublisherSideBar;

    public PublisherEditTests() {
        publishersPage = new PublishersPage();
        editPublisherSideBar = new EditPublisherSidebar();
    }

    @BeforeMethod
    private void initAndLogin() {

        testStart()
                .given()
                .openDirectPath(Path.PUBLISHER)
                .clickBrowserRefreshButton()
                .logIn(TEST_USER)
                .waitAndValidate(disappear, publishersPage.getNuxtProgress())
                .clickBrowserRefreshButton()
                .testEnd();
    }

    @Test(description = "Edit Active Publisher without category", priority = 1)
    private void editActivePublisherWithoutCategory() {

        String publisherName = captionWithSuffix("autoPub");
        publisher = createPublisher(publisherName);

        editAndValidateEditedPublisher(publisherName, Statuses.ACTIVE, false);
    }

    @Test(description = "Edit Inactive Publisher with category", priority = 2)
    private void editInactivePublisherWithCategory() {

        String publisherName = captionWithSuffix("autoPublisher");
        publisher = createPublisher(publisherName);

        editAndValidateEditedPublisher(publisherName, Statuses.INACTIVE, true);
    }

    @Test(description = "Edit inactive Publisher without category", priority = 3)
    private void editInactivePublisherWithoutCategory() {

        String publisherName = captionWithSuffix("autoPublisher");
        publisher = createPublisher(publisherName);

        editAndValidateEditedPublisher(publisherName, Statuses.INACTIVE, false);
    }

    @Test(description = "Edit active Publisher with category", priority = 4)
    private void editActivePublisherWithCategory() {

        String publisherName = captionWithSuffix("autoPublisher");
        publisher = createPublisher(publisherName);

        editAndValidateEditedPublisher(publisherName, Statuses.ACTIVE, true);
    }

    @Test(description = "Check 'External ID' field")
    public void checkExternalIDField() {
        var tableData = publishersPage.getTable().getTableData();
        var tablePagination = publishersPage.getTable().getTablePagination();

        String publisherName = "Viber";
        String externalID = "";

        testStart()
                .and(format("Search publisher %s", publisherName))
                .setValueWithClean(tableData.getSearch(), publisherName)
                .pressEnterKey(tableData.getSearch())
                .then("Validate that text in table footer '1-1 of 1")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .and("Open Sidebar and check data")
                .waitAndValidate(disappear, publishersPage.getTableProgressBar())
                .clickOnTableCellLink(tableData, ColumnNames.NAME, publisherName)
                .waitSideBarOpened()
                .then("Check 'External ID' field")
                .validate(disabled, editPublisherSideBar.getExternalIdInput())
                .testEnd();

        externalID = editPublisherSideBar.getExternalIdInput().getAttribute("value");

        testStart()
                .clickOnWebElement(editPublisherSideBar.getCloseIcon())
                .waitSideBarClosed()
                .then("'External ID' cell in the table should be filled")
                .validate(tableData.getCellByRowValue(ColumnNames.EXTERNAL_ID, ColumnNames.NAME, publisherName), externalID)
                .testEnd();
    }

    @Step("Create Publisher via Api")
    private Publisher createPublisher(String name) {

        return publisher()
                .createNewPublisher(name)
                .build()
                .getPublisherResponse();
    }

    @Step("Edit Publisher")
    private void editAndValidateEditedPublisher(String pubName, Statuses status, boolean categorySelected) {

        var tableData = publishersPage.getTable().getTableData();
        var categories = editPublisherSideBar.getCategoriesPanel();
        var tableOptions = publishersPage.getTable().getShowHideColumns();
        var tablePagination = publishersPage.getTable().getTablePagination();

        String domainUrl = "https://gmail.com";
        String adOpsPerson = "randomAutoPerson";
        String AdOpsEmail = "randomAutoPub@gmail.com";

        testStart()
                .and(format("Search publisher %s", pubName))
                .setValueWithClean(tableData.getSearch(), pubName)
                .pressEnterKey(tableData.getSearch())
                .then("Validate that text in table footer '1-1 of 1")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .and("Open Sidebar and check data")
                .waitAndValidate(disappear, publishersPage.getTableProgressBar())
                .clickOnTableCellLink(tableData, ColumnNames.NAME, pubName)
                .waitSideBarOpened()
                .then("Check all fields");

        if (status.getStatus().equals(Statuses.INACTIVE.getStatus())) {
            testStart()
                    .turnToggleOff(editPublisherSideBar.getActiveToggle())
                    .testEnd();
        }

        testStart()
                .and("enter publisher name")
                .setValueWithClean(editPublisherSideBar.getNameInput(), pubName)
                .and("enter ad ops person")
                .setValueWithClean(editPublisherSideBar.getAdOpsPerson(), adOpsPerson)
                .and("enter ad ops email")
                .setValueWithClean(editPublisherSideBar.getAdOpsEmail(), AdOpsEmail)
                .and("enter domain url")
                .setValueWithClean(editPublisherSideBar.getDomainInput(), domainUrl)
                .and("select category")
                .testEnd();

        if (categorySelected) {
            testStart()
                    .clickOnWebElement(editPublisherSideBar.getCategoriesLabel())
                    .clickOnWebElement(categories.getCategoryCheckbox(CategoriesList.EDUCATION))
                    .testEnd();
        }

        testStart()
                .and("click save button")
                .clickOnWebElement(editPublisherSideBar.getSaveButton())
                .scrollIntoView(tableOptions.getShowHideColumnsBtn())
                .and("click show/hide columns button")
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .and("select unselected checkboxes")
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .and("close show/hide columns tab")
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .and("validate category is correct")
                .testEnd();

        if (categorySelected) {
            testStart()
                    .validate(tableData.getCellByRowValue(ColumnNames.CATEGORY, ColumnNames.NAME, pubName),
                            format("%s, %s, %s", CategoriesList.ART_ENTERTAIMENTS.getName(),
                                    CategoriesList.AUTOMOTIVE.getName(), CategoriesList.EDUCATION.getName()))
                    .testEnd();
        }

        testStart()
                .and("validate status is correct")
                .validate(tableData.getCellByRowValue(ColumnNames.STATUS, ColumnNames.NAME, pubName), status.getStatus())
                .and("validate domain is correct")
                .validate(tableData.getCellByRowValue(ColumnNames.DOMAIN, ColumnNames.NAME, pubName), domainUrl)
                .then("End Test")
                .testEnd();
    }

    @AfterMethod
    private void logout() {
        testStart()
                .logOut()
                .testEnd();
    }
}

