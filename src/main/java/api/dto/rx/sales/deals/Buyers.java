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
public class Buyers {
    private Integer id;
    private Boolean expanded;
    private Integer dspId;
    private String proposal;
    private Boolean enabled;
    private Buyer buyer;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
