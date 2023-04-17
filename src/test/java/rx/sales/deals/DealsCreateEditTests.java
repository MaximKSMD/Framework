package rx.sales.deals;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.common.Currency;
import api.dto.rx.privateauction.PrivateAuction;
import api.dto.rx.privateauction.PrivateAuctionRequest;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.sales.deals.DealsPage;
import pages.sales.privateauctions.PrivateAuctionsPage;
import rx.BaseTest;
import widgets.common.detailsmenu.menu.TableItemDetailsMenu;
import widgets.common.table.ColumnNames;
import widgets.common.table.Statuses;
import widgets.sales.deals.sidebar.CreateDealSidebar;
import widgets.sales.privateauctions.sidebar.CreatePrivateAuctionSidebar;
import zutils.StringUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static api.preconditionbuilders.PrivateAuctionPrecondition.privateAuction;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static java.lang.String.format;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Deals")
public class DealsCreateEditTests extends BaseTest {

    private PrivateAuctionsPage privateAuctionsPage;
    private TableItemDetailsMenu auctionTableDetailsMenu;
    private CreatePrivateAuctionSidebar privateAuctionSidebar;
    private DealsPage dealsPage;
    private CreateDealSidebar dealSidebar;

    private String dealName;
    private Publisher publisher;
    private PrivateAuction auction;

    public DealsCreateEditTests() {

        privateAuctionsPage = new PrivateAuctionsPage();
        privateAuctionSidebar = new CreatePrivateAuctionSidebar();
        auctionTableDetailsMenu = new TableItemDetailsMenu();
        dealsPage = new DealsPage();
        dealSidebar = new CreateDealSidebar();
    }

    @BeforeClass
    private void login() {
        dealName = captionWithSuffix("autoDeal");
        publisher = createPublisher();

        testStart()
                .given()
                .openDirectPath(Path.DEALS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, dealsPage.getNuxtProgress())
                .testEnd();
    }

    @Test(description = "Create Deal from Private Auction sidebar")
    public void createFromPrivateAuctionSidebar() {
        var tableData = privateAuctionsPage.getTable().getTableData();
        var tableOptions = privateAuctionsPage.getTable().getShowHideColumns();
        ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.of("UTC"));

        var endDay = "20";
        var startDay = "12";
        var year = currentDate.plusMonths(1).getYear();
        var month = currentDate.plusMonths(1).getMonth();

        auction = createPrivateAuction(format("%s-%s-%sT00:00:00Z", year,
                        month.getValue() < 10 ? format("0%s", month.getValue()) : month.getValue(), startDay),
                format("%s-%s-%sT00:00:00Z", year, month.getValue() < 10 ? format("0%s", month.getValue()) : month.getValue(), endDay));

