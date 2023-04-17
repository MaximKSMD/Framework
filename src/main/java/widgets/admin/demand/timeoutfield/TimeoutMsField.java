package widgets.admin.demand.timeoutfield;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$x;
import static widgets.admin.demand.timeoutfield.TimeoutMsFieldElements.*;


/**
 * Keep Selectors of UI elements in {@link TimeoutMsFieldElements}
 */
@Getter
public class TimeoutMsField {

    private SelenideElement timeoutMsFieldInput = $x(TIMEOUT_MS_INPUT.getSelector()).as(TIMEOUT_MS_INPUT.getAlias());
    private SelenideElement timeoutMsFieldSuffix = $x(TIMEOUT_MS_SUFFIX.getSelector()).as(TIMEOUT_MS_SUFFIX.getAlias());
    private SelenideElement timeoutMsUpButton = $x(TIMEOUT_MS_UP_BUTTON.getSelector()).as(TIMEOUT_MS_UP_BUTTON.getAlias());
    private SelenideElement timeoutMsDownButton = $x(TIMEOUT_MS_DOWN_BUTTON.getSelector()).as(TIMEOUT_MS_DOWN_BUTTON.getAlias());

}
