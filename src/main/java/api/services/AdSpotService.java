package api.services;

import api.dto.rx.inventory.adspot.AdSpotRequest;
import io.restassured.response.Response;

import java.util.Map;

import static api.core.RakutenExchangeApi.*;
import static api.core.client.HttpClient.*;

public class AdSpotService extends BaseService {
    public Response createAdSpot(AdSpotRequest body) {
        URL = initURL(CREATE_ADSPOT);

        return post(URL, body.toJson());
    }

    public Response updateAdSpot(AdSpotRequest body) {
        URL = initURL(UPDATE_ADSPOT);

        return put(URL, body.toJson());
    }

    public Response getAll() {
        URL = initURL(GET_ALL_ADSPOTS);

        return get(URL);
    }

    public Response deleteAdSpot(int id) {
        URL = initURL(DELETE_ADSPOT.setParameters(id));

        return delete(URL);
    }

    public Response getAdSpotsWithFilter(Map<String, Object> queryParams) {
        URL = initURL(GET_ALL_ADSPOTS);

        return get(URL,queryParams);
    }
}
