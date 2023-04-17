package widgets.common.validationalert;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static widgets.common.validationalert.ValidationBottomAlertElements.*;

/**
 * Keep Selectors of UI elements in {@link ValidationBottomAlertElements}
 */
@Getter
public class ValidationBottomAlert {

    private final SelenideElement iconError = $x(ERROR_ICON.getSelector()).as(ERROR_ICON.getAlias());
    private final SelenideElement errorPanel = $x(ERROR_PANEL.getSelector()).as(ERROR_PANEL.getAlias());
    private final SelenideElement headerError = $x(HEADER_ERROR.getSelector()).as(HEADER_ERROR.getAlias());
    private final ElementsCollection errorsList = $$x(ERRORS_LIST.getSelector()).as(ERRORS_LIST.getAlias());
}
