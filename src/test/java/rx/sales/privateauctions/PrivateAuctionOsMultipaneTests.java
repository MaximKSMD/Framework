package rx.sales.privateauctions;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.os.OperatingSystem;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.sales.privateauctions.PrivateAuctionsPage;
import rx.BaseTest;
import rx.enums.MultipaneConstants;
import widgets.sales.privateauctions.sidebar.CreatePrivateAuctionSidebar;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import static api.preconditionbuilders.OperatingSystemPrecondition.operatingSystem;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Private Auctions")
public class PrivateAuctionOsMultipaneTests extends BaseTest {

    private Publisher publisherActive;
    private List<OperatingSystem> operatingSystemList;
    private PrivateAuctionsPage privateAuctionsPage;
    private CreatePrivateAuctionSidebar privateAuctionSidebar;

    public PrivateAuctionOsMultipaneTests() {

        privateAuctionsPage = new PrivateAuctionsPage();
        privateAuctionSidebar = new CreatePrivateAuctionSidebar();
    }

    @BeforeClass
    private void login() {

        publisherActive = getAllPublishersByParams(Map.of("sort","created_at-desc"));

        operatingSystemList = getOperatingSystems();

        testStart()
                .given()
                .openDirectPath(Path.PRIVATE_AUCTIONS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, privateAuctionsPage.getNuxtProgress())
                .and("Press 'Create Private Auction' button")
                .clickOnWebElement(privateAuctionsPage.getCreatePrivateAuctionsButton())
                .waitSideBarOpened()
                .testEnd();
    }

    @Test(description = "Check OS Items List is filled and disabled by default", priority = 0)
    private void checkDefaultOsList() {
        var operatingSystemMultipane = privateAuctionSidebar.getOperatingSystemMultipane();

        testStart()
                .then("Validate OS items list should not be empty")
                .validate(operatingSystemMultipane.countSelectTableItems(), operatingSystemList.size())
                .validate(disabled, operatingSystemMultipane.getSearchInput())
                .validateContainsText(operatingSystemMultipane.getIncludeAllButton(), String.format("INCLUDE ALL\n(%s)", 0))
                .testEnd();
    }

