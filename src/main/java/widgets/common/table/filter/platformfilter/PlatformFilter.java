package widgets.common.table.filter.platformfilter;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import widgets.common.table.filter.abstractt.BaseFilter;

import static com.codeborne.selenide.Selenide.$x;
import static widgets.common.table.filter.platformfilter.PlatformFilterElements.*;


/**
 * Keep Selectors of UI elements in {@link PlatformFilterElements}
 */
@Getter
public class PlatformFilter extends BaseFilter {

    public SelenideElement getPlatformTypeCheckboxByName (String fieldName) {

        return $x(String.format(PLATFORM_CHECKBOX.getSelector(), fieldName))
                .as(String.format(PLATFORM_CHECKBOX.getAlias(), fieldName));
    }

}
