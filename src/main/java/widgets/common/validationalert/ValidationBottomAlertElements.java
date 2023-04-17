package widgets.common.validationalert;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValidationBottomAlertElements {

    ERROR_PANEL( "Error Panel", "//div[@class='v-alert__wrapper']"),
    ERROR_ICON( "Icon Error", "//div[@class='v-alert__wrapper']/i"),
    HEADER_ERROR( "Header Error", "//div[@class='v-alert__wrapper']/div[@class='v-alert__content']/div"),
    ERRORS_LIST( "Errors List", "//div[@class='v-alert__wrapper']/div[@class='v-alert__content']/div/ul/li");

    private String alias;
    private String selector;
}
