package api.dto.rx.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CountryCode {

    US("US"),
    UA("UA");

    private String code;
}
