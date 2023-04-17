package widgets.common.table;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum ColumnNames {
    //General for all tables
    ID("ID", true),
    ACTIVE("Active", true),
    DETAILS("Details", true),
    PUBLISHER("Publisher", true),
    CREATED_BY("Created By", true),
    UPDATED_BY("Updated By", true),
    UPDATED_DATE("Updated Date", true),
    CREATED_DATE("Created Date", true),
    NAME("Name", true),
    EXTERNAL_ID("External ID", true),
    STATUS("Status", true),
    END_DATE("End Date", true),
    ALWAYS_ON("Always On", true),
    START_DATE("Start Date", true),

    //Demand Source
    BIDDER("Bidder", true),
    DSP_ID("DSP ID", false),


    //Publisher
    MAIL("Mail", true),
    DOMAIN("Domain", true),
    CURRENCY("Currency", true),
    CATEGORY("Category", true),
    AD_OPS_PERSON("Ad Ops Person", true),


    //Media
    PLATFORM("Platform", true),
    MEDIA_NAME("Name", true),
    SITE_APP_STORE_URL("Site / App Store URL", true),

    //Ad Spot Name
    AD_SPOT_NAME("Name", true),
    RELATED_MEDIA("Related Media", true),

    FILTER("Filter", false),
    TEST_MODE("Test Mode", false),
    DEFAULT_SIZES("Default Sizes", false),
    PAGE_CATEGORY("Page Category", false),
    DEFAULT_FLOOR_PRICE("Default Floor Price", true),

    //Private Auction
    OPTIMIZE("Optimize", false),

    //Protections
    MANAGED_BY_SYSTEM_ADMIN("Managed by system admin", false),

    //Users
    ROLE("Role", false),
    EMAIL("Email", true),

    //Deals
    DSP("DSP", false),
    PRICE_VALUE("Price | Value", true),
    PRIVATE_AUCTION("Private Auction", true),
    PRICE_CURRENCY("Price | Currency", true),

    //Open Pricing
    FLOOR_PRICE("Floor Price", true);


    private String name;
    private Boolean sortable;
}
