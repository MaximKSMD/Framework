package api.dto.auth;

import zutils.ObjectMapperUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.transform.sax.SAXResult;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payload {

    private Integer id;
    private Integer exp;
    private String mail;
    private String name;
    private Integer role;
    private String loginId;
    private String updatedBy;
    private String createdBy;
    private String updatedAt;
    private String createdAt;
    private Boolean isEnabled;
    private Integer publisherId;
    private String publisherName;
    public String toJson() {
        return ObjectMapperUtils.toJson(this);
    }
}
