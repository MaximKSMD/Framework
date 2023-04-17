package widgets.admin.publisher.sidebar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CurrencyType {

    RUB("RUB - Rubles"),
    USD("USD - Dollars"),
    JPY("JPY - Yen"),
    EUR("EUR - Euro");

    private String type;
}
