package widgets.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ToasterMessageElements {

    ERROR_ICON("Error Icon", "//*[@class='toast-wrapper']/i"),
    ERROR_PANEL("Error Panel", "//*[@class='v-snack__content']"),
    ERROR_STATUS("'Error' Status", "//*[@class='toast-wrapper']/div[@class='content']/div[1]/h3"),
    REMOVE_ICON("Remove Icon", "//*[@class='toast-wrapper']/div[@class='content']/div[1]/a[@class='remove']"),
    ERROR_MESSAGE("'Error' Message", "//*[@class='toast-wrapper']/div[@class='content']/div[@class='hidden']"),
    VIEW_ERROR_DETAILS("'View Error Details Link", "//*[@class='toast-wrapper']/div[@class='content']/div[1]/a[@class='more-button']");

    private String alias;
    private String selector;
}
