package rx.sales.privateauctions;

import api.dto.rx.admin.publisher.Publisher;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.sales.privateauctions.PrivateAuctionsPage;
import rx.BaseTest;
import rx.enums.MultipaneConstants;
import widgets.common.adSizes.AdSizesList;
import widgets.sales.privateauctions.sidebar.CreatePrivateAuctionSidebar;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Private Auctions")
public class PrivateAuctionAdSizeMultipaneTests extends BaseTest {

    private Publisher publisherActive;
    private List<AdSizesList> adSizeList;
    private PrivateAuctionsPage privateAuctionsPage;
    private CreatePrivateAuctionSidebar privateAuctionSidebar;

    public PrivateAuctionAdSizeMultipaneTests() {
        privateAuctionsPage = new PrivateAuctionsPage();
        privateAuctionSidebar = new CreatePrivateAuctionSidebar();
    }

    @BeforeClass
    private void login() {
        publisherActive = getAllPublishersByParams(Map.of("sort","created_at-desc"));

        adSizeList = Arrays.asList(AdSizesList.values());

        testStart()
                .given()
                .openDirectPath(Path.PRIVATE_AUCTIONS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, privateAuctionsPage.getNuxtProgress())
                .and("Click 'Create Private Auction' button")
                .clickOnWebElement(privateAuctionsPage.getCreatePrivateAuctionsButton())
                .waitSideBarOpened()
                .testEnd();
    }

    @Test(description = "Check AdSize Items List is filled and disabled by default", priority = 0)
    private void checkDefaultAdSizeList() {
        var privateAuctionMultipane = privateAuctionSidebar.getAdSizeMultipane();

        testStart()
                .and("Scroll into 'AdSize' multipane")
                .scrollIntoView(privateAuctionSidebar.getSaveAndCloseButton())
                .then("Validate AdSize items list should not be empty")
                .validate(privateAuctionMultipane.countSelectTableItems(), adSizeList.size())
                .validate(disabled, privateAuctionMultipane.getSearchInput())
                .validateContainsText(privateAuctionMultipane.getIncludeAllButton(),String.format("INCLUDE ALL\n(%s)",0))
                .testEnd();
    }

