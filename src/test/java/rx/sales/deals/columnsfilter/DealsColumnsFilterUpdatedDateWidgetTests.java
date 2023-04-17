package rx.sales.deals.columnsfilter;

import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.sales.deals.DealsPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;
import zutils.StringUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static java.lang.String.format;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Protections Columns Filter")
public class DealsColumnsFilterUpdatedDateWidgetTests extends BaseTest {

    private DealsPage dealsPage;

    public DealsColumnsFilterUpdatedDateWidgetTests() {
        dealsPage = new DealsPage();
    }

    @BeforeClass
    private void login() {
        var tableColumns = dealsPage.getDealsTable().getShowHideColumns();

        testStart()
                .given()
                .openDirectPath(Path.DEALS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, dealsPage.getNuxtProgress())
                .and("Select 10 rows per page")
                .scrollIntoView(dealsPage.getDealsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(dealsPage.getDealsTable().getTablePagination().getPageMenu(),
                        dealsPage.getDealsTable().getTablePagination().getRowNumbersList(), "10")
                .scrollIntoView(dealsPage.getPageTitle())
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .testEnd();
    }


    @Test(description = "Check Default State")
    public void testDefaultStateColumnsFilterComponent() {
        var filter = dealsPage.getDealsTable().getColumnFiltersBlock();
        var calendar = filter.getCalendarFilter().getCalendar();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        testStart()
                .and("Select Column Filter 'Updated Date'")
                .scrollIntoView(dealsPage.getPageTitle())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.UPDATED_DATE))
                .then("Current date should be selected by default")
                .validate(calendar.getMonthOrYearHeaderButton().getText(), StringUtils.getStringMonthYear(currentDate.getMonth(),
                        currentDate.getYear()))
                .validate(calendar.getSelectedDayButton(), String.valueOf(currentDate.getDayOfMonth()))
                .testEnd();
    }

    @Test(description = "Check Next Month", dependsOnMethods = "testDefaultStateColumnsFilterComponent")
    public void testNextMonthColumnsFilterComponent() {
        var calendar = dealsPage.getDealsTable().getColumnFiltersBlock().getCalendarFilter().getCalendar();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        testStart()
                .and("Click on the Next Month button >")
                .clickOnWebElement(calendar.getNextMonthButton())
                .waitAndValidate(visible, calendar.getMonthOrYearHeaderButton())
                .then("Should be displayed next month")
                .validateContainsText(calendar.getMonthOrYearHeaderButton(), StringUtils.getStringMonthYear(currentDate.plusMonths(1).getMonth(), currentDate.plusMonths(1).getYear()))
                .testEnd();
    }

    @Test(description = "Check Back button", dependsOnMethods = "testNextMonthColumnsFilterComponent")
    public void testBackButtonColumnsFilterComponent() {
        var filter = dealsPage.getDealsTable().getColumnFiltersBlock();
        var calendar = filter.getCalendarFilter().getCalendar();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        testStart()
                .and("Click on Back button")
                .clickOnWebElement(filter.getCalendarFilter().getBackButton())
                .then("Columns Menu should appear")
                .validateList(filter.getFilterOptionItems(),
                        List.of(ColumnNames.PUBLISHER.getName(),
                                ColumnNames.PRIVATE_AUCTION.getName(),
                                ColumnNames.STATUS.getName(),
                                ColumnNames.DSP.getName(),
                                ColumnNames.PRICE_CURRENCY.getName(),
                                ColumnNames.ALWAYS_ON.getName(),
                                ColumnNames.START_DATE.getName(),
                                ColumnNames.END_DATE.getName(),
                                ColumnNames.UPDATED_DATE.getName()))
                .and("Select Column Filter 'Updated Date'")
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.UPDATED_DATE))
                .then("Current date should be selected by default")
                .validate(calendar.getMonthOrYearHeaderButton().getText(), StringUtils.getStringMonthYear(currentDate.getMonth(), currentDate.getYear()))
                .validate(calendar.getSelectedDayButton(), String.valueOf(currentDate.getDayOfMonth()))
                .testEnd();
    }

    @Test(description = "Check Previous Month", dependsOnMethods = "testBackButtonColumnsFilterComponent")
    public void testPreviousMonthColumnsFilterComponent() {
        var filter = dealsPage.getDealsTable().getColumnFiltersBlock();
        var calendar = filter.getCalendarFilter().getCalendar();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        testStart()
                .and("Click on the Previous Month button")
                .clickOnWebElement(calendar.getPreviousMonthButton())
                .waitAndValidate(visible, calendar.getMonthOrYearHeaderButton())
                .then("Should be displayed previous month")
                .validateContainsText(calendar.getMonthOrYearHeaderButton(), StringUtils.getStringMonthYear(currentDate.minusMonths(1).getMonth(), currentDate.minusMonths(1).getYear()))
                .testEnd();
    }

    @Test(description = "Select First Day of the current month and click Cancel", dependsOnMethods = "testPreviousMonthColumnsFilterComponent")
    public void testCancelButtonColumnsFilterComponent() {
        var filter = dealsPage.getDealsTable().getColumnFiltersBlock();
        var table = dealsPage.getDealsTable().getTableData();
        var calendar = filter.getCalendarFilter().getCalendar();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        testStart()
                .and("Select 1 day of the month and Click on Cancel button")
                .clickOnWebElement(calendar.getDayButtonByValue("1"))
                .clickOnWebElement(filter.getCalendarFilter().getCancelButton())
                .then("Calendar widget should be closed and chip under the table should not appear")
                .waitAndValidate(not(visible), filter.getCalendarFilter().getFilterHeaderLabel())
                .validate(table.countFilterChipsItems(), 0)
                .testEnd();
    }

    @Test(description = "Select Last Day of the current month and click Submit", dependsOnMethods = "testCancelButtonColumnsFilterComponent")
    public void testSubmitButtonColumnsFilterComponent() {
        var filter = dealsPage.getDealsTable().getColumnFiltersBlock();
        var table = dealsPage.getDealsTable().getTableData();
        var calendar = filter.getCalendarFilter().getCalendar();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        testStart()
                .and("Select Column Filter 'Updated Date'")
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.UPDATED_DATE))
                .and("Select Last Date of the month")
                .clickOnWebElement(calendar.getDayButtonByValue(StringUtils.getLastDayOfMonth(currentDate.getYear(), currentDate.getMonth())))
                .and("Click on Submit")
                .clickOnWebElement(filter.getCalendarFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validate(visible, table.getChipItemByName(ColumnNames.UPDATED_DATE.getName()).getHeaderLabel())
                .validate(visible, table.getChipItemByName(ColumnNames.UPDATED_DATE.getName()).getCloseIcon())
                .validateContainsText(table.getChipItemByName(ColumnNames.UPDATED_DATE.getName()).getHeaderLabel(),
                        format("%s:", ColumnNames.UPDATED_DATE.getName()))
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate list of selected date")
                .validate(table.getChipItemByName(ColumnNames.UPDATED_DATE.getName()).countFilterOptionsChipItems(), 1)
                .validate(exist, table.getChipItemByName(ColumnNames.UPDATED_DATE.getName()).getChipFilterOptionItemByName(
                        StringUtils.getLastDayOfTheMonthDateAsString(currentDate.getYear(), currentDate.getMonth())))
                .testEnd();
    }

    @Issue("https://rakutenadvertising.atlassian.net/browse/GS-3325")
    @Test(description = "Select Period of the current month and click Submit", dependsOnMethods = "testSubmitButtonColumnsFilterComponent")
    public void testPeriodAndSubmitButtonColumnsFilterComponent() {
        var filter = dealsPage.getDealsTable().getColumnFiltersBlock();
        var table = dealsPage.getDealsTable().getTableData();
        var calendar = filter.getCalendarFilter().getCalendar();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        testStart()
                .and("Select Column Filter 'Updated Date'")
                .scrollIntoView(dealsPage.getPageTitle())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.UPDATED_DATE))
                .and("Select Period Date of the month")
                .clickOnWebElement(calendar.getDayButtonByValue("7"))
                .clickOnWebElement(calendar.getDayButtonByValue("7"))
                .clickOnWebElement(calendar.getDayButtonByValue("14"))
                .and("Click on Submit")
                .clickOnWebElement(filter.getCalendarFilter().getSubmitButton())
                .then("ColumnsFilter widget is closed")
                .validate(not(visible), filter.getFilterOptionsMenu())
                .validate(visible, table.getChipItemByName(ColumnNames.UPDATED_DATE.getName()).getHeaderLabel())
                .validate(visible, table.getChipItemByName(ColumnNames.UPDATED_DATE.getName()).getCloseIcon())
                .validateContainsText(table.getChipItemByName(ColumnNames.UPDATED_DATE.getName()).getHeaderLabel(),
                        format("%s:", ColumnNames.UPDATED_DATE.getName()))
                .validate(table.countFilterChipsItems(), 1)
                .then("Validate list of selected date")
                .validate(table.getChipItemByName(ColumnNames.UPDATED_DATE.getName()).countFilterOptionsChipItems(), 1)
                .validateContainsText(table.getChipItemByName(ColumnNames.UPDATED_DATE.getName()).getChipFilterOptionItemByPosition(0),
                        format("%s – %s",
                                StringUtils.getDateAsString(currentDate.getYear(), currentDate.getMonth(), 7),
                                StringUtils.getDateAsString(currentDate.getYear(), currentDate.getMonth(), 14)))
                .testEnd();
    }

    @Test(description = "Delete Update By Chip", dependsOnMethods = "testPeriodAndSubmitButtonColumnsFilterComponent", alwaysRun = true)
    public void testDeleteColumnsFilterComponent() {
        var table = dealsPage.getDealsTable().getTableData();

        testStart()
                .and("Click 'X' icon on the Update Date chip")
                .clickOnWebElement(table.getChipItemByName(ColumnNames.UPDATED_DATE.getName()).getCloseIcon())
                .then("Chip 'Updated Date' should be disappear")
                .validate(table.countFilterChipsItems(), 0)
                .testEnd();
    }


    @AfterClass
    private void logout() {
        testStart()
                .logOut()
                .testEnd();
    }
}