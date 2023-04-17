package api.dto.rx.demandsource;

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
public class BidEndpoint {

    private Integer id;
    private Integer dspID;
    private String endpoint;
    private Integer datacenterID;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
