package widgets.sales.privateauctions.sidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PrivateAuctionSidebarElements {

    NAME("'Name' Input", "//label[text()='Name']/../input"),
    TITLE("'Title' Sidebar", "//div[@class='v-toolbar__title']/div"),
    CLOSE_ICON("'Close Private Auction' Icon", "//aside//i[contains(@class,'close')]"),
    DATE_RANGE_INPUT("'Date Range' input","//label[text()='Date Range']/..//input"),
    START_DATE_INPUT("'Date Range' input","//label[text()='Start Date']/..//input"),
    OPTIMIZE_CHECKBOX( "'Optimize'' Checkbox", "//label[text()='Optimize']/..//i"),
    ACTIVE_TOGGLE("'Active' Toggle", "//label[text()='Active']/..//input[@role='switch']"),
    PUBLISHER_ITEMS("'Publisher's Items' Input",
            "//div[contains(@class,'menuable__content__activ')]//div[@class='v-list-item__title']"),
    PUBLISHER_NAME_DROPDOWN("'Publisher Name' Input", "//label[text()='Publisher Name']/../div"),
    ERROR_BY_FILED_NAME("'%s' Field Error", "//label[text()='%s']/../../..//div[@role='alert']"),
    ALWAYS_ON_TOGGLE("'Always On' Toggle", "//label[text()='Always on']/..//input[@role='switch']"),
    PUBLISHER_NAME_INPUT("'Publisher Name' Input", "//label[text()='Publisher Name']/../div/input"),
    SAVE_AND_CLOSE_BUTTON("'Save Private Auction & Close' Button", "//button/span[contains(text(),'Save Private Auction & Close')]"),
    SAVE_AND_CREATE_DEAL_BUTTON("'Save Private Auction & Create Deal", "//button/span[contains(text(),'Save Private Auction & Create Deal')]");

    private String alias;
    private String selector;
}
