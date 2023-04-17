package widgets.inventory.adSpots.sidebar;

import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.Getter;
import widgets.inventory.adSpots.sidebar.tabs.GeneralTab;

import static com.codeborne.selenide.Selenide.$x;
import static widgets.inventory.adSpots.sidebar.AdSpotSideBarElements.*;

/**
 * Keep Selectors of UI elements in {@link AdSpotSideBarElements}
 */
@Getter
public abstract class AdSpotSideBar {

    private SelenideElement closeIcon = $x(CLOSE_ICON.getSelector()).as(CLOSE_ICON.getAlias());
    private SelenideElement saveButton = $x(SAVE_AD_SPOT_BUTTON.getSelector()).as(SAVE_AD_SPOT_BUTTON.getAlias());
    @Getter(AccessLevel.NONE)
    private SelenideElement tabHeaderByCaption = $x(TAB_HEADER_BY_CAPTION.getSelector()).as(TAB_HEADER_BY_CAPTION.getAlias());
    private SelenideElement adSpotSideBarTitle = $x(AD_SPOT_SIDEBAR_TITLE.getSelector()).as(AD_SPOT_SIDEBAR_TITLE.getAlias());
    private SelenideElement saveAndViewAdSpotButton = $x(SAVE_AND_VIEW_AD_TAG_BUTTON.getSelector()).as(SAVE_AND_VIEW_AD_TAG_BUTTON.getAlias());

    private GeneralTab generalTab = new GeneralTab();

    public SelenideElement getTabHeaderByCaption(String tabHeader) {

        return $x(String.format(TAB_HEADER_BY_CAPTION.getSelector(), tabHeader))
                .as(String.format(TAB_HEADER_BY_CAPTION.getAlias(), tabHeader));
    }
}
