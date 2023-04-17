package api.preconditionbuilders;

import api.core.client.HttpClient;
import api.dto.rx.version.VersionDto;
import api.services.VersionService;
import configurations.User;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public class VersionPrecondition {

    private Integer responseCode;
    private VersionDto versionResponse;

    private VersionPrecondition(VersionPreconditionBuilder builder) {
        this.responseCode = builder.responseCode;
        this.versionResponse = builder.versionResponse;
    }

    public static VersionPreconditionBuilder version() {

        return new VersionPreconditionBuilder();
    }

    public static class VersionPreconditionBuilder {

        private Response response;
        private Integer responseCode;
        private VersionDto versionResponse;

        private final VersionService versionService = new VersionService();

        public VersionPreconditionBuilder getAPIVersion() {
            this.response = versionService.getAPIVersion();
            this.versionResponse = this.response.as(VersionDto.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public VersionPreconditionBuilder setCredentials(User user) {
            HttpClient.setCredentials(user);

            return this;
        }

        public VersionPrecondition build() {

            return new VersionPrecondition(this);
        }
    }
}
