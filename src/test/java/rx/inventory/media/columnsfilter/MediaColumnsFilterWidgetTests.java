package rx.inventory.media.columnsfilter;

import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.inventory.media.MediaPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;
import zutils.StringUtils;

import java.util.List;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Media Columns Filter")
public class MediaColumnsFilterWidgetTests extends BaseTest {

    private MediaPage mediaPage;

    public MediaColumnsFilterWidgetTests() {
        mediaPage = new MediaPage();
    }

    @BeforeClass
    private void login() {
        var tableColumns = mediaPage.getMediaTable().getShowHideColumns();

        testStart()
                .given()
                .openDirectPath(Path.MEDIA)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .scrollIntoView(mediaPage.getMediaTable().getTablePagination().getPageMenu())
                .selectFromDropdown(mediaPage.getMediaTable().getTablePagination().getPageMenu(),
                        mediaPage.getMediaTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(tableColumns.getShowHideColumnsBtn())
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.UPDATED_DATE))
                .testEnd();
    }

    @Test(description = "Check Active/Inactive Chip Widget Component")
    public void testActiveInactiveChipWidgetComponent() {
        var filter = mediaPage.getMediaTable().getColumnFiltersBlock();
        var table = mediaPage.getMediaTable().getTableData();

        testStart()
                .and("Click on 'Column Filters'")
                .scrollIntoView(mediaPage.getPageTitle())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .and("Select Column Filter 'Active/Inactive'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.STATUS))
                .then("Title should be displayed")
                .validate(filter.getActiveBooleanFilter().getFilterHeaderLabel(), StringUtils.getFilterHeader(ColumnNames.STATUS.getName()))
                .then("All options should be unselected")
                .validateAttribute(filter.getActiveBooleanFilter().getActiveRadioButton(),"aria-checked","false")
                .validateAttribute(filter.getActiveBooleanFilter().getInactiveRadioButton(),"aria-checked","false")
                .and("Select Active")
                .selectRadioButton(filter.getActiveBooleanFilter().getActiveRadioButton())
                .validateAttribute(filter.getActiveBooleanFilter().getActiveRadioButton(),"aria-checked","true")
                .and("Select Inactive")
                .selectRadioButton(filter.getActiveBooleanFilter().getInactiveRadioButton())
                .then("Only one option should be selected")
                .validateAttribute(filter.getActiveBooleanFilter().getActiveRadioButton(),"aria-checked","false")
                .validateAttribute(filter.getActiveBooleanFilter().getInactiveRadioButton(),"aria-checked","true")
                .and("Click on Back")
                .clickOnWebElement(filter.getActiveBooleanFilter().getBackButton())
                .then("Columns Menu should appear")
                .validateList(filter.getFilterOptionItems(), List.of(ColumnNames.PUBLISHER.getName(),
                        ColumnNames.PLATFORM.getName(),
                        ColumnNames.STATUS.getName(),
                        ColumnNames.CREATED_DATE.getName(),
                        ColumnNames.UPDATED_DATE.getName()))
                .and("Select Column Filter 'Active/Inactive'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.STATUS))
                .then("All options should be reset and unselected")
                .validateAttribute(filter.getActiveBooleanFilter().getActiveRadioButton(),"aria-checked","false")
                .validateAttribute(filter.getActiveBooleanFilter().getInactiveRadioButton(),"aria-checked","false")
                .and("Select Inactive")
                .selectRadioButton(filter.getActiveBooleanFilter().getInactiveRadioButton())
                .validateAttribute(filter.getActiveBooleanFilter().getInactiveRadioButton(),"aria-checked","true")
                .and("Click on Submit")
                .clickOnWebElement(filter.getActiveBooleanFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validate(visible, table.getChipItemByName(ColumnNames.STATUS.getName()).getHeaderLabel())
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate value on chip")
                .validate(table.getChipItemByName(ColumnNames.STATUS.getName()).getChipFilterOptionItemByName("Inactive"))
                .clickOnWebElement(table.getChipItemByName(ColumnNames.STATUS.getName()).getCloseIcon())
                .testEnd();
    }

    @AfterClass
    private void logout() {
        testStart()
                .logOut()
                .testEnd();
    }
}
