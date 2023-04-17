package widgets.common.table.filter.activebooleanfilter;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.table.filter.abstractt.BaseFilter;

import static com.codeborne.selenide.Selenide.$x;


/**
 * Keep Selectors of UI elements in {@link ActiveBooleanFilterElements}
 */
@Getter
public class ActiveBooleanFilter extends BaseFilter {

    private SelenideElement activeRadioButton = $x(ActiveBooleanFilterElements.ACTIVE_RADIO_BUTTON.getSelector())
            .as(ActiveBooleanFilterElements.ACTIVE_RADIO_BUTTON.getAlias());
    private SelenideElement inactiveRadioButton = $x(ActiveBooleanFilterElements.INACTIVE_RADIO_BUTTON.getSelector())
            .as(ActiveBooleanFilterElements.INACTIVE_RADIO_BUTTON.getAlias());

}
