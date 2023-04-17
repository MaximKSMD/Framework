package api.services;

import io.restassured.response.Response;

import static api.core.RakutenExchangeApi.GET_API_VERSION;
import static api.core.client.HttpClient.get;
import static api.core.client.HttpClient.initURL;

public class VersionService extends BaseService {

    public Response getAPIVersion() {
        URL = initURL(GET_API_VERSION);

        return get(URL);
    }
}
