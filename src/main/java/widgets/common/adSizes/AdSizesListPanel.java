package widgets.common.adSizes;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static widgets.common.adSizes.AdSizesListPanelElements.*;

/**
 * Keep Selectors of UI elements in {@link AdSizesListPanelElements}
 */
@Getter
public class AdSizesListPanel {

    private ElementsCollection adSizesSelectedItems = $$x(AD_SIZES_SELECTED_ITEMS.getSelector())
            .as(AD_SIZES_SELECTED_ITEMS.getAlias());
    @Getter(AccessLevel.NONE)
    private SelenideElement adSizeCheckbox = $x(AD_SIZE_CHECKBOX.getSelector()).as(AD_SIZE_CHECKBOX.getAlias());


    public SelenideElement getAdSizeCheckbox(AdSizesList adSize) {

        return $x(String.format(AD_SIZE_CHECKBOX.getSelector(), adSize.getSize())).as(AD_SIZE_CHECKBOX.getAlias());
    }
}