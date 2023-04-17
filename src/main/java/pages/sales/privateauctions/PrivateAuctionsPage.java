package pages.sales.privateauctions;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import pages.BasePage;
import widgets.common.table.Table;

import static com.codeborne.selenide.Selenide.$x;

/**
 * Keep Selectors of UI elements in {@link PrivateAuctionsPageElements}
 */
@Getter
public class PrivateAuctionsPage extends BasePage {

    private SelenideElement pageTitle = $x(PrivateAuctionsPageElements.PRIVATE_AUCTIONS_PAGE_TITLE.getSelector()).as(PrivateAuctionsPageElements.PRIVATE_AUCTIONS_PAGE_TITLE.getAlias());
    private SelenideElement createPrivateAuctionsButton = $x(PrivateAuctionsPageElements.CREATE_PRIVATE_AUCTIONS_BUTTON.getSelector()).as(PrivateAuctionsPageElements.CREATE_PRIVATE_AUCTIONS_BUTTON.getAlias());

    private Table table = new Table();
}
