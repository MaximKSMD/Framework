package pages.admin.demand;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DemandPageElements {

    DEMAND_PAGE_TITLE( "'Demand' Page Title", "//h1");

    private String alias;
    private String selector;
}
