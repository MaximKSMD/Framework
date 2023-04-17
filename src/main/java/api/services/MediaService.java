package api.services;

import api.dto.rx.admin.publisher.Publisher;
import api.dto.rx.inventory.media.Media;
import api.dto.rx.inventory.media.MediaRequest;
import io.restassured.response.Response;

import java.util.Map;

import static api.core.RakutenExchangeApi.*;
import static api.core.client.HttpClient.*;

public class MediaService extends BaseService {

    public Response getMediaById(int id) {
        URL = initURL(GET_MEDIA.setParameters(id));

        return get(URL);
    }

    public Response createMedia(MediaRequest body) {
        URL = initURL(CREATE_MEDIA);

        return post(URL, body.toJson());
    }

    public Response getAll() {
        URL = initURL(GET_ALL_MEDIA);

        return get(URL);
    }

    public Response getMediaWithFilter(Map<String, Object> queryParams) {
        URL = initURL(GET_ALL_MEDIA);

        return get(URL, queryParams);
    }

    public Response deleteMedia(int id) {
        URL = initURL(DELETE_MEDIA.setParameters(id));

        return delete(URL);
    }

    public Response updateMedia(Media media) {
        URL = initURL(UPDATE_MEDIA.setParameters(media.getId()));

        return put(URL, media.toJson());
    }

}
