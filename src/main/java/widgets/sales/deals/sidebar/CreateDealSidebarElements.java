
package widgets.sales.deals.sidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CreateDealSidebarElements {

    DEAL_ID_LABEL("'Deal ID' Label", "//span[contains(text(),'Copy to Clipboard')]/../.."),
    COPY_TO_CLIPBOARD_BUTTON("'Copy To Clipboard' button", "//span[contains(text(),'Copy to Clipboard')]/../../button");

    private String alias;
    private String selector;
}
