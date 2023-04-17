package api.dto.rx.geo;

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
public class Geo {

    private Integer id;
    private String name;
    private String isoAlpha2;
    private String isoAlpha3;
    private String continentCode;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
