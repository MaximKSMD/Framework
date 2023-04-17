package rx.component.multipane;

import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Path;
import rx.BaseTest;
import widgets.common.multipane.Multipane;
import widgets.common.multipane.MultipaneNameImpl;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static configurations.User.TEST_USER;
import static managers.TestManager.testStart;

@Slf4j
@Listeners({ScreenShooter.class})
@Feature(value = "Components")
public class MultipaneTest extends BaseTest {

    private Multipane deviceMultipane;

    public static final String ONE_DEVICE_INCLUDED = "1 device is included";
    public static final String ALL_DEVICES_INCLUDED = "7 devices are included";

    //Todo remove it after
    private String publisherDropdown = "//label[text()='Publisher Name']/../div";
    private String publisherDropdownItems = "//div[contains(@class,'menuable__content__activ')]//div[@class='v-list-item__title']";


    public MultipaneTest() {
        deviceMultipane = new Multipane(MultipaneNameImpl.DEVICE);
    }

    @BeforeClass
    @Step("Getting info about 'Viber' publisher and logging in SSP")
    public void getPublisherInfo() {
        //publisher = getPublisher();

        testStart()
                .given()
                .openDirectPath(Path.CREATE_PROTECTION)
                .logIn(TEST_USER)
                .and()
                .waitSideBarOpened()
                .testEnd();
    }

    @Test
    @Step("Expand Multipane Component for Device")
    public void expandMultipaneTest() {

        //Expand Device Multipane
        testStart()
                .given()
                .waitSideBarOpened()
                .and()
                .waitAndValidate(appear, $x(publisherDropdown))
                .selectFromDropdownByPosition($x(publisherDropdown), $$x(publisherDropdownItems), 1)
                .and()
                .clickOnWebElement(deviceMultipane.getPanelNameLabel())
                .testEnd();
    }

    @Test(dependsOnMethods = "expandMultipaneTest")
    @Step("Select Device from Multipane Component")
    public void deviceSelectionTest() {
        var firstDeviceToSelect = deviceMultipane.getSelectTableItemByPositionInList(1);

        //Check Device Selection
        testStart()
                .waiter(visible, firstDeviceToSelect.getName())
                .and()
                .hoverMouseOnWebElement(firstDeviceToSelect.getName())
                .then()
                .validate(not(visible), firstDeviceToSelect.getExcludedIcon())
                .validate(not(visible), firstDeviceToSelect.getIncludedIcon())
                .validate(not(visible), firstDeviceToSelect.getActiveIcon())
                .validate(not(visible), firstDeviceToSelect.getInactiveIcon())
                .validate(not(visible), firstDeviceToSelect.getAssociatedWithPublisherIcon())
                .validate(visible, firstDeviceToSelect.getIncludeButton())
                .validate(visible, firstDeviceToSelect.getExcludeButton())
                .and()
                .clickOnWebElement(firstDeviceToSelect.getIncludeButton())
                .then()
                .validate(visible, firstDeviceToSelect.getIncludedIcon())
                .validate(not(visible), firstDeviceToSelect.getExcludedIcon())
                .testEnd();
    }

    @Test(dependsOnMethods = "deviceSelectionTest")
    @Step("Select Device from Multipane Component")
    public void checkIncludedItemTest() {
        var firstIncludedDevice = deviceMultipane.getIncludedExcludedTableItemByPositionInList(0);

        //Check Device Selection
        testStart()
                .and()
                .hoverMouseOnWebElement(firstIncludedDevice.getName())
                .then()
                .validate(not(visible), firstIncludedDevice.getParentLabel())
                .validate(visible, firstIncludedDevice.getRemoveButton())
                //Todo add more checks with icons
                .testEnd();
    }

    @Test(dependsOnMethods = "checkIncludedItemTest")
    @Step("Select Device from Multipane Component")
    public void checkSelectionInfoTextTest() {

        //Check Selection Info Text
        testStart()
                .then()
                .validate(visible, deviceMultipane.getSelectionInfoLabel())
                .validateContainsText(deviceMultipane.getSelectionInfoLabel(), ONE_DEVICE_INCLUDED)
                .and()
                .clickOnWebElement(deviceMultipane.getIncludeAllButton())
                .then()
                .validateContainsText(deviceMultipane.getSelectionInfoLabel(), ALL_DEVICES_INCLUDED)
                .testEnd();
    }
}
