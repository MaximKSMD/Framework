package widgets.common;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$x;
import static widgets.common.ToasterMessageElements.*;
import static widgets.common.validationalert.ValidationBottomAlertElements.ERROR_ICON;


/**
 * Keep Selectors of UI elements in {@link ToasterMessageElements}
 */
@Getter
public class ToasterMessage {

    private final SelenideElement iconError = $x(ERROR_ICON.getSelector()).as(ERROR_ICON.getAlias());
    private final SelenideElement panelError = $x(ERROR_PANEL.getSelector()).as(ERROR_PANEL.getAlias());
    private final SelenideElement removeIcon = $x(REMOVE_ICON.getSelector()).as(REMOVE_ICON.getAlias());
    private final SelenideElement messageStatus = $x(ERROR_STATUS.getSelector()).as(ERROR_STATUS.getAlias());
    private final SelenideElement messageError = $x(ERROR_MESSAGE.getSelector()).as(ERROR_MESSAGE.getAlias());
    private final SelenideElement viewErrorDetails = $x(VIEW_ERROR_DETAILS.getSelector()).as(VIEW_ERROR_DETAILS.getAlias());
}
