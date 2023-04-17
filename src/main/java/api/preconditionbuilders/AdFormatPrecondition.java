package api.preconditionbuilders;

import api.core.client.HttpClient;
import api.dto.GenericResponse;
import api.dto.rx.adformat.AdFormat;
import api.services.AdFormatService;
import configurations.User;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public class AdFormatPrecondition {

    private Integer responseCode;
    private GenericResponse<AdFormat> adFormatGetAllResponse;

    private AdFormatPrecondition(AdFormatPreconditionBuilder builder) {
        this.responseCode = builder.responseCode;
        this.adFormatGetAllResponse = builder.adFormatGetAllResponses;
    }

    public static AdFormatPreconditionBuilder adFormat() {

        return new AdFormatPreconditionBuilder();
    }

    public static class AdFormatPreconditionBuilder {

        private Response response;
        private Integer responseCode;
        private GenericResponse<AdFormat> adFormatGetAllResponses;

        private final AdFormatService adFormatService = new AdFormatService();

        public AdFormatPreconditionBuilder getAllAdFormatsList() {
            this.response = adFormatService.getAll();
            this.adFormatGetAllResponses = this.response.as(new TypeRef<GenericResponse<AdFormat>>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        public AdFormatPreconditionBuilder setCredentials(User user) {
            HttpClient.setCredentials(user);

            return this;
        }

        public AdFormatPrecondition build() {

            return new AdFormatPrecondition(this);
        }
    }
}
