package widgets.common.multipane.item.common;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.multipane.MultipaneName;
import widgets.common.multipane.item.abstractt.MultipaneItem;

import static com.codeborne.selenide.Selenide.$x;
import static java.lang.String.format;
import static widgets.common.multipane.item.common.CommonSelectTableItemElements.*;

/**
 * Keep Selectors of UI elements in {@link CommonSelectTableItemElements}
 * 'activeIcon' , 'inactiveIcon', 'associatedWithPublisherIcon' fields are
 * available only for DEMAND_SOURCES Multipane
 */
@Getter
public abstract class CommonSelectTableItem extends MultipaneItem {

    private SelenideElement activeIcon;
    private SelenideElement inactiveIcon;
    private SelenideElement excludedIcon;
    private SelenideElement includedIcon;
    private SelenideElement excludeButton;
    private SelenideElement includeButton;

    public CommonSelectTableItem(int position, String tableItem, MultipaneName multipaneName) {
        super(position, tableItem, multipaneName);

        this.activeIcon = $x(buildXpath(ACTIVE_ICON.getSelector())).as(format("%s%s", ACTIVE_ICON.getAlias(), position));
        this.inactiveIcon = $x(buildXpath(INACTIVE_ICON.getSelector())).as(format("%s%s", INACTIVE_ICON.getAlias(), position));
        this.excludedIcon = $x(buildXpath(EXCLUDED_ICON.getSelector())).as(format("%s%s", EXCLUDED_ICON.getAlias(), position));
        this.includedIcon = $x(buildXpath(INCLUDED_ICON.getSelector())).as(format("%s%s", INCLUDED_ICON.getAlias(), position));
        this.excludeButton = $x(buildXpath(EXCLUDE_BUTTON.getSelector())).as(format("%s%s", EXCLUDE_BUTTON.getAlias(), position));
        this.includeButton = $x(buildXpath(INCLUDE_BUTTON.getSelector())).as(format("%s%s", INCLUDE_BUTTON.getAlias(), position));
    }
}
