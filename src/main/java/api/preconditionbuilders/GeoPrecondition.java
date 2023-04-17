package api.preconditionbuilders;

import api.core.client.HttpClient;
import api.dto.GenericResponse;
import api.dto.rx.geo.Geo;
import api.services.GeoService;
import configurations.User;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Getter
@AllArgsConstructor
public class GeoPrecondition {

    private Integer responseCode;
    private GenericResponse<Geo> geoGetAllResponse;

    private GeoPrecondition(GeoPreconditionBuilder builder) {
        this.responseCode = builder.responseCode;
        this.geoGetAllResponse = builder.geoGetAllResponse;
    }

    public static GeoPreconditionBuilder geo() {

        return new GeoPreconditionBuilder();
    }

    public static class GeoPreconditionBuilder {

        private Response response;
        private Integer responseCode;
        private GenericResponse<Geo> geoGetAllResponse;

        private final GeoService geoService = new GeoService();

        public GeoPreconditionBuilder getAllGeos() {
            this.response = geoService.getAll();
            this.geoGetAllResponse = this.response.as(new TypeRef<GenericResponse<Geo>>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        public GeoPreconditionBuilder getAllGeosWithFilter(Map<String, Object> queryParams) {
            this.response = geoService.getGeosWithFilter(queryParams);
            this.geoGetAllResponse = this.response.as(new TypeRef<GenericResponse<Geo>>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        public GeoPreconditionBuilder setCredentials(User user) {
            HttpClient.setCredentials(user);

            return this;
        }

        public GeoPrecondition build() {

            return new GeoPrecondition(this);
        }
    }
}
