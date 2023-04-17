package api.dto.rx.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Currency {

    EUR("EUR - Euro","EUR", "€"),
    JPY("JPY - Yen","JPY", "¥"),
    RUB("RUB - Rubles", "RUB", "RUB"),
    USD("USD - Dollars", "USD", "$");

    private String fullName;
    private String alias;
    private String abbr;
}
