package api.dto.rx.sales.deals;

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
public class Identifier {
    private Integer publisherId;
    private String cdsc;
    private Kind kind;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
