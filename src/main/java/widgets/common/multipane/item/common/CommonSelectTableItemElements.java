package widgets.common.multipane.item.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonSelectTableItemElements {

    ACTIVE_ICON("'Active' Icon of row %s in table list in Multipane", "/tr/td//i[@title='Active']"),
    INACTIVE_ICON("'Inactive' Icon of row %s in table list in Multipane", "/tr/td//i[@title='Inactive']"),
    EXCLUDED_ICON("'Excluded' Icon of row %s in table list in Multipane","/tr/td[contains(@class,'excluded')]"),
    INCLUDED_ICON("'Included' Icon of row %s in table list in Multipane","/tr/td[contains(@class,'included')]"),
    EXCLUDE_BUTTON("'Exclude' Button of row %s in table list in Multipane","//td/div[contains(@class,'exclude')]/button[2]"),
    INCLUDE_BUTTON("'Include' Button of row %s in table list in Multipane","//td/div[contains(@class,'include')]/button[2]"),
    NOT_ASSOCIATED_WITH_PUBLISHER_ICON("'Associated with publisher' icon of row %s in table list in Multipane", "/tr/td//i[contains(@title,'Not associated']");

    private String alias;
    private String selector;
}
