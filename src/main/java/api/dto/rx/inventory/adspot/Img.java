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
public class Img {

    private Integer width;
    private Integer height;
    private Integer imgType;
    private Integer minWidth;
    private Integer minHeight;
    private List<String> mimes;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
