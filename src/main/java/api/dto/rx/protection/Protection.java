package api.dto.rx.protection;

import zutils.ObjectMapperUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Protection extends ProtectionRequest{
    private Integer id;
    private String createdAt;
    private String updatedAt;


    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
