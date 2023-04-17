package widgets.common.table.filter.singlepanefilter.item;

import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;

import static widgets.common.table.filter.singlepanefilter.SinglepaneFilterElements.FILTER_ITEMS;
import static widgets.common.table.filter.singlepanefilter.item.SinglepaneItemElements.*;

/**
 * Keep Selectors of UI elements in {@link SinglepaneItemElements}
 */
@Getter
public class SinglepaneItem {

    private SelenideElement name;
    private SelenideElement includedIcon;
    private SelenideElement includeButton;

    @Getter(AccessLevel.NONE)
    private int position;
    @Getter(AccessLevel.NONE)
    protected String singlepaneItem;

    public SinglepaneItem(int position) {

        this.position = position;
        this.name = $x(buildXpath(format(NAME.getSelector(), 2))).as(NAME.getAlias());
        this.includedIcon = $x(buildXpath(format(INCLUDED_ICON.getSelector(), 3))).as(INCLUDED_ICON.getAlias());
        this.includeButton = $x(buildXpath(format(INCLUDE_BUTTON.getSelector(), 3))).as(INCLUDE_BUTTON.getAlias());
    }

    protected String buildXpath(String elementXpath) {

        return format("%s[%s]%s",
                FILTER_ITEMS.getSelector(),
                position,
                elementXpath);
    }
}
