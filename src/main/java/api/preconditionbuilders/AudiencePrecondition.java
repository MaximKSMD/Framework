package api.preconditionbuilders;

import api.core.client.HttpClient;
import api.dto.GenericResponse;
import api.dto.rx.audience.Audience;
import api.dto.rx.audience.AudienceRequest;
import api.services.AudienceService;
import configurations.User;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import zutils.FakerUtils;


@Slf4j
@Getter
@AllArgsConstructor
public class AudiencePrecondition {

    private Integer responseCode;
    private GenericResponse<Audience> audienceGetAllResponse;

    private AudiencePrecondition(AudiencePreconditionBuilder builder) {
        this.responseCode = builder.responseCode;
        this.audienceGetAllResponse = builder.audienceGetAllResponse;
    }

    public static AudiencePreconditionBuilder audience() {

        return new AudiencePreconditionBuilder();
    }

    public static class AudiencePreconditionBuilder {

        private Response response;
        private Integer responseCode;
        private GenericResponse<Audience> audienceGetAllResponse;

        private final AudienceService audienceService = new AudienceService();

        public AudiencePreconditionBuilder getAll() {
            this.response = audienceService.getAll();
            this.audienceGetAllResponse = this.response.as(new TypeRef<GenericResponse<Audience>>() {
            });
            this.responseCode = response.getStatusCode();

            return this;
        }

        public AudiencePreconditionBuilder getAudienceById(int id) {
            this.response = audienceService.getAudienceById(id);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public AudiencePreconditionBuilder createAudience() {
            var audience =
                    this.getAudienceBody("Audience", FakerUtils.notes(), "viber", "segmentId");

            this.response = audienceService.createAudience(audience);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public AudiencePreconditionBuilder updateAudience(Audience audienceRequest) {
            this.response = audienceService.updateAudience(audienceRequest);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public AudiencePreconditionBuilder deleteAudience(int id) {
            this.response = audienceService.deleteAudience(id);
            this.responseCode = response.getStatusCode();

            return this;
        }

        private AudienceRequest getAudienceBody(String name, String description, String publisherCode, String segmentId) {

            return AudienceRequest.builder()
                    .name(FakerUtils.captionWithSuffix(String.format("%s_auto", name)))
                    .description(description)
                    .publisherCode(publisherCode)
                    .segmentId(segmentId)
                    .build();
        }


        public AudiencePreconditionBuilder setCredentials(User user) {
            HttpClient.setCredentials(user);

            return this;
        }

        public AudiencePrecondition build() {

            return new AudiencePrecondition(this);
        }
    }
}
