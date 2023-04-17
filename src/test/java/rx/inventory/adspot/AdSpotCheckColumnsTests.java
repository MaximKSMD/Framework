package rx.inventory.adspot;

import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.inventory.adspots.AdSpotsPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Ad Spots")
public class AdSpotCheckColumnsTests extends BaseTest {
    private AdSpotsPage adSpotsPage;

    public AdSpotCheckColumnsTests() {
        adSpotsPage = new AdSpotsPage();
    }

    @BeforeClass
    private void login() {
        testStart()
                .given()
                .openDirectPath(Path.AD_SPOT)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, adSpotsPage.getNuxtProgress())
                .scrollIntoView(adSpotsPage.getPageTitle())

                .testEnd();
    }

    @Test(description = "Check columns")
    public void checkColumns() {
        var tableOptions = adSpotsPage.getAdSpotsTable().getShowHideColumns();
        var tableData = adSpotsPage.getAdSpotsTable().getTableData();
        var tablePagination = adSpotsPage.getAdSpotsTable().getTablePagination();
        testStart()
                .and("Action to remove the scroll table")
                .scrollIntoView(tablePagination.getPageMenu())
                .selectFromDropdown(tablePagination.getPageMenu(),
                        tablePagination.getRowNumbersList(), "10")
                .and("'Show' all columns")
                .scrollIntoView(tableOptions.getShowHideColumnsBtn())
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.ID))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.DETAILS))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.AD_SPOT_NAME))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.PUBLISHER))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.RELATED_MEDIA))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.STATUS))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.PAGE_CATEGORY))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.TEST_MODE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.DEFAULT_SIZES))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.DEFAULT_FLOOR_PRICE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .then("All columns should be shown")
                .validateListSize(tableData.getColumns(),
                        ColumnNames.ID.getName(),
                        ColumnNames.DETAILS.getName(),
                        ColumnNames.AD_SPOT_NAME.getName(),
                        ColumnNames.PUBLISHER.getName(),
                        ColumnNames.RELATED_MEDIA.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.PAGE_CATEGORY.getName(),
                        ColumnNames.TEST_MODE.getName(),
                        ColumnNames.DEFAULT_SIZES.getName(),
                        ColumnNames.DEFAULT_FLOOR_PRICE.getName(),
                        ColumnNames.CREATED_DATE.getName(),
                        ColumnNames.UPDATED_DATE.getName())
                .and("Hide all columns")
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.ID))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.DETAILS))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.AD_SPOT_NAME))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.PUBLISHER))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.RELATED_MEDIA))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.STATUS))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.PAGE_CATEGORY))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.TEST_MODE))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.DEFAULT_SIZES))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.DEFAULT_FLOOR_PRICE))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .unSelectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .then("All columns should be hidden")
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.ID.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.DETAILS.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.AD_SPOT_NAME.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.PUBLISHER.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.RELATED_MEDIA.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.STATUS.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.PAGE_CATEGORY.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.TEST_MODE.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.DEFAULT_SIZES.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.DEFAULT_FLOOR_PRICE.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.CREATED_DATE.getName()))
                .validate(not(visible), tableData.getColumnHeader(ColumnNames.UPDATED_DATE.getName()))
                .testEnd();
    }

    @AfterClass
    private void logout() {

        testStart()
                .logOut()
                .testEnd();
    }
}
