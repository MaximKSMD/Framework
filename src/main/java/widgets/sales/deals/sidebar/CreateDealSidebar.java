package widgets.sales.deals.sidebar;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.datepicker.DatePicker;
import widgets.common.warningbanner.ChangePublisherBanner;

import static com.codeborne.selenide.Selenide.$x;
import static widgets.sales.deals.sidebar.CreateDealSidebarElements.*;

/**
 * Keep Selectors of UI elements in {@link CreateDealSidebarElements}
 */
@Getter
public class CreateDealSidebar extends AbstractDealSidebar {

    private SelenideElement dealIdLabel = $x(DEAL_ID_LABEL.getSelector()).as(DEAL_ID_LABEL.getAlias());
    private SelenideElement copyToClipboardButton = $x(COPY_TO_CLIPBOARD_BUTTON.getSelector()).as(COPY_TO_CLIPBOARD_BUTTON.getAlias());

    private ChangePublisherBanner changePublisherBanner = new ChangePublisherBanner();
    private DatePicker dateRangeField = new DatePicker();

}
