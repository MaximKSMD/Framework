package configurations;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static zutils.FakerUtils.captionWithSuffix;

@Getter
@AllArgsConstructor
public enum User {

    //TEST_USER("jyothi.kotla@rakuten.com", "tUJ*WwQF_7kDHmftjPhV"),
    TEST_USER("os-yuliia.podust@rakuten.com", "Testpass1!"),
    TEMP_USER(String.format("%s@rakuten.com",captionWithSuffix("auto")), "Password1"),
    WRONG_EMAIL_USER("serhii.shydlovsky@rakuten.com", "Password1"),
    WRONG_PASSWORD_USER("os-serhii.shydlovsky@rakuten.com", "Password232"),
    KEYCLOAK_ADMIN("system", "P@ssw0rd"),
    USER_FOR_DELETION("mail74582@test.com","Password1");

    private String mail;
    private String password;
}
