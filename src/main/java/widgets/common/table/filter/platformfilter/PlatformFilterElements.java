
package widgets.common.table.filter.platformfilter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlatformFilterElements {

    PLATFORM_CHECKBOX("'%s' platform checkbox in 'Platform' Filter",
            "//div[@class='v-list-item__title' and normalize-space()='%s']/../..//i");
    private String alias;
    private String selector;
}
