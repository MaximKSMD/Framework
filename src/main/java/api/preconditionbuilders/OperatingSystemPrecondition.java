package api.preconditionbuilders;

import api.core.client.HttpClient;
import api.dto.GenericResponse;
import api.dto.rx.os.OperatingSystem;
import api.services.OperatingSystemService;
import configurations.User;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public class OperatingSystemPrecondition {

    private Integer responseCode;
    private GenericResponse<OperatingSystem> operatingSystemGetAllResponse;

    private OperatingSystemPrecondition(OperatingSystemPreconditionBuilder builder) {
        this.responseCode = builder.responseCode;
        this.operatingSystemGetAllResponse = builder.operatingSystemGetAllResponse;
    }

    public static OperatingSystemPreconditionBuilder operatingSystem() {

        return new OperatingSystemPreconditionBuilder();
    }

    public static class OperatingSystemPreconditionBuilder {

        private Response response;
        private Integer responseCode;
        private GenericResponse<OperatingSystem> operatingSystemGetAllResponse;
        private OperatingSystemService operatingSystemService = new OperatingSystemService();



        public OperatingSystemPreconditionBuilder getOperatingSystemList() {
            this.response = operatingSystemService.getAll();

            this.responseCode = response.getStatusCode();
            this.operatingSystemGetAllResponse = this.response.as(new TypeRef<GenericResponse<OperatingSystem>>() {});

            return this;
        }

        public OperatingSystemPreconditionBuilder setCredentials(User user) {
            HttpClient.setCredentials(user);

            return this;
        }

        public OperatingSystemPrecondition build() {

            return new OperatingSystemPrecondition(this);
        }
    }
}
