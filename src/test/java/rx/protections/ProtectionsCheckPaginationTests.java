package rx.protections;

import api.dto.rx.protection.Protection;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import pages.Path;
import pages.protections.ProtectionsPage;
import rx.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static api.preconditionbuilders.ProtectionsPrecondition.protection;
import static api.preconditionbuilders.PublisherPrecondition.publisher;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static configurations.User.TEST_USER;
import static configurations.User.USER_FOR_DELETION;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Epic("Waiting for separate QA env")
@Link("https://rakutenadvertising.atlassian.net/browse/GS-3280")
@Feature(value = "Protections")
public class ProtectionsCheckPaginationTests extends BaseTest {

    private ProtectionsPage protectionsPage;
    private int totalProtections;
    private List<Protection> listProtections = new ArrayList<>();

    public ProtectionsCheckPaginationTests() {
        protectionsPage = new ProtectionsPage();
    }

    @BeforeClass
    private void init() {
        if (getTotalProtections() < 200) generateProtection();

        totalProtections = getTotalProtections();
    }

    @BeforeMethod
    private void login() {
        testStart()
                .given()
                .openDirectPath(Path.PROTECTIONS)
                .logIn(TEST_USER)
                .waitAndValidate(disappear, protectionsPage.getNuxtProgress())
                .testEnd();
    }

    @Test(description = "Verify Pagination: 100 rows per page")
    public void checkPagination100() {
        verifyPagination(100);
    }

    @Test(description = "Verify Pagination: 50 rows per page")
    public void checkPagination50() {
        verifyPagination(50);
    }

    @Test(description = "Verify Pagination: 25 rows per page")
    public void checkPagination25() {
        verifyPagination(25);
    }

    @Test(description = "Verify Pagination: 20 rows per page")
    public void checkPagination20() {
        verifyPagination(20);
    }

    @Test(description = "Verify Pagination: 15 rows per page")
    public void checkPagination15() {
        verifyPagination(15);
    }

    @Test(description = "Verify Pagination: 10 rows per page")
    public void checkPagination10() {
        verifyPagination(10);
    }

    @Step("Verify pagination {0}")
    private void verifyPagination(Integer rowsPerPage) {
        var tablePagination = protectionsPage.getProtectionsTable().getTablePagination();
        var tableData = protectionsPage.getProtectionsTable().getTableData();
        //Todo Add checking of total qauntity in pagination test when
        // https://rakutenadvertising.atlassian.net/browse/GS-3280 will be ready
        testStart()
                .and(String.format("Select %s rows per page", rowsPerPage))
                .scrollIntoView(tablePagination.getPageMenu())
                .selectFromDropdown(tablePagination.getPageMenu(),
                        tablePagination.getRowNumbersList(),
                        rowsPerPage.toString())
                .waitLoading(visible, protectionsPage.getTableProgressBar())
                .waitLoading(disappear, protectionsPage.getTableProgressBar())
                .then(String.format("Validate that text in table footer '1-%s of", rowsPerPage))
                .validateContainsText(tablePagination.getPaginationPanel(),
                        String.format(String.format("1-%s of", rowsPerPage)))
                .then(String.format("Rows in table page equals %s", rowsPerPage))
                .validateListSize(tableData.getRows(), rowsPerPage)
                .and("Click on Next page").scrollIntoView(tablePagination.getNext())
                .clickOnWebElement(tablePagination.getNext())
                .then(String.format("Validate that text in table footer '%s-%s of",
                        rowsPerPage + 1, Math.min(rowsPerPage * 2, totalProtections)))
                .validateContainsText(tablePagination.getPaginationPanel(), String.format("%s-%s of", rowsPerPage + 1,
                        Math.min(rowsPerPage * 2, totalProtections)))
                .then(String.format("Rows in table page equals %s", rowsPerPage))
                .validateListSize(tableData.getRows(), rowsPerPage).and("Click on Previous page")
                .scrollIntoView(tablePagination.getPrevious()).clickOnWebElement(tablePagination.getPrevious())
                .then(String.format("Validate that text in table footer '1-%s of", rowsPerPage))
                .validateContainsText(tablePagination.getPaginationPanel(), String.format("1-%s of", rowsPerPage))
                .then(String.format("Rows in table page equals %s", rowsPerPage))
                .validateListSize(tableData.getRows(), rowsPerPage)
                .testEnd();
    }

    @AfterMethod(alwaysRun = true)
    private void logout(){
        testStart()
                .and("Logout")
                .logOut()
                .testEnd();
    }

    @AfterClass(alwaysRun = true)
    private void deleteEntities() {
        if (listProtections != null) {
            for (Protection protection : listProtections) {
                deleteProtection(protection.getId());
                deletePublisher(protection.getPublisherId());
            }
        }
    }

    private void deleteProtection(Integer id) {
        protection()
                .setCredentials(USER_FOR_DELETION)
                .deleteProtection(id)
                .build();
    }

    private void deletePublisher(Integer id) {
        publisher()
                .setCredentials(USER_FOR_DELETION)
                .deletePublisher(id)
                .build();
    }

    private int getTotalProtections() {

        return protection()
                .getAllProtectionsList()
                .build()
                .getProtectionsGetAllResponse()
                .getTotal();
    }

    private void generateProtection() {

        while (getTotalProtections() < 221) {
            Protection protection = protection()
                    .createNewRandomProtection()
                    .build()
                    .getProtectionsResponse();

            listProtections.add(protection);
        }
    }
}
