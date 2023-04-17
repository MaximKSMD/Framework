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
public class UserDto {

    private Integer id;
    private String mail;
    private String name;
    private Integer role;
    private Integer publisherId;
    private String publisherName;
    private String loginId;
    private Boolean isEnabled;
    private String updatedAt;
    private String createdAt;

    public String toJson() {

        return ObjectMapperUtils.toJson(this);
    }
}
