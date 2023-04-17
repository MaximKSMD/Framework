package widgets.inventory.adSpots.sidebar.tabs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GeneralTabElements {

    FILTER("'Filter' Input", "//label[text()='Filter']/../input"),
    POSITION("'Position' Button", "//label[text()='Position']/../div/div"),
    CATEGORIES("'Categories' Element", "//label[text()='Categories']/../div[1]"),
    POSITION_INPUT("'Position' Input", "//label[text()='Position']/../div/input"),
    //TODO: child does not work , need to investigate GS-3029
    ERROR_ALERT_BY_FIELD_NAME("Error Alert under Field '%s'",
            "//label[text()='%s']/../../../div[2]/div/div/div"),
    AD_SPOT_NAME("'Ad Spot Name' Input", "//label[text()='Name']/../input"),
    PUBLISHER_NAME("'Publisher Name' Input", "//label[text()='Publisher Name']/../div"),
    CATEGORIES_INPUT("'Categories' Input", "//label[text()='Categories']/../div/input"),
    RELATED_MEDIA("'Related Media' Icon", "//label[text()='Related Media']/../div/div"),
    TOOLTIP_DEFAULT_FLOOR_PRICE_ICON("Tooltip Default Floor Price Icon",
            "//*[text()='Default Floor Price']/../../div/*[contains(@class,'tooltip')]/../i"),
    ACTIVE_TOGGLE("'Active' Toggle", "//label[text()='Active']/..//input[@role='switch']"),
    TEST_MODE("'Test Mode' Toggle", "//label[text()='Test Mode']/..//input[@role='switch']"),
    PUBLISHER_ITEMS("'Publisher's Items' Input",
            "//div[contains(@class,'menuable__content__activ')]//div[@class='v-list-item__title']"),
    RELATED_MEDIA_ITEMS("'Related Media Items' Input",
            "//div[contains(@class,'menuable__content__activ')]//div[@class='v-list-item__title']"),
    RELATED_MEDIA_INPUT("'Related Media' Input", "//label[text()='Related Media']/../div/input"),
    DEFAULT_AD_SIZES("'Default Ad Sizes' Button", "//label[text()='Default Ad Sizes']/../div[1]"),
    DEFAULT_AD_SIZES_INPUT("'Default Ad Sizes' Input", "//label[text()='Default Ad Sizes']/../div/input"),
    PUBLISHER_NAME_INPUT("'Publisher Name' Input", "//label[text()='Publisher Name']/../div/input"),
    DEFAULT_FLOOR_PRICE("'Default Floor Price' Input", "//label[text()='Default Floor Price']/../input"),
    TOOLTIP_CONTENT_FOR_CHILDREN_ICON("Tooltip Content for Children  Icon", "//*[text()='Content for Children']/../i"),
    DEFAULT_FLOOR_PRICE_CURRENCY("'Default Floor Price Currency' Label", "//label[text()='Default Floor Price']/../div"),
    TOOLTIP_CATEGORIES_ICON("Tooltip Categories Icon", "//*[text()='Categories']/../div/*[contains(@class,'tooltip')]/../i"),
    POSITION_ITEMS("'Position' Items", "//div[contains(@class,'menuable__content__activ')]//div[@class='v-list-item__title']"),
    CONTENT_FOR_CHILDREN("'Content For Children' Toggle", "//span[text()='Content for Children']/../../div/input[@role='switch']"),
    TOOLTIP_DEFAULT_AD_SIZES_ICON("Tooltip Default Ad Sizes Icon", "//*[text()='Default Ad Sizes']/../div/span[contains(@class,'tooltip')]/../i");

    private String alias;
    private String selector;
}
