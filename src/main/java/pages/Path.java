package pages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Path {

    //Pages
    MEDIA("/media"),
    AD_SPOT("/adspots"),
    PROFILE("/profile"),
    USER("/admin/users"),
    DEALS("/sales/deals"),
    DASHBOARD("/dashboard"),
    DEMAND("/admin/demand"),
    PROTECTIONS("/protections"),
    PUBLISHER("/admin/publishers"),
    OPEN_PRICING("/yield/open-pricing"),
    PRIVATE_AUCTIONS("/sales/private-auctions"),

    //Sidebars
    CREATE_USER("/admin/users/create"),
    CREATE_PROTECTION("/protections/create"),
    CREATE_OPEN_PRICING("/yield/open-pricing/create"),
    CREATE_PRIVATE_AUCTION("/sales/private-auctions/create");

    private String path;
}
