package api.dto.rx.admin.user;

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
public class UserRequest {

    private String name;
    private String mail;
    private Integer role;
    private String password;
    private Boolean isEnabled;
    private Integer publisherId;

    public String toJson() {

        return ObjectMapperUtils.toJson(this);
    }
}
