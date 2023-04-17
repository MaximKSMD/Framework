package rx.protections.columnsfilter;

import api.dto.rx.protection.Protection;
import api.dto.rx.protection.ProtectionRequest;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.protections.ProtectionsPage;
import rx.BaseTest;
import widgets.common.table.ColumnNames;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static api.preconditionbuilders.ProtectionsPrecondition.protection;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static java.lang.String.format;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Protections Columns Filter")
public class ProtectionsColumnsFilterCombinationsTests extends BaseTest {

    private ProtectionsPage protectionPage;

    public ProtectionsColumnsFilterCombinationsTests() {
        protectionPage = new ProtectionsPage();
    }

    //TODO: need to add more cases, is not finished
    @BeforeClass
    private void login() {
        var tableColumns = protectionPage.getProtectionsTable().getShowHideColumns();

        var allProtectionsList = getAllProtectionBE();

        testStart()
                .given()
                .openDirectPath(Path.PROTECTIONS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, protectionPage.getNuxtProgress())
                .scrollIntoView(protectionPage.getProtectionPageTitle())
                .clickOnWebElement(tableColumns.getShowHideColumnsBtn())
                .selectCheckBox(tableColumns.getMenuItemCheckbox(ColumnNames.CREATED_DATE))
                .and("Select 15 rows per page")
                .scrollIntoView(protectionPage.getProtectionsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(protectionPage.getProtectionsTable().getTablePagination().getPageMenu(),
                        protectionPage.getProtectionsTable().getTablePagination().getRowNumbersList(), "15")

                .testEnd();
    }

    @Test(description = "Combination filters only for Active Protections")
    public void activeProtectionsCombinationFilters() {
        var filter = protectionPage.getProtectionsTable().getColumnFiltersBlock();
        var tableData = protectionPage.getProtectionsTable().getTableData();
        var publishersActiveProtectionsList = getPublisherNamesFromProtection(Map.of("active", true));

        testStart()
                .and("Select Column Filter 'Status'")
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.STATUS))
                .and("Select Active option")
                .selectRadioButton(filter.getActiveBooleanFilter().getActiveRadioButton())
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .then("Check filtered data")
                .clickOnWebElement(tableData.getColumnHeader(ColumnNames.ID.getName()))
                .clickOnWebElement(tableData.getColumnHeader(ColumnNames.ID.getName()))
                .validateAttribute(tableData.getColumnHeader(ColumnNames.ID.getName()),
                        "aria-sort", "ascending")
                .validateList(tableData.getCustomCells(ColumnNames.ID),
                        getProtectionsIdsWithFilterBE(Map.of("active", true, "limit", 10)))
                .and("Select 20 rows per page")
                .scrollIntoView(protectionPage.getProtectionsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(protectionPage.getProtectionsTable().getTablePagination().getPageMenu(),
                        protectionPage.getProtectionsTable().getTablePagination().getRowNumbersList(), "15")
                .validateAttribute(tableData.getColumnHeader(ColumnNames.ID.getName()),
                        "aria-sort", "ascending")
                .validateList(tableData.getCustomCells(ColumnNames.ID),
                        getProtectionsIdsWithFilterBE(Map.of("active", true, "limit", 15)))
                .and("Select Column Filter 'Publisher'")
                .scrollIntoView(protectionPage.getProtectionPageTitle())
                .clickOnWebElement(filter.getColumnsFilterButton())
                .waitAndValidate(visible, filter.getFilterOptionsMenu())
                .clickOnWebElement(filter.getFilterOptionByName(ColumnNames.PUBLISHER))
                .and("Search 1 publisher")
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), publishersActiveProtectionsList.get(0))
                .validate(not(visible), protectionPage.getTableProgressBar())
                .waitAndValidate(visible, filter.getSinglepaneFilter().getFilterItemByName(publishersActiveProtectionsList.get(0)).getName())
                .validate(not(visible), protectionPage.getTableProgressBar())
                .clickOnWebElement(filter.getSinglepaneFilter()
                        .getFilterItemByName(publishersActiveProtectionsList.get(0)).getName())
                .and("Search 2 publisher")
                .setValueWithClean(filter.getSinglepaneFilter().getSearchInput(), publishersActiveProtectionsList.get(1))
                .validate(disappear, protectionPage.getTableProgressBar())
                .waitAndValidate(not(visible), filter.getSinglepaneFilter().getFilterItemByName(publishersActiveProtectionsList.get(0)).getName())
                .validateContainsText(filter.getSinglepaneFilter().getItemsTotalQuantityLabel(), "(1)")
                .waitAndValidate(visible, filter.getSinglepaneFilter().getFilterItemByName(publishersActiveProtectionsList.get(1)).getName())
                .validate(not(visible), protectionPage.getTableProgressBar())
                .clickOnWebElement(filter.getSinglepaneFilter()
                        .getFilterItemByName(publishersActiveProtectionsList.get(1)).getName())
                .clickOnWebElement(filter.getSinglepaneFilter().getSubmitButton())
                .and("Select 100 rows per page")
                .scrollIntoView(protectionPage.getProtectionsTable().getTablePagination().getPageMenu())
                .selectFromDropdown(protectionPage.getProtectionsTable().getTablePagination().getPageMenu(),
                        protectionPage.getProtectionsTable().getTablePagination().getRowNumbersList(), "25")
                .validateAttribute(tableData.getColumnHeader(ColumnNames.ID.getName()),
                        "aria-sort", "ascending")
                .then("Check filtered data")
                //TODO:
                .validateList(tableData.getCustomCells(ColumnNames.ID),
                        getProtectionsIdsWithFilterBE(Map.of(
                                "active", true,
                                "limit", 25,
                                "sort", "id-asc",
                                format("publisher_id=%s&publisher_id", getPublisherIdByName(publishersActiveProtectionsList.get(1))),
                                getPublisherIdByName(publishersActiveProtectionsList.get(0)))))
                .testEnd();
    }

    private List<Protection> getAllProtectionBE() {

        return protection()
                .getAllProtectionsList()
                .build()
                .getProtectionsGetAllResponse()
                .getItems();
    }

    private List<String> getProtectionsIdsWithFilterBE(Map<String, Object>  params) {

        return protection()
                .getProtectionsWithFilter(params)
                .build()
                .getProtectionsGetAllResponse()
                .getItems()
                .stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
    }

    private List<String> getPublisherNamesFromProtection(Map<String, Object> params) {

        return protection()
                .getProtectionsWithFilter(params)
                .build()
                .getProtectionsGetAllResponse()
                .getItems()
                .stream()
                .map(ProtectionRequest::getPublisherName)
                .distinct()
                .collect(Collectors.toList());
    }

    private String getPublisherIdByName(String name) {

        return publisher()
                .getPublishersListWithFilter(Map.of("name", name))
                .build()
                .getPublisherResponse()
                .getId().toString();
    }

    @AfterClass
    private void logout() {
        testStart()
                .logOut()
                .testEnd();
    }
}
