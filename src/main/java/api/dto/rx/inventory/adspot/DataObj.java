package api.dto.rx.inventory.adspot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import zutils.ObjectMapperUtils;

@lombok.Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataObj {

    private Integer len;
    private Integer type;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
