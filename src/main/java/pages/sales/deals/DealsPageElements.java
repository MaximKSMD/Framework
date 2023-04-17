package pages.sales.deals;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum DealsPageElements {

    DEAL_ITEMS("'Deal' Items", "//tbody/tr"),
    DEAL_PAGE_TITLE( "'Deals' Page Title", "//h1"),
    CREATE_DEAL_BUTTON("'Create Deal' Button", "//button//span[text()='Create Deal']"),
    ACTIVATE_DEAL_BUTTON("'Activate Deal' Button", "//button//span[text()='Activate Deal']"),
    DEACTIVATE_DEAL_BUTTON("'Deactivate Deal' Button", "//button//span[text()='Deactivate Deal']");

    private String alias;
    private String selector;
}
