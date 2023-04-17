package widgets.common.table.filter.rolefilter;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.table.filter.abstractt.BaseFilter;

import static com.codeborne.selenide.Selenide.$x;


/**
 * Keep Selectors of UI elements in {@link RoleFilterElements}
 */
@Getter
public class RoleFilter extends BaseFilter {

    private SelenideElement adminPublisherCheckBox = $x(RoleFilterElements.ADMIN_ROLE_CHECkBOX.getSelector())
            .as(RoleFilterElements.ADMIN_ROLE_CHECkBOX.getAlias());
    private SelenideElement crossPublisherCheckBox = $x(RoleFilterElements.CROSS_PUBLISHER_ROLE_CHECkBOX.getSelector())
            .as(RoleFilterElements.CROSS_PUBLISHER_ROLE_CHECkBOX.getAlias());
    private SelenideElement singlePublisherCheckBox = $x(RoleFilterElements.SINGLE_PUBLISHER_ROLE_CHECkBOX.getSelector())
            .as(RoleFilterElements.SINGLE_PUBLISHER_ROLE_CHECkBOX.getAlias());
}
