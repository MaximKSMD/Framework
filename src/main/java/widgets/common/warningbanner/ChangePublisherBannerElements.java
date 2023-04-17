
package widgets.common.warningbanner;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChangePublisherBannerElements {

    WARNING_ICON("Warning Icon", "//div[contains(text(),'%s')]/../div[@class='v-avatar v-banner__icon']"),
    CANCEL_BUTTON("Cancel Button","//div[contains(text(),'%s')]//../.." +
          "/div[@class='v-banner__actions']//span[contains(text(),'CANCEL')]/.."),
    ACCEPT_BUTTON("Accept Button","//div[contains(text(),'%s')]/../.." +
         "/div[@class='v-banner__actions']//span[contains(text(),'ACCEPT')]/.."),
    BANNER_TEXT("Banner Text","//div[contains(text(),'%s')]");

    private String alias;
    private String selector;
}
