package api.dto.rx.adsize;

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
public class AdType {
    private int id;
    private String name;
    private int adSpecTypeId;
    private List<Size> sizes;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
