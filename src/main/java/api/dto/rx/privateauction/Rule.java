package api.dto.rx.privateauction;

import api.dto.rx.yield.openpricing.AdSize;
import api.dto.rx.yield.openpricing.Geo;
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
public class Rule {

    private List<Integer> adFormats;
    private List<Integer> deviceOS;
    private List<Integer> dsps;
    private Geo geo;
    private List<AdSize> adSizes;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
