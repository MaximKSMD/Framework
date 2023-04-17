package api.services;

import api.dto.rx.audience.Audience;
import api.dto.rx.audience.AudienceRequest;
import io.restassured.response.Response;

import static api.core.RakutenExchangeApi.*;
import static api.core.client.HttpClient.*;

public class AudienceService extends BaseService{

    public Response getAll() {
        URL = initURL(GET_ALL_AUDIENCES);

        return get(URL);
    }

    public Response getAudienceById(int id) {
        URL = initURL(GET_AUDIENCE_BY_ID.setParameters(id));

        return get(URL);
    }

    public Response createAudience(AudienceRequest body) {
        URL = initURL(CREATE_AUDIENCE);

        return post(URL, body.toJson());
    }

    public Response updateAudience(Audience audience) {
        URL = initURL(UPDATE_AUDIENCE.setParameters(audience.getId()));

        return put(URL, audience.toJson());
    }

    public Response deleteAudience(int id) {
        URL = initURL(DELETE_AUDIENCE.setParameters(id));

        return delete(URL);
    }
}
