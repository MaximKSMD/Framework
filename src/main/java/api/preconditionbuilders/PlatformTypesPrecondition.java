package api.preconditionbuilders;

import api.core.client.HttpClient;
import api.dto.GenericResponse;
import api.dto.rx.adsize.AdSize;
import api.dto.rx.platformtype.PlatformType;
import api.services.AdSizeService;
import api.services.PlatformTypeService;
import configurations.User;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public class PlatformTypesPrecondition {

    private Integer responseCode;
    private GenericResponse<PlatformType> platformTypesGetAllResponse;

    private PlatformTypesPrecondition(PlatformTypePreconditionBuilder builder) {
        this.responseCode = builder.responseCode;
        this.platformTypesGetAllResponse = builder.platformTypesGetAllResponse;
    }

    public static PlatformTypePreconditionBuilder platformType() {

        return new PlatformTypePreconditionBuilder();
    }

    public static class PlatformTypePreconditionBuilder {

        private Response response;
        private Integer responseCode;
        private GenericResponse<PlatformType> platformTypesGetAllResponse;

        private final PlatformTypeService platformTypeService = new PlatformTypeService();

        public PlatformTypePreconditionBuilder getAllPlatformsList() {
            this.response = platformTypeService.getAll();
            this.platformTypesGetAllResponse = this.response.as(new TypeRef<GenericResponse<PlatformType>>() {});
            this.responseCode = response.getStatusCode();

            return this;
        }

        public PlatformTypePreconditionBuilder setCredentials(User user) {
            HttpClient.setCredentials(user);

            return this;
        }

        public PlatformTypesPrecondition build() {

            return new PlatformTypesPrecondition(this);
        }
    }
}
