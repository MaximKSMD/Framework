package rx.admin.publishers;

import widgets.common.table.ColumnNames;
import zutils.FakerUtils;
import api.dto.rx.admin.publisher.Publisher;
import api.preconditionbuilders.PublisherPrecondition;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import pages.admin.publisher.PublishersPage;
import pages.dashbord.DashboardPage;
import rx.BaseTest;
import widgets.admin.publisher.sidebar.CreatePublisherSidebar;
import widgets.admin.publisher.sidebar.EditPublisherSidebar;

import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static java.lang.String.valueOf;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature("Publishers")
public class PublisherTest extends BaseTest {

    private Publisher publisher;
    private DashboardPage dashboardsPage;
    private PublishersPage publishersPage;
    private EditPublisherSidebar editPublisherSidebar;
    private CreatePublisherSidebar createPublisherSidebar;

    private static final String PUBLISHER_NAME_EDITED = FakerUtils.captionWithSuffix("Pub_Auto_Edited");
    private static final String PUBLISHER_AD_OPS_EDITED = FakerUtils.captionWithSuffix("Ad_Ops_Edited");

    public PublisherTest() {
        dashboardsPage = new DashboardPage();
        publishersPage = new PublishersPage();
        editPublisherSidebar = new EditPublisherSidebar();
        createPublisherSidebar = new CreatePublisherSidebar();
    }

    @BeforeClass
    public void createNewPublisher() {
        //Creating publisher to edit Using API
        publisher = PublisherPrecondition.publisher()
                .createNewPublisher()
                .build()
                .getPublisherResponse();
    }

    @Test
    public void editPublisherTest() {
        var tableData = publishersPage.getTable().getTableData();
        var tablePagination = publishersPage.getTable().getTablePagination();

        //Opening Browser and Edit the publisher created from Precondition
        testStart()
                .given()
                .openDirectPath(Path.PUBLISHER)
                .logIn(TEST_USER)
                .validate(visible, dashboardsPage.getLogo())
                .validate(TEST_USER.getMail())
                .waitAndValidate(disappear, dashboardsPage.getNuxtProgress())
                .and()
                .waitAndValidate(disappear, dashboardsPage.getTableProgressBar())
                .and(String.format("Search publisher %s", publisher.getName()))
                .setValueWithClean(tableData.getSearch(), publisher.getName())
                .pressEnterKey(tableData.getSearch())
                .then("Validate that text in table footer '1-1 of 1")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .and("Open Sidebar and check data")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, publisher.getName())
                .waitSideBarOpened()
                .then()
                .validate(publisher.getName()
                        , publisher.getMail()
                        , publisher.getCurrency())
                .validateAttribute(createPublisherSidebar.getCurrency(), "disabled", "true")
                .and()
                .setValueWithClean(createPublisherSidebar.getNameInput(), PUBLISHER_NAME_EDITED)
                .setValueWithClean(createPublisherSidebar.getAdOpsPerson(), PUBLISHER_AD_OPS_EDITED)
                .clickOnWebElement(createPublisherSidebar.getSaveButton())
                .waitSideBarClosed()
                .then()
                .setValueWithClean(tableData.getSearch(), PUBLISHER_NAME_EDITED)
                .pressEnterKey(tableData.getSearch())
                .then("Validate that text in table footer '1-1 of 1")
                .validateContainsText(tablePagination.getPaginationPanel(), "1-1 of 1")
                .validate(tableData.getCellByRowValue(ColumnNames.AD_OPS_PERSON, ColumnNames.NAME, PUBLISHER_NAME_EDITED), PUBLISHER_AD_OPS_EDITED)
                .validate(tableData.getCellByRowValue(ColumnNames.ID, ColumnNames.NAME, PUBLISHER_NAME_EDITED), valueOf(publisher.getId()))
                .and("Open Sidebar and check data")
                .clickOnTableCellLink(tableData, ColumnNames.NAME, PUBLISHER_NAME_EDITED)
                .waitSideBarOpened()
                .then()
                .validateAttribute(editPublisherSidebar.getNameInput(), "value", PUBLISHER_NAME_EDITED)
                .validateAttribute(editPublisherSidebar.getAdOpsPerson(), "value", PUBLISHER_AD_OPS_EDITED)
                .testEnd();
    }

    @AfterClass
    private void deletePublisher(){
        if (publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(publisher.getId())
                .build()
                .getResponseCode() == HttpStatus.SC_NO_CONTENT)
            log.info(String.format("Deleted publisher %s",publisher.getId()));
    }
}
