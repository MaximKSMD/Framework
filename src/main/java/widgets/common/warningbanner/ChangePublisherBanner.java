package widgets.common.warningbanner;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$x;

import static widgets.common.warningbanner.ChangePublisherBannerElements.*;


/**
 * Keep Selectors of UI elements in {@link ChangePublisherBannerElements}
 */
@Getter
public class ChangePublisherBanner {

    private SelenideElement bannerText = $x(BANNER_TEXT.getSelector()).as(BANNER_TEXT.getAlias());
    private SelenideElement activeCheckBox = $x(WARNING_ICON.getSelector()).as(WARNING_ICON.getAlias());
    private SelenideElement cancelButton = $x(CANCEL_BUTTON.getSelector()).as(CANCEL_BUTTON.getAlias());
    private SelenideElement acceptButton = $x(ACCEPT_BUTTON.getSelector()).as(ACCEPT_BUTTON.getAlias());

    public SelenideElement getWarningIcon(String fieldName){

        return $x(String.format(WARNING_ICON.getSelector(),fieldName))
                .as(String.format(WARNING_ICON.getAlias(),fieldName));
    }

    public SelenideElement getCancelButton(String fieldName){

        return $x(String.format(CANCEL_BUTTON.getSelector(),fieldName))
                .as(String.format(CANCEL_BUTTON.getAlias(),fieldName));
    }

    public SelenideElement getAcceptButton(String fieldName){

        return $x(String.format(ACCEPT_BUTTON.getSelector(),fieldName))
                .as(String.format(ACCEPT_BUTTON.getAlias(),fieldName));
    }

    public SelenideElement getBannerText(String fieldName){

        return $x(String.format(BANNER_TEXT.getSelector(),fieldName))
                .as(String.format(BANNER_TEXT.getAlias(),fieldName));
    }
}