        testStart()
                .given()
                .openDirectPath(Path.PRIVATE_AUCTIONS)
                .waitAndValidate(disappear, privateAuctionsPage.getNuxtProgress())
                .setValueWithClean(privateAuctionsPage.getTable().getTableData().getSearch(), auction.getName())
                .clickOnTableCellLink(dealsPage.getDealsTable().getTableData(), ColumnNames.NAME, auction.getName())
                .waitSideBarOpened()
                .clickOnWebElement(privateAuctionSidebar.getSaveAndCreateDealButton())
                .waitSideBarOpened()
                .validate(dealSidebar.getPublisherDropdown(), auction.getPublisherName())
                .validate(enabled, dealSidebar.getPublisherDropdown())
                .validate(dealSidebar.getNameInput(), "")
                .setValue(dealSidebar.getNameInput(), dealName)
                .validate(dealSidebar.getPrivateAuctionInput(), auction.getName())
                .validate(enabled, dealSidebar.getPrivateAuctionDropdown())
                .validate(enabled, dealSidebar.getDateRangeField().getDateRangeInput())
                .validateAttribute(dealSidebar.getDateRangeField().getDateRangeInput(), "value", format("%s-%s-%s - %s-%s-%s GMT",
                        year, month.getValue() < 10 ? format("0%s", month.getValue()) : month.getValue(), startDay,
                        year, month.getValue() < 10 ? format("0%s", month.getValue()) : month.getValue(), endDay))
                .validate(dealSidebar.getFloorPriceInput(), "")
                .setValueWithClean(dealSidebar.getFloorPriceInput(), "5.20")
                .validate(dealSidebar.getCurrencyInput().getText(), publisher.getCurrency())
                .validate(dealSidebar.getDspInput().getText(), "")
                .selectFromDropdown(dealSidebar.getDspDropdown(), dealSidebar.getDropDownItems(), "Eskimi")
                .clickOnWebElement(dealSidebar.getSaveDealButton())
                .waitSideBarClosed()
                .then("Errors are not appear")
                .waitAndValidate(not(visible), dealSidebar.getErrorAlertByFieldName("Date Range"))
                .waitAndValidate(not(visible), dealSidebar.getErrorAlertByFieldName("Name"))
                .waitAndValidate(not(visible), dealSidebar.getErrorAlertByFieldName("Floor Price"))
                .waitAndValidate(not(visible), dealSidebar.getErrorAlertByFieldName("DSP"))
                .waitAndValidate(not(visible), dealSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), dealsPage.getToasterMessage().getPanelError())
                .and()
                .setValueWithClean(tableData.getSearch(), dealName)
                .pressEnterKey(tableData.getSearch())
                .validateContainsText(dealsPage.getDealsTable().getTablePagination().getPaginationPanel(), "1-1 of 1")
                .and("Show all columns")
                .selectFromDropdown(privateAuctionsPage.getTable().getTablePagination().getPageMenu(),
                        privateAuctionsPage.getTable().getTablePagination().getRowNumbersList(), "10")
                .waitAndValidate(disappear, privateAuctionsPage.getNuxtProgress())
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.CREATED_BY))
                .selectCheckBox(tableOptions.getMenuItemCheckbox(ColumnNames.UPDATED_BY))
                .clickOnWebElement(tableOptions.getShowHideColumnsBtn())
                .then("Validate data in table")
                .validate(tableData.getCellByRowValue(ColumnNames.STATUS, ColumnNames.NAME, dealName), Statuses.ACTIVE.getStatus())
                .validate(tableData.getCellByRowValue(ColumnNames.PUBLISHER, ColumnNames.NAME, dealName), auction.getPublisherName())
                .validate(tableData.getCellByRowValue(ColumnNames.ALWAYS_ON, ColumnNames.NAME, dealName), "No")
                .validate(tableData.getCellByRowValue(ColumnNames.CREATED_BY, ColumnNames.NAME, dealName), TEST_USER.getMail())
                .validate(tableData.getCellByRowValue(ColumnNames.UPDATED_BY, ColumnNames.NAME, dealName), "")
                .validate(tableData.getCellByRowValue(ColumnNames.PRICE_VALUE, ColumnNames.NAME, dealName), "5.20")
                .validate(tableData.getCellByRowValue(ColumnNames.PRICE_CURRENCY, ColumnNames.NAME, dealName), publisher.getCurrency())
                .validate(tableData.getCellByRowValue(ColumnNames.START_DATE, ColumnNames.NAME, dealName),
                        StringUtils.getDateAsString(year, month, Integer.valueOf(startDay)))
                .validate(tableData.getCellByRowValue(ColumnNames.END_DATE, ColumnNames.NAME, dealName),
                        StringUtils.getDateAsString(year, month, Integer.valueOf(endDay)))
                .validate(tableData.getCellByRowValue(ColumnNames.CREATED_DATE, ColumnNames.NAME, dealName),
                        StringUtils.getDateAsString(currentDate.getYear(), currentDate.getMonth(), currentDate.getDayOfMonth()))
                .validate(tableData.getCellByRowValue(ColumnNames.UPDATED_DATE, ColumnNames.NAME, dealName),
                        StringUtils.getDateAsString(currentDate.getYear(), currentDate.getMonth(), currentDate.getDayOfMonth()))
                .testEnd();
    }

    private PrivateAuction createPrivateAuction(String startDate, String endDate) {

        return privateAuction()
                .createPrivateAuction(PrivateAuctionRequest.builder()
                        .name(captionWithSuffix("autoAuction"))
                        .publisherId(publisher.getId())
                        .enabled(true)
                        .noEndDate(false)
                        .startDate(startDate)
                        .endDate(endDate)
                        .relatedPackages(List.of())
                        .optimized(true)
                        .build())
                .build()
                .getPrivateAuctionResponse();
    }

    private Publisher createPublisher() {

        return publisher()
                .createNewPublisher(captionWithSuffix("AutoPubDeal"), true, Currency.JPY, List.of(), List.of(24, 30))
                .build()
                .getPublisherResponse();
    }

    @AfterClass(alwaysRun = true)
    private void logout() {

        testStart()
                .logOut()
                .testEnd();
    }
}
