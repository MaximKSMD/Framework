package api.dto.rx.adsize;

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
public class Size {
    private int id;
    private int fixedWidthDp;
    private int fixedHeightDp;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
