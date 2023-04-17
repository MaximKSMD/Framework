package api.dto.rx.privateauction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zutils.ObjectMapperUtils;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrivateAuction extends PrivateAuctionRequest{

    private Integer id;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
