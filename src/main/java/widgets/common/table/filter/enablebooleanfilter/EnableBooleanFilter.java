package widgets.common.table.filter.enablebooleanfilter;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.table.filter.abstractt.BaseFilter;

import static com.codeborne.selenide.Selenide.$x;


/**
 * Keep Selectors of UI elements in {@link EnableBooleanFilterElements}
 */
@Getter
public class EnableBooleanFilter extends BaseFilter {

    private SelenideElement enabledRadioButton = $x(EnableBooleanFilterElements.ENABLED_RADIO_BUTTON.getSelector())
            .as(EnableBooleanFilterElements.ENABLED_RADIO_BUTTON.getAlias());
    private SelenideElement disabledRadioButton = $x(EnableBooleanFilterElements.DISABLED_RADIO_BUTTON.getSelector())
            .as(EnableBooleanFilterElements.DISABLED_RADIO_BUTTON.getAlias());

}