    @Test(description = "Check AdSize Items List", priority = 1)
    private void checkAdSizeList() {
        var privateAuctionMultipane = privateAuctionSidebar.getAdSizeMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(privateAuctionSidebar.getPublisherNameDropdown(),
                        privateAuctionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'AdSize' multipane")
                .clickOnWebElement(privateAuctionMultipane.getPanelNameLabel())
                .then("Validate AdSize items list should not be empty")
                .validate(privateAuctionMultipane.countSelectTableItems(), adSizeList.size())
                .testEnd();

        adSizeList.stream().limit(10).collect(Collectors.toList()).forEach(e -> {
            testStart()
                    .validate(visible, privateAuctionMultipane.getSelectTableItemByName(e.getSize()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Search AdSize", priority = 2)
    private void checkSearchAdSize() {
        var privateAuctionMultipane = privateAuctionSidebar.getAdSizeMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(privateAuctionSidebar.getPublisherNameDropdown(),
                        privateAuctionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'AdSize' multipane")
                .clickOnWebElement(privateAuctionMultipane.getPanelNameLabel())
                .and(String.format("Search AdSize by Name = %s", adSizeList.get(0).getSize()))
                .setValueWithClean(privateAuctionMultipane.getSearchInput(), adSizeList.get(0).getSize())
                .pressEnterKey(privateAuctionMultipane.getSearchInput())
                .then("Validate item list includes AdSize")
                .validateContainsText(privateAuctionMultipane.getItemsQuantityString(),"(1)")
                .validate(privateAuctionMultipane.countSelectTableItems(), 1)
                .validate(privateAuctionMultipane.getSelectTableItemByPositionInList(0).getName(), adSizeList.get(0).getSize())
                .and("Clear Search field")
                .setValueWithClean(privateAuctionMultipane.getSearchInput(), "")
                .pressEnterKey(privateAuctionMultipane.getSearchInput())
                .and("Validate size of the filtered items list")
                .validate(privateAuctionMultipane.countSelectTableItems(), adSizeList.size())
                .testEnd();

        adSizeList.stream().limit(10).collect(Collectors.toList()).forEach(e -> {
            testStart()
                    .validate(visible, privateAuctionMultipane.getSelectTableItemByName(e.getSize()).getName())
                    .testEnd();
        });
    }

    @Test(description = "Check Multipane Text 'Include All'", priority = 3)
    private void checkMultipaneText() {
        var privateAuctionMultipane = privateAuctionSidebar.getAdSizeMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(privateAuctionSidebar.getPublisherNameDropdown(),
                        privateAuctionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'AdSize' multipane")
                .clickOnWebElement(privateAuctionMultipane.getPanelNameLabel())
                .then("Validate text above items panel")
                .validate(privateAuctionMultipane.getSelectionInfoExcludedLabel().getText(), "")
                .and("Include All media")
                .clickOnWebElement(privateAuctionMultipane.getIncludeAllButton())
                .then("Validate 'Selected' items panel")
                .validate(privateAuctionMultipane.countSelectTableItems(), privateAuctionMultipane.countIncludedExcludedTableItems())
                .then("Validate text above items panel")
                .validate(privateAuctionMultipane.getSelectionInfoExcludedLabel().getText(),
                        MultipaneConstants.AD_SIZES_ARE_INCLUDED.setQuantity(adSizeList.size()))
                .testEnd();
    }

    @Test(description = "Check Multipane Text 'Clear All'", priority = 4)
    private void checkMultipaneTextClearAll() {
        var privateAuctionMultipane = privateAuctionSidebar.getAdSizeMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(privateAuctionSidebar.getPublisherNameDropdown(),
                        privateAuctionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'AdSize' multipane")
                .clickOnWebElement(privateAuctionMultipane.getPanelNameLabel())
                .and("Include All AdSizes")
                .clickOnWebElement(privateAuctionMultipane.getIncludeAllButton())
                .validate(privateAuctionMultipane.countIncludedExcludedTableItems(), adSizeList.size())
                .validate(privateAuctionMultipane.countSelectTableItems(), adSizeList.size())
                .and("Clear All AdSizes")
                .clickOnWebElement(privateAuctionMultipane.getClearAllButton())
                .then("Validate text above items panel")
                .validate(privateAuctionMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_AD_SIZES_ARE_INCLUDED_DETAILS.setQuantity())
                .validate(privateAuctionMultipane.countSelectTableItems(), adSizeList.size())
                .validate(privateAuctionMultipane.countIncludedExcludedTableItems(), 0)
                .testEnd();
    }

    @Test(description = "Check Multipane Text (include not all items)", priority = 4)
    private void checkMultipaneTextNotAllItems() {
        var privateAuctionMultipane = privateAuctionSidebar.getAdSizeMultipane();

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(privateAuctionSidebar.getPublisherNameDropdown(),
                        privateAuctionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'AdSize' multipane")
                .clickOnWebElement(privateAuctionMultipane.getPanelNameLabel())
                .testEnd();

        includeOneByOneItems(adSizeList);
    }

    @Test(description = "Check Multipane Text (exclude not all items)", priority = 5)
    private void checkMultipaneTextExcludeNotAllItems() {
        var privateAuctionMultipane = privateAuctionSidebar.getAdSizeMultipane();

        var filterdAdSizes = adSizeList.stream()
                .filter(e -> e.getSize().contains("1")).collect(Collectors.toList());

        testStart()
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(privateAuctionSidebar.getPublisherNameDropdown(),
                        privateAuctionSidebar.getPublisherItems(), publisherActive.getName())
                .and(String.format("Select Publisher '%s'", publisherActive.getName()))
                .selectFromDropdown(privateAuctionSidebar.getPublisherNameDropdown(),
                        privateAuctionSidebar.getPublisherItems(), publisherActive.getName())
                .and("Expand 'Adsize' multipane")
                .clickOnWebElement(privateAuctionMultipane.getPanelNameLabel())
                .and("Clear All AdSizes")
                .clickOnWebElement(privateAuctionMultipane.getClearAllButton())
                .validate(privateAuctionMultipane.getSelectionInfoExcludedLabel().getText(), MultipaneConstants.ALL_AD_SIZES_ARE_INCLUDED_DETAILS.setQuantity())
                .validate(privateAuctionMultipane.countSelectTableItems(), adSizeList.size())
                .validate(privateAuctionMultipane.countIncludedExcludedTableItems(), 0)
                .and(String.format("Search Adsize by Name = 1"))
                .setValueWithClean(privateAuctionMultipane.getSearchInput(), "1")
                .pressEnterKey(privateAuctionMultipane.getSearchInput())
                .and("Include all filtered items")
                .validate(privateAuctionMultipane.countSelectTableItems(), filterdAdSizes.size())
                .clickOnWebElement(privateAuctionMultipane.getIncludeAllButton())
                .testEnd();

        excludeOneByOneItems(filterdAdSizes);
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

    @Step("Include all items one by one")
    private void includeOneByOneItems(List<AdSizesList> list) {
        var privateAuctionMultipane = privateAuctionSidebar.getAdSizeMultipane();
        var updated = new AtomicInteger(0);

        list.forEach(e ->
        {
            var selectedItem = privateAuctionMultipane.getSelectTableItemByPositionInList(updated.get());
            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validate(not(visible), selectedItem.getExcludedIcon())
                    .validate(not(visible), selectedItem.getIncludedIcon())
                    .validate(not(visible), selectedItem.getActiveIcon())
                    .validate(not(visible), selectedItem.getInactiveIcon())
                    .validate(not(visible), selectedItem.getAssociatedWithPublisherIcon())
                    .and()
                    .clickOnWebElement(selectedItem.getIncludeButton())
                    .validate(privateAuctionMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 0) ?
                            MultipaneConstants.ONE_AD_SIZE_IS_INCLUDED.setQuantity(updated.incrementAndGet()) :
                            MultipaneConstants.AD_SIZES_ARE_INCLUDED.setQuantity(updated.incrementAndGet()))
                    .validate(visible, selectedItem.getName())
                    .testEnd();
        });
    }

    @Step("Exclude all items one by one")
    private void excludeOneByOneItems(List<AdSizesList> list) {
        var updated = new AtomicInteger(list.size());
        var privateAuctionMultipane = privateAuctionSidebar.getAdSizeMultipane();

        list.forEach(item -> {
            var selectedItem = privateAuctionMultipane.getIncludedExcludedTableItemByName(item.getSize());

            testStart()
                    .waiter(visible, selectedItem.getName())
                    .hoverMouseOnWebElement(selectedItem.getName())
                    .then()
                    .validateContainsText(privateAuctionMultipane.getSelectionInfoExcludedLabel(), (updated.get() == 1) ?
                            MultipaneConstants.ONE_AD_SIZE_IS_INCLUDED.setQuantity(updated.getAndDecrement()) :
                            MultipaneConstants.AD_SIZES_ARE_INCLUDED.setQuantity(updated.getAndDecrement()))
                    .then()
                    .validate(visible, selectedItem.getRemoveButton())
                    .and(String.format("Remove selected item %s", selectedItem.getName()))
                    .clickOnWebElement(selectedItem.getRemoveButton())
                    .testEnd();
        });
    }

    @AfterMethod(alwaysRun = true)
    private void collapseMultipane(){
        var privateAuctionMultipane = privateAuctionSidebar.getAdSizeMultipane();

        testStart()
                .and("Collapse 'AdSize' multipane")
                .clickOnWebElement(privateAuctionMultipane.getPanelNameLabel())
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