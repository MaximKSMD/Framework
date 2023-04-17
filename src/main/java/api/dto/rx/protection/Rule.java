package api.dto.rx.protection;

import zutils.ObjectMapperUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rule {

    public AdSpot adspot;
    public AdFormat adFormat;
    public AdSize adSize;
    public Media media;
    public DeviceType deviceType;
    public Ifa ifa;
    public Geo geo;
    public DeviceOS deviceOS;
    public Dsp dsp;
    public DspSeat dspSeat;
    public Advertiser advertiser;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
