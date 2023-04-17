package api.dto.rx.demandsource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zutils.ObjectMapperUtils;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DemandSource {

    private Integer id;
    private String key;
    private String name;
    private String currency;
    private String country;
    private String syncUrl;
    private Integer requestAdjustmentRate;
    private Boolean idfaRequired;
    private Boolean syncRequired;
    private Boolean isEnabled;
    private Boolean nativeAllowed;
    private Boolean bannerAllowed;
    private Boolean videoAllowed;
    private Boolean platformIosAppAllowed;
    private Boolean platformAndroidAppAllowed;
    private Boolean platformMobileWebAllowed;
    private Boolean platformPcWebAllowed;
    private Boolean tokenGeneration;
    private Integer timeout;
    private String debugAdspotIds;
    private Integer status;
    private String allowedCountries;
    private Integer dspTypeId;
    private Boolean pmpSupported;
    private Boolean ecpmSupported;
    private Boolean nonProgrammatic;
    private String createdAt;
    private String updatedAt;
    private List<BidEndpoint> bidEndpoints;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
