package widgets.admin.publisher.tableitem;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PublisherTableItemElements {

    STATE( "'State' Label", "/td[5]"),
    DOMAIN( "'Domain' Label", "/td[6]"),
    ID("'Publisher id' Label", "/td[2]"),
    CHECKBOX( "'CheckBox' Label", "/td[1]"),
    MAIL("'Publisher Mail' Label", "/td[9]"),
    NAME( "'Publisher Name' Title", "/td[3]/a"),
    CATEGORY( "'Ad Ops Person' Label", "/td[4]"),
    CURRENCY("'Publisher Currency' Label", "/td[7]"),
    AD_OPS_PERSON( "'Ad Ops Person' Label", "/td[8]");

    private String alias;
    private String selector;
}
