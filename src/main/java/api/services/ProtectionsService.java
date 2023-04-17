package api.services;

import api.dto.rx.protection.ProtectionRequest;
import io.restassured.response.Response;

import java.util.Map;

import static api.core.RakutenExchangeApi.*;
import static api.core.client.HttpClient.*;

public class ProtectionsService extends BaseService {

    public Response createProtection(ProtectionRequest body) {
        URL = initURL(CREATE_PROTECTION);

        return post(URL, body.toJson());
    }

    public Response deleteProtection(Integer id) {
        URL = initURL(DELETE_PROTECTION.setParameters(id));

        return delete(URL);
    }

    public Response getAll() {
        URL = initURL(GET_ALL_PROTECTIONS);

        return get(URL);
    }

    public Response getProtectionsWithFilter(Map<String, Object> queryParams) {
        URL = initURL(GET_ALL_PROTECTIONS);

        return get(URL, queryParams);
    }

}
