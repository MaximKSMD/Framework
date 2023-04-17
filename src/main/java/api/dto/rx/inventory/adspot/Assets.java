package api.dto.rx.inventory.adspot;

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
public class Assets {

    private Integer id;
    private Img     img;
    private DataObj data;
    private String  assetType;
    private List<Integer> playbackMethodIds;
    private List<Integer> sizeIds;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
