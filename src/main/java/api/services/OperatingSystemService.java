package api.services;

import io.restassured.response.Response;

import static api.core.RakutenExchangeApi.GET_ALL_OPERATING_SYSTEM;
import static api.core.client.HttpClient.get;
import static api.core.client.HttpClient.initURL;

public class OperatingSystemService extends BaseService {

    public Response getAll() {
        URL = initURL(GET_ALL_OPERATING_SYSTEM);

        return get(URL);
    }
}
