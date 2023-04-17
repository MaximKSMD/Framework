package api.services;

import io.restassured.response.Response;

import static api.core.RakutenExchangeApi.GET_ALL_PLATFORM_TYPES;
import static api.core.client.HttpClient.get;
import static api.core.client.HttpClient.initURL;

public class PlatformTypeService extends BaseService {

    public Response getAll() {
        URL = initURL(GET_ALL_PLATFORM_TYPES);

        return get(URL);
    }
}
