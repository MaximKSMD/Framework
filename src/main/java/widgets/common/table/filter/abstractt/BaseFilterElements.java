
package widgets.common.table.filter.abstractt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseFilterElements {

    BACK_BUTTON("'Back' button in '%s' Filter","//div[@role='menu']//i[contains(@class,'back')]"),
    CANCEL_BUTTON("'Cancel' button in in '%s' Filter","//div[@role='menu']//span[text()='Cancel']/.."),
    SUBMIT_BUTTON("'Submit' button in in '%s","//div[@role='menu']//span[text()='Submit']/.."),
    FILTER_HEADER("'Filter Header Label in '%s' Filter", "//div[@role='menu']//div[contains(@class,'filter-title')]");

    private String alias;
    private String selector;
}
