package widgets.inventory.adSpots.sidebar.tabs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdTagTabElements {

    //Todo Elements will be added in scope of task https://rakutenadvertising.atlassian.net/browse/GS-3719
    PLACEHOLDER("", "");

    private String alias;
    private String selector;
}
