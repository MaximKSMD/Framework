package widgets.protections.protectiontypemultipane;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProtectionTypeMultipaneElements {

    INCLUDE_SUBDOMAINS_WHERE_APPLICABLE_CHECK_BOX(
            "'Include subdomains (where applicalble)' CheckBox in Multipane",
            "//label[text()='Include subdomains (where applicalble)']/..//input"),
    TARGET_AWAY_FROM_THE_FOLLOWING_ADVERTISER_BLOCK_RADIO_BUTTON(
            "'Target away from the following advertiser (block)' Radiobutton in Multipane",
            "//label[text()='Target away from the following advertiser (block)']/..//input"),
    TARGET_TOWARDS_THE_FOLLOWING_ADVERTISER_WHITELIST_RADIO_BUTTON(
            "'Target towards the following advertiser (whitelist)' Radiobutton in Multipane",
            "//label[text()='Target towards the following advertiser (whitelist)']/..//input"),
    EXCLUDE_REQUESTS_WHERE_THE_ADVERTISER_IS_NOT_A_DOMAIN_CHECK_BOX(
            "'Exclude requests where the advertiser is not a domain' CheckBox in Multipane",
            "//label[text()='Exclude requests where the advertiser is not a domain']/..//input");

    private String alias;
    private String selector;
}
