package api.dto.rx.currency;

import zutils.ObjectMapperUtils;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency {

    private String name;
    private Integer code;
    private String symbol;

    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
