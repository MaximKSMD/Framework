
package widgets.common.menusidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MenuSidebarOptions {

    USER("User"),
    ADMIN("Admin"),
    SALES("Sales"),
    DEALS("Deals"),
    DASHBOARD("Dashboard"),
    PRIVATE_AUCTIONS("Private Auctions");

    private String text;
}
