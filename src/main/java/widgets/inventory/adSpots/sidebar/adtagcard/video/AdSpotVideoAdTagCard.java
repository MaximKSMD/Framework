package widgets.inventory.adSpots.sidebar.adtagcard.video;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.inventory.adSpots.sidebar.adtagcard.video.enums.AdServerCheckBox;
import widgets.inventory.adSpots.sidebar.adtagcard.video.enums.OptionalParameter;
import widgets.inventory.adSpots.sidebar.adtagcard.video.enums.RequiredParameter;

import static com.codeborne.selenide.Selenide.$x;
import static widgets.inventory.adSpots.sidebar.adtagcard.video.AdSpotVideoAdTagCardElements.*;

/**
 * Keep Selectors of UI elements in {@link AdSpotVideoAdTagCardElements}
 */
@Getter
public class AdSpotVideoAdTagCard {

    private SelenideElement adTagTextArea = $x(AD_TAG_TEXT_AREA.getSelector())
            .as(AD_TAG_TEXT_AREA.getAlias());
    private SelenideElement videoAdTagCard = $x(VIDEO_AD_TAG_CARD.getSelector())
            .as(VIDEO_AD_TAG_CARD.getAlias());
    private SelenideElement videAdTagPanel = $x(VIDEO_AD_TAG_PANEL.getSelector())
            .as(VIDEO_AD_TAG_PANEL.getAlias());

    public SelenideElement getAdServerCheckboxByCaption(AdServerCheckBox checkBox){

        return $x(String.format(AD_SERVER_CHECKBOX_BY_CAPTION.getSelector(), checkBox.getName()))
                .as(String.format(OPTIONAL_PARAMETERS_CHIP_BY_CAPTION.getAlias(), checkBox.getName()));
    }

    public SelenideElement getRequiredParameterChipByCaption(RequiredParameter parameter){

        return $x(String.format(REQUIRED_PARAMETER_CHIP_BY_CAPTION.getSelector(), parameter.getName()))
                .as(String.format(REQUIRED_PARAMETER_CHIP_BY_CAPTION.getAlias(), parameter.getName()));
    }

    public SelenideElement getOptionalParameterChipByCaption(OptionalParameter parameter){

        return $x(String.format(OPTIONAL_PARAMETERS_CHIP_BY_CAPTION.getSelector(), parameter.getName()))
                .as(String.format(OPTIONAL_PARAMETERS_CHIP_BY_CAPTION.getAlias(), parameter.getName()));
    }

    public SelenideElement getRecommendedParameterChipByCaption(OptionalParameter parameter){

        return $x(String.format(RECOMMENDED_PARAMETERS_CHIP_BY_CAPTION.getSelector(), parameter.getName()))
                .as(String.format(RECOMMENDED_PARAMETERS_CHIP_BY_CAPTION.getAlias(), parameter.getName()));
    }
}
