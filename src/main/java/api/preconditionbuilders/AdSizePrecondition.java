package api.preconditionbuilders;

import api.core.client.HttpClient;
import api.dto.GenericResponse;
import api.dto.rx.adsize.AdSize;
import api.services.AdSizeService;
import configurations.User;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public class AdSizePrecondition {

    private Integer responseCode;
    private GenericResponse<AdSize> adSizesGetAllResponse;

    private AdSizePrecondition(AdSizePreconditionBuilder builder) {
        this.responseCode = builder.responseCode;
        this.adSizesGetAllResponse = builder.adSizesGetAllResponse;
    }

    public static AdSizePreconditionBuilder adSize() {

        return new AdSizePreconditionBuilder();
    }

    public static class AdSizePreconditionBuilder {

        private Response response;
        private Integer responseCode;
        private GenericResponse<AdSize> adSizesGetAllResponse;

        private final AdSizeService adSizeService = new AdSizeService();

        public AdSizePreconditionBuilder getAllAdSizesList() {
            this.response = adSizeService.getAll();
            this.adSizesGetAllResponse = this.response.as(new TypeRef<GenericResponse<AdSize>>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        public AdSizePreconditionBuilder setCredentials(User user) {
            HttpClient.setCredentials(user);

            return this;
        }

        public AdSizePrecondition build() {

            return new AdSizePrecondition(this);
        }
    }
}
