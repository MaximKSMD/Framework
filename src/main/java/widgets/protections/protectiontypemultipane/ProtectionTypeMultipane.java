package widgets.protections.protectiontypemultipane;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.multipane.Multipane;

import static com.codeborne.selenide.Selenide.$x;
import static widgets.protections.protectiontypemultipane.ProtectionTypeMultipaneElements.*;


/**
 * Keep Selectors of UI elements in {@link ProtectionTypeMultipaneElements}
 */
@Getter
public class ProtectionTypeMultipane extends Multipane {

    private SelenideElement includeSubdomainsWhereApplicalbleCheckBox;
    private SelenideElement targetAwayFromTheFollowingAdvertiserBlockCheckBox;
    private SelenideElement targetTowardsTheFollowingAdvertiserWhitelistRadioButton;
    private SelenideElement excludeRequestsWhereTheAdvertiserIsNotaDomainCheckBox;

    public ProtectionTypeMultipane(ProtectionTypeNameImpl protectionTypeName){
        super(protectionTypeName);

        this.includeSubdomainsWhereApplicalbleCheckBox =
                $x(buildXpath(INCLUDE_SUBDOMAINS_WHERE_APPLICABLE_CHECK_BOX.getSelector()))
                        .as(INCLUDE_SUBDOMAINS_WHERE_APPLICABLE_CHECK_BOX.getAlias());
        this.targetAwayFromTheFollowingAdvertiserBlockCheckBox =
                $x(buildXpath(TARGET_AWAY_FROM_THE_FOLLOWING_ADVERTISER_BLOCK_RADIO_BUTTON.getSelector()))
                        .as(TARGET_AWAY_FROM_THE_FOLLOWING_ADVERTISER_BLOCK_RADIO_BUTTON.getAlias());
        this.targetTowardsTheFollowingAdvertiserWhitelistRadioButton =
                $x(buildXpath(TARGET_TOWARDS_THE_FOLLOWING_ADVERTISER_WHITELIST_RADIO_BUTTON.getSelector()))
                        .as(TARGET_TOWARDS_THE_FOLLOWING_ADVERTISER_WHITELIST_RADIO_BUTTON.getAlias());
        this.excludeRequestsWhereTheAdvertiserIsNotaDomainCheckBox =
                $x(buildXpath(EXCLUDE_REQUESTS_WHERE_THE_ADVERTISER_IS_NOT_A_DOMAIN_CHECK_BOX.getSelector()))
                        .as(EXCLUDE_REQUESTS_WHERE_THE_ADVERTISER_IS_NOT_A_DOMAIN_CHECK_BOX.getAlias());
    }
}