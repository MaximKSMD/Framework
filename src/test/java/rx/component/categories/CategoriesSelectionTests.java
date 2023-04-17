package rx.component.categories;

import api.dto.rx.admin.publisher.Publisher;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;

import rx.BaseTest;
import widgets.common.categories.CategoriesList;
import widgets.common.table.ColumnNames;
import widgets.inventory.media.sidebar.EditMediaSidebar;
import widgets.inventory.media.sidebar.PlatformType;
import pages.inventory.media.*;

import java.util.List;

import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.*;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;
import static zutils.FakerUtils.captionWithSuffix;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Components")
public class CategoriesSelectionTests extends BaseTest {

    private MediaPage mediaPage;
    private EditMediaSidebar editMediaSidebar;
    Publisher publisher;


    public CategoriesSelectionTests() {
        mediaPage = new MediaPage();
        editMediaSidebar = new EditMediaSidebar();
    }

    @BeforeClass
    private void initAndLogin() {

        publisher = publisher()
                .createNewPublisher(captionWithSuffix("0000001autoPubCategories"))
                .build()
                .getPublisherResponse();


        testStart()
                .given()
                .openDirectPath(Path.MEDIA)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, mediaPage.getNuxtProgress())
                .testEnd();

    }

    @Test(description = "Create Media with Categories")
    private void createMedia() {
        var mediaName = captionWithSuffix("autoMedia");
        var tableData = mediaPage.getMediaTable().getTableData();
        var tablePagination = mediaPage.getMediaTable().getTablePagination();
        var categories = editMediaSidebar.getCategoriesPanel();

        testStart()
                .clickOnWebElement(mediaPage.getCreateMediaButton())
                .waitSideBarOpened()
                .selectFromDropdown(editMediaSidebar.getPublisherInput(),
                        editMediaSidebar.getPublisherDropdownItems(), publisher.getName())
                .and("Fill Name")
                .setValueWithClean(editMediaSidebar.getNameInput(), mediaName)
                .selectFromDropdown(editMediaSidebar.getPlatformDropdown(),
                        editMediaSidebar.getPlatformDropdownItems(), PlatformType.MOBILE_WEB.getName())

                .setValueWithClean(editMediaSidebar.getSiteURL(), "http://testCategory.yy")
                .clickOnWebElement(editMediaSidebar.getCategories())
                .clickOnWebElement(categories.getCategoryCheckbox(CategoriesList.EDUCATION))
                .clickOnWebElement(categories.getCategoryGroupIcon(CategoriesList.AUTOMOTIVE))
                .clickOnWebElement(categories.getCategoryCheckbox(CategoriesList.AUTO_REPAIR))

                .clickOnWebElement(editMediaSidebar.getSaveButton())
                .and("Error message is absent")
                .waitAndValidate(not(visible), editMediaSidebar.getErrorAlert().getErrorPanel())
                .waitAndValidate(not(visible), mediaPage.getToasterMessage().getPanelError())
                .waitSideBarClosed()
                .and("Toaster Error message is absent")
                .waitAndValidate(not(visible), mediaPage.getToasterMessage().getPanelError())
                .and("Search new media")
                .setValueWithClean(tableData.getSearch(), mediaName)
                .pressEnterKey(tableData.getSearch())
                .then("Validate that text in table footer '1-1 of 1")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .and("Open Sidebar and check data")
                .clickOnTableCellLink(tableData, ColumnNames.MEDIA_NAME, mediaName)
                .waitSideBarOpened()
                .then("Check all fields")
                .validate(editMediaSidebar.getPublisherInput(), publisher.getName())
                .validateAttribute(editMediaSidebar.getNameInput(), "value", mediaName)
                .validate(editMediaSidebar.getPlatformDropdown(), PlatformType.MOBILE_WEB.getName())
                .validateList(categories.getCategoriesSelectedItems(), List.of(CategoriesList.AUTO_REPAIR.getName(),
                        CategoriesList.EDUCATION.getName()))
                .clickOnWebElement(editMediaSidebar.getCloseIcon())
                .waitSideBarClosed()
                .testEnd();
    }

    @AfterClass(alwaysRun = true)
    private void deletePublisher(){

        testStart()
                .logOut()
                .testEnd();


        publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(publisher.getId())
                .build();
    }
}
