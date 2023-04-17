package widgets.admin.publisher.tableitem;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;
import static widgets.admin.publisher.tableitem.PublisherTableItemElements.*;

/**
 * Keep Selectors of UI elements in {@link PublisherTableItemElements}
 */
@Getter
public class PublisherTableItem {

    private int position;

    private SelenideElement publisherId;
    private SelenideElement publisherMail;
    private SelenideElement publisherName;
    private SelenideElement publisherState;
    private SelenideElement publisherAdOps;
    private SelenideElement publisherDomain;
    private SelenideElement publisherCategory;
    private SelenideElement publisherCurrency;
    private SelenideElement publisherCheckBox;

    private static final String PUBLISHER_ITEM = "tbody/tr";
    ////descendant::tbody/tr[2]/td[9]

    public PublisherTableItem(int position) {
        this.position = position;
        this.publisherId =  $x(buildXpath(ID.getSelector())).as(ID.getAlias());
        this.publisherName = $x(buildXpath(NAME.getSelector())).as(NAME.getAlias());
        this.publisherMail = $x(buildXpath(MAIL.getSelector())).as(MAIL.getAlias());
        this.publisherState = $x(buildXpath(STATE.getSelector())).as(STATE.getAlias());
        this.publisherDomain = $x(buildXpath(DOMAIN.getSelector())).as(DOMAIN.getAlias());
        this.publisherCheckBox = $x(buildXpath(CHECKBOX.getSelector())).as(CHECKBOX.getAlias());
        this.publisherCategory = $x(buildXpath(CATEGORY.getSelector())).as(CATEGORY.getAlias());
        this.publisherCurrency = $x(buildXpath(CURRENCY.getSelector())).as(CURRENCY.getAlias());
        this.publisherAdOps =  $x(buildXpath(AD_OPS_PERSON.getSelector())).as(AD_OPS_PERSON.getAlias());
    }

    public String buildXpath(String elementXpath) {

        return (format("//descendant::%s[%s]%s", PUBLISHER_ITEM, position, elementXpath));
    }
}
