package rx.admin.publishers;

import pages.Path;
import rx.BaseTest;
import widgets.common.table.ColumnNames;
import pages.admin.publisher.PublishersPage;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import org.testng.annotations.Listeners;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;
import static com.codeborne.selenide.Condition.*;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Publishers")
public class PublisherCheckColumnsTests extends BaseTest {
    private PublishersPage publisherPage;

    public PublisherCheckColumnsTests() {
        publisherPage = new PublishersPage();
    }

    @BeforeClass
    private void login() {
        testStart()
                .given()
                .openDirectPath(Path.PUBLISHER)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, publisherPage.getNuxtProgress())
                .scrollIntoView(publisherPage.getPageTitle())
                .testEnd();
    }

    @Test(description = "Check all columns are show/hide")
    public void checkColumns() {
        var tableOptions = publisherPage.getTable().getShowHideColumns();
        var tableData = publisherPage.getTable().getTableData();
        var tablePagination = publisherPage.getTable().getTablePagination();
        testStart()
                .and("Action to remove the scroll table")
                .scrollIntoView(tablePagination.getPageMenu())
                .selectFromDropdown(tablePagination.getPageMenu(),
                        tablePagination.getRowNumbersList(), "10")
                .and("'Show' all columns")
                .scrollIntoView(tableOptions.getShowHideColumnsBtn())
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.ID))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.NAME))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.EXTERNAL_ID))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CATEGORY))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.STATUS))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.DOMAIN))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CURRENCY))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.AD_OPS_PERSON))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.MAIL))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .then("All columns should be shown")
                .validateListSize(tableData.getColumns(),
                        ColumnNames.ID.getName(),
                        ColumnNames.NAME.getName(),
                        ColumnNames.EXTERNAL_ID.getName(),
                        ColumnNames.CATEGORY.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.DOMAIN.getName(),
                        ColumnNames.CURRENCY.getName(),
                        ColumnNames.AD_OPS_PERSON.getName(),
                        ColumnNames.MAIL.getName(),
                        ColumnNames.CREATED_DATE.getName(),
                        ColumnNames.CREATED_BY.getName(),
                        ColumnNames.UPDATED_DATE.getName(),
                        ColumnNames.UPDATED_BY.getName())
                .and("Hide all columns")
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.ID))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.NAME))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.EXTERNAL_ID))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CATEGORY))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.STATUS))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.DOMAIN))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CURRENCY))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.AD_OPS_PERSON))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.MAIL))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .then("All columns should be hidden")
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.ID.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.NAME.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.EXTERNAL_ID.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.CATEGORY.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.STATUS.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.DOMAIN.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.CURRENCY.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.AD_OPS_PERSON.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.MAIL.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.CREATED_DATE.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.CREATED_BY.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.UPDATED_DATE.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.UPDATED_BY.getName()))
                .testEnd();
    }

    @AfterClass
    private void logout() {
        testStart()
                .logOut()
                .testEnd();
    }

}
