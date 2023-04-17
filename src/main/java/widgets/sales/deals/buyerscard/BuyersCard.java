package widgets.sales.deals.buyerscard;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;
import static widgets.sales.deals.buyerscard.BuyersCardElements.*;


/**
 * Keep Selectors of UI elements in {@link BuyersCardElements}
 */
@Getter
public class BuyersCard {

    private int position;

    private SelenideElement activeCheckBox;
    private SelenideElement deleteCardIcon;
    private SelenideElement dspSeatIdCombo;
    private SelenideElement dspSeatNameInput;
    private SelenideElement advertiserIdInput;
    private SelenideElement advertiserNameInput;
    private SelenideElement expandCollapseButton;
    private SelenideElement dspSeatPassthroughStringInput;
    private SelenideElement dspDomainAdvertiserPassthroughStringInput;

    private ElementsCollection autocompleteItems;

    private static final String BUYER_CARD = "div[@class='buyers-cards']/div";
    ////descendant:://div[@class='buyers-cards']/div[2]/{{elementXpath}}

    public BuyersCard(int position) {
        this.position = position;

        this.deleteCardIcon = $x(buildXpath(DELETE_CARD_ICON.getSelector())).as(DELETE_CARD_ICON.getAlias());
        this.activeCheckBox =  $x(buildXpath(ACTIVE_CHECK_BOX.getSelector())).as(ACTIVE_CHECK_BOX.getAlias());
        this.dspSeatIdCombo =  $x(buildXpath(DSP_SEAT_ID_COMBO.getSelector())).as(DSP_SEAT_ID_COMBO.getAlias());
        this.dspSeatNameInput = $x(buildXpath(DSP_SEAT_NAME_INPUT.getSelector())).as(DSP_SEAT_NAME_INPUT.getAlias());
        this.advertiserIdInput = $x(buildXpath(ADVERTISER_ID_INPUT.getSelector())).as(ADVERTISER_ID_INPUT.getAlias());
        this.advertiserNameInput = $x(buildXpath(ADVERTISER_NAME_INPUT.getSelector())).as(ADVERTISER_NAME_INPUT.getAlias());
        this.expandCollapseButton = $x(buildXpath(EXPAND_COLLAPSE_BUTTON.getSelector())).as(EXPAND_COLLAPSE_BUTTON.getAlias());
        this.dspSeatPassthroughStringInput = $x(buildXpath(DSP_SEAT_PASSTHROUGH_STRING_INPUT.getSelector()))
                .as(DSP_SEAT_PASSTHROUGH_STRING_INPUT.getAlias());
        this.dspDomainAdvertiserPassthroughStringInput = $x(buildXpath(DSP_DOMAIN_ADVERTISER_PASSTHROUGHT_STRING_INPUT.getSelector()))
                .as(DSP_DOMAIN_ADVERTISER_PASSTHROUGHT_STRING_INPUT.getAlias());

        this.autocompleteItems = $$x(AUTOCOMPLETE_ITEMS.getSelector()).as(AUTOCOMPLETE_ITEMS.getAlias());
    }

    public String buildXpath(String elementXpath) {

        return (format("//descendant::%s[%s]%s", BUYER_CARD, position, elementXpath));
    }
}
