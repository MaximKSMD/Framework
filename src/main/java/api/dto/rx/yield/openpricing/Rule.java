package api.dto.rx.yield.openpricing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zutils.ObjectMapperUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rule {

    public Dsp dsp;
    public Geo geo;
    public AdSize adSize;
    public MediaRule media;
    public AdSpotRule adspot;
    public AdFormat adFormat;
    public DeviceOS deviceOS;
    public DeviceType deviceType;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
