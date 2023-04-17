package api.preconditionbuilders;

import api.core.client.HttpClient;
import api.dto.GenericResponse;
import api.dto.rx.common.CountryCode;
import api.dto.rx.common.Currency;
import api.dto.rx.demandsource.BidEndpoint;
import api.dto.rx.demandsource.DemandSource;
import api.services.DemandSourceService;
import com.github.javafaker.Country;
import com.github.javafaker.Faker;
import com.google.i18n.phonenumbers.Phonenumber;
import configurations.User;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import zutils.FakerUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@AllArgsConstructor
public class DemandSourcePrecondition {

    private Integer responseCode;
    private DemandSource demandSourceResponse;
    private GenericResponse<DemandSource> demandSourceGetAllResponse;

    private DemandSourcePrecondition(DemandSourcePreconditionBuilder builder) {
        this.responseCode = builder.responseCode;
        this.demandSourceResponse = builder.demandSourceResponse;
        this.demandSourceGetAllResponse = builder.demandSourceGetAllResponse;
    }

    public static DemandSourcePreconditionBuilder demandSource() {

        return new DemandSourcePreconditionBuilder();
    }

    public static class DemandSourcePreconditionBuilder {

        private Response response;
        private Integer responseCode;
        private DemandSource demandSourceRequest;
        private DemandSource demandSourceResponse;
        private GenericResponse<DemandSource> demandSourceGetAllResponse;
        private DemandSourceService demandSourceService = new DemandSourceService();

        public DemandSourcePreconditionBuilder getDemandSourceList() {
            this.response = demandSourceService.getAll();

            this.demandSourceGetAllResponse = this.response.as(new TypeRef<GenericResponse<DemandSource>>() {
            });
            this.responseCode = response.getStatusCode();

            return this;
        }

        public DemandSourcePreconditionBuilder createDemandSource() {
            this.demandSourceRequest = DemandSource.builder()
                    .name(FakerUtils.captionWithSuffix("Demand auto"))
                    .status(1)
                    .currency(Currency.EUR.getAlias())
                    .country(CountryCode.US.getCode())
                    .bidEndpoints(List.of(BidEndpoint.builder()
                                    .id(2)
                                    .datacenterID(1)
                                    .dspID(3)
                                    .endpoint("http://dumbbidder:5001/bid")
                            .build()))
                    .syncUrl("http://sync.com")
                    .requestAdjustmentRate(100)
                    .idfaRequired(false)
                    .syncRequired(false)
                    .nativeAllowed(true)
                    .bannerAllowed(true)
                    .videoAllowed(false)
                    .platformIosAppAllowed(true)
                    .platformAndroidAppAllowed(true)
                    .platformMobileWebAllowed(true)
                    .platformPcWebAllowed(true)
                    .tokenGeneration(false)
                    .debugAdspotIds("")
                    .timeout(300)
                    .allowedCountries("")
                    .dspTypeId(0)
                    .isEnabled(true)
                    .pmpSupported(true)
                    .ecpmSupported(false)
                    .nonProgrammatic(false)
                    .build();

            this.response = demandSourceService.createDSP(this.demandSourceRequest);

            this.demandSourceResponse = this.response.as(DemandSource.class);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public DemandSourcePreconditionBuilder deleteDemandSource(int id) {
            this.response = demandSourceService.deleteDsp(id);
            this.responseCode = response.getStatusCode();

            return this;
        }

        public DemandSourcePreconditionBuilder setCredentials(User user) {
            HttpClient.setCredentials(user);

            return this;
        }

        public DemandSourcePreconditionBuilder getDSPsWithFilter(Map<String, Object> queryParams) {
            this.response = demandSourceService.getDSPsWithFilter(queryParams);
            this.demandSourceGetAllResponse = this.response.as(new TypeRef<GenericResponse<DemandSource>>() {
            });
            this.responseCode = response.getStatusCode();

            return this;
        }

        public DemandSourcePrecondition build() {

            return new DemandSourcePrecondition(this);
        }
    }
}
