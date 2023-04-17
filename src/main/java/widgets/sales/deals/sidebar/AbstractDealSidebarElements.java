package widgets.sales.deals.sidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AbstractDealSidebarElements {

    DSP_DROPDOWN("'Dsp' Dropdown","//label[text()='DSP']/../div"),
    TITLE("'Title' Sidebar", "//div[@class='v-toolbar__title']/div"),
    DSP_INPUT("'Dsp' Dropdown","//label[text()='DSP']/../div/input"),
    NAME( "'Publisher Name' Input", "//label[text()='Name']/../input"),
    SAVE_DEAL_BUTTON("'Save Deal' button", "//span[text()='Save Deal']"),
    BUYERS_CARD_ITEMS("'Buyers Card' Items","//div[@class='buyers-cards']/div"),
    CURRENCY_DROPDOWN( "'Currency' Dropdown", "//label[text()='Currency']/../div"),
    CURRENCY_INPUT( "'Currency' Input", "//label[text()='Currency']/../div/input"),
    FLOOR_PRICE_INPUT("'Floor Price' Input",  "//label[text()='Floor Price']/../input"),
    PUBLISHER_DROPDOWN("'Publisher' dropdown", "//label[text()='Publisher Name']/../div"),
    ACTIVE_TOGGLE("'Active' Toggle", "//label[text()='Active']/..//input[@role='switch']"),
    CLOSE_SIDEBAR_BUTTON( "'Close Sidebar' button", "//aside//i[contains(@class,'close')]"),
    ERROR_BY_FILED_NAME("'%s' Field Error", "//label[text()='%s']/../../..//div[@role='alert']"),
    ALWAYS_ON_TOGGLE("'Always On' Toggle", "//label[text()='Always on']/..//input[@role='switch']"),
    PUBLISHER_NAME_INPUT("'Publisher Name' Input", "//label[text()='Publisher Name']/../div/input"),
    ADD_MORE_SEATS_BUTTON("'Add More Seats' Button", "//span[contains(text(),'Add More Seats')]/.."),
    PRIVATE_AUCTION_DROPDOWN("'Private Auction' Dropdown", "//label[text()='Private Auction']/../div"),
    PRIVATE_AUCTION_INPUT("'Private Auction' Input", "//label[text()='Private Auction']/../div[@class='v-select__selections']/div"),

    DROPDOWN_ITEMS("Dropdown Items", "//div[contains(@class,'menuable__content__activ')]//div[@class='v-list-item__title']");

    private String alias;
    private String selector;
}
