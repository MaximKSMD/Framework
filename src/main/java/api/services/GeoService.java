package api.services;

import io.restassured.response.Response;

import java.util.Map;

import static api.core.RakutenExchangeApi.GET_ALL_GEOS;
import static api.core.client.HttpClient.get;
import static api.core.client.HttpClient.initURL;

public class GeoService extends BaseService {

    public Response getAll() {
        URL = initURL(GET_ALL_GEOS);

        return get(URL);
    }

    public Response getGeosWithFilter(Map<String, Object> queryParams) {
        URL = initURL(GET_ALL_GEOS);

        return get(URL, queryParams);
    }
}
