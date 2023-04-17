package widgets.inventory.adSpots.sidebar.adtagcard.video;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdSpotVideoAdTagCardElements {

    VIDEO_AD_TAG_CARD("'Video Ad Tag' Card", "//h3[text()='Video Ad Tag']/.."),
    AD_SERVER_CHECKBOX_BY_CAPTION("'%s' Ad Server Checkbox",
            "//h4[contains(text(),'Ad Server')]/..//label[normalize-space()='%s']/../div/i"),
    REQUIRED_PARAMETER_CHIP_BY_CAPTION("'%s' Required Parameter Chip",
            "//h4[contains(text(),'Required Parameters')]/..//span[contains(text(),'%s')]/.."),
    OPTIONAL_PARAMETERS_CHIP_BY_CAPTION("'%s' Optional Parameters Chip",
            "//h4[contains(text(),'Optional Parameters')]/..//span[contains(text(),'%s')]/.."),
    AD_TAG_TEXT_AREA("'Ad Tag' Text Area", "//h4[contains(text(),'Ad Tag')]/..//textarea"),
    RECOMMENDED_PARAMETERS_CHIP_BY_CAPTION("'%s' Recommended Parameters Chip",
            "//h4[contains(text(),'Recommended Parameters')]/..//span[contains(text(),'%s')]/.."),
    VIDEO_AD_TAG_PANEL("'Video Ad Tag' Panel with Elements", "//h3[text()='Video Ad Tag']/../..");

    private String alias;
    private String selector;
}