    @Test(description = "Check OS Items List", priority = 1)
    private void checkOsList() {
        var operatingSystemMultipane = privateAuctionSidebar.getOperatingSystemMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(privateAuctionSidebar.getPublisherNameDropdown(),
                        privateAuctionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'OS' multipane")
                .clickOnWebElement(operatingSystemMultipane.getPanelNameLabel())
                .then("Validate OS items list should not be empty")
                .validate(operatingSystemMultipane.countSelectTableItems(), operatingSystemList.size())
                .testEnd();

        operatingSystemList.forEach(e -> {
            testStart()
                    .validate(visible, operatingSystemMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Search OS", priority = 2)
    private void checkSearchOS() {
        var operatingSystemMultipane = privateAuctionSidebar.getOperatingSystemMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(privateAuctionSidebar.getPublisherNameDropdown(),
                        privateAuctionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'OS' multipane")
                .clickOnWebElement(operatingSystemMultipane.getPanelNameLabel())
                .and(String.format("Search OS by Name = %s", operatingSystemList.get(0).getName()))
                .setValueWithClean(operatingSystemMultipane.getSearchInput(), operatingSystemList.get(0).getName())
                .pressEnterKey(operatingSystemMultipane.getSearchInput())
                .then("Validate item list includes OS")
                .validateContainsText(operatingSystemMultipane.getItemsQuantityString(), "(1)")
                .validate(operatingSystemMultipane.countSelectTableItems(), 1)
                .validate(operatingSystemMultipane.getSelectTableItemByPositionInList(0).getName(), operatingSystemList.get(0).getName())
                .and("Clear Search field")
                .setValueWithClean(operatingSystemMultipane.getSearchInput(), "")
                .pressEnterKey(operatingSystemMultipane.getSearchInput())
                .waitAndValidate(visible, operatingSystemMultipane.getSearchInput())
                .validate(operatingSystemMultipane.countSelectTableItems(), operatingSystemList.size())
                .testEnd();

        operatingSystemList.forEach(e -> {
            testStart()
                    .validate(visible, operatingSystemMultipane.getSelectTableItemByName(e.getName()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Multipane Text 'Include All'", priority = 3)
    private void checkMultipaneText() {
        var operatingSystemMultipane = privateAuctionSidebar.getOperatingSystemMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(privateAuctionSidebar.getPublisherNameDropdown(),
                        privateAuctionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'OS' multipane")
                .clickOnWebElement(operatingSystemMultipane.getPanelNameLabel())
                .then("Validate text above items panel")
                .validate(operatingSystemMultipane.getSelectionInfoExcludedLabel().getText(),
                        "")
                .and("Include All media")
                .clickOnWebElement(operatingSystemMultipane.getIncludeAllButton())
                .then("Validate 'Selected' items panel")
                .validate(operatingSystemMultipane.countSelectTableItems(), operatingSystemMultipane.countIncludedExcludedTableItems())
                .then("Validate text above items panel")
                .validate(operatingSystemMultipane.getSelectionInfoExcludedLabel().getText(),
                        MultipaneConstants.OPERATING_SYSTEMS_ARE_INCLUDED.setQuantity(operatingSystemList.size()))
                .testEnd();
    }

    @Test(description = "Check Multipane Text 'Clear All'", priority = 4)
    private void checkMultipaneTextClearAll() {
        var operatingSystemMultipane = privateAuctionSidebar.getOperatingSystemMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(privateAuctionSidebar.getPublisherNameDropdown(),
                        privateAuctionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'OS' multipane")
                .clickOnWebElement(operatingSystemMultipane.getPanelNameLabel())
                .and("Include All OSs")
                .clickOnWebElement(operatingSystemMultipane.getIncludeAllButton())
                .validate(operatingSystemMultipane.countIncludedExcludedTableItems(), operatingSystemList.size())
                .validate(operatingSystemMultipane.countSelectTableItems(), operatingSystemList.size())
                .and("Clear All OS")
                .clickOnWebElement(operatingSystemMultipane.getClearAllButton())
                .then("Validate text above items panel")
                .validate(operatingSystemMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_OPERATING_SYSTEMS_ARE_INCLUDED.setQuantity())
                .validate(operatingSystemMultipane.countSelectTableItems(), operatingSystemList.size())
                .validate(operatingSystemMultipane.countIncludedExcludedTableItems(), 0)
                .testEnd();
    }

    @Test(description = "Check Multipane Text (include not all items)", priority = 4)
    private void checkMultipaneTextNotAllItems() {
        var operatingSystemMultipane = privateAuctionSidebar.getOperatingSystemMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(privateAuctionSidebar.getPublisherNameDropdown(),
                        privateAuctionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'OS' multipane")
                .clickOnWebElement(operatingSystemMultipane.getPanelNameLabel())
                .testEnd();

        includeOneByOneItems(operatingSystemList);
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 5)
    private void checkMultipaneTextExcludeNotAllItems() {
        var operatingSystemMultipane = privateAuctionSidebar.getOperatingSystemMultipane();


        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(privateAuctionSidebar.getPublisherNameDropdown(),
                        privateAuctionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'OS' multipane")
                .clickOnWebElement(operatingSystemMultipane.getPanelNameLabel())
                .and("Include All OS")
                .clickOnWebElement(operatingSystemMultipane.getIncludeAllButton())
                .testEnd();

        excludeOneByOneItems(operatingSystemList);
    }


    @AfterClass(alwaysRun = true)
    private void logout() {

        testStart()
                .clickOnWebElement(privateAuctionSidebar.getCloseIcon())
                .waitSideBarClosed()
                .and("Logout")
                .logOut()
                .testEnd();
    }

    private List<OperatingSystem> getOperatingSystems() {

        return operatingSystem()
                .getOperatingSystemList()
                .build()
                .getOperatingSystemGetAllResponse()
                .getItems();
    }

    @Step("Include all items one by one")
    private void includeOneByOneItems(List<OperatingSystem> list) {
        var operatingSystemMultipane = privateAuctionSidebar.getOperatingSystemMultipane();
        var updated = new AtomicInteger(0);

        list.forEach(e ->
        {
            var selectedItem = operatingSystemMultipane.getSelectTableItemByPositionInList(updated.get());
            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validate(not(exist), selectedItem.getExcludedIcon())
                    .validate(not(visible), selectedItem.getIncludedIcon())
                    .validate(not(visible), selectedItem.getActiveIcon())
                    .validate(not(visible), selectedItem.getInactiveIcon())
                    .validate(not(visible), selectedItem.getAssociatedWithPublisherIcon())
                    .validate(visible, selectedItem.getIncludeButton())
                    .and()
                    .clickOnWebElement(selectedItem.getIncludeButton())
                    .validate(operatingSystemMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 0) ?
                            MultipaneConstants.ONE_OPERATING_SYSTEM_IS_INCLUDED.setQuantity(updated.incrementAndGet()) :
                            MultipaneConstants.OPERATING_SYSTEMS_ARE_INCLUDED.setQuantity(updated.incrementAndGet()))
                    .validate(exist, selectedItem.getName())
                    .testEnd();
        });
    }

    @Step("Exclude all items one by one")
    private void excludeOneByOneItems(List<OperatingSystem> list) {
        var updated = new AtomicInteger(list.size());
        var operatingSystemMultipane = privateAuctionSidebar.getOperatingSystemMultipane();


        list.forEach(item -> {
            var selectedItem = operatingSystemMultipane.getIncludedExcludedTableItemByName(item.getName());

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(operatingSystemMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
                            MultipaneConstants.ONE_OPERATING_SYSTEM_IS_INCLUDED.setQuantity(updated.getAndDecrement()) :
                            MultipaneConstants.OPERATING_SYSTEMS_ARE_INCLUDED.setQuantity(updated.getAndDecrement()))
                    .then()
                    .validate(visible, selectedItem.getRemoveButton())
                    .and(String.format("Remove selected item %s", selectedItem.getName()))
                    .clickOnWebElement(selectedItem.getRemoveButton())
                    .testEnd();
        });
    }

    @AfterMethod(alwaysRun = true)
    private void collapseMultipane() {
        var operatingSystemMultipane = privateAuctionSidebar.getOperatingSystemMultipane();

        testStart()
                .and("Collapse 'OS' multipane")
                .clickOnWebElement(operatingSystemMultipane.getPanelNameLabel())
                .testEnd();
    }

    private Publisher getAllPublishersByParams(Map<String, Object> strParams) {

        return publisher()
                .getPublishersListWithFilter(strParams)
                .build()
                .getPublisherGetAllResponse()
                .getItems().stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

    }
}
