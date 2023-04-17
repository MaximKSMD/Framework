package api.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthApi {

    GET_TOKEN("/v2/login");

    private String endpoint;

}
