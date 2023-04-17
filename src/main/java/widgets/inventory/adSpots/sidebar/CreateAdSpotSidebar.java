package widgets.inventory.adSpots.sidebar;

import lombok.Getter;
import widgets.inventory.adSpots.sidebar.tabs.AdTagTab;
import widgets.inventory.adSpots.sidebar.tabs.GeneralTab;

/**
 * Keep Selectors of UI elements in {@link AdSpotSideBarElements}
 */
@Getter
public class CreateAdSpotSidebar extends AdSpotSideBar {

    private GeneralTab generalTab = new GeneralTab();
    private AdTagTab adTagTab = new AdTagTab();

}
