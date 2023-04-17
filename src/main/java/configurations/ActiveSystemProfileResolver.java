package configurations;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class ActiveSystemProfileResolver {

    private static String osHostName;
    private static String profileName;

    private static final String USER_1 = "US17010293";
    private static final String USER_2 = "ODS01L0000003";
    private static final String USER_3 = "ODS01L0000005";
    private static final String USER_4 = "ashidlovskaya-pro13.local";

    public static String resolveCurrentProfile() {
        try {
            osHostName = InetAddress.getLocalHost().getHostName();

            switch (osHostName) {
                case USER_1:
                case USER_2:
                case USER_3:
                case USER_4:

                    profileName = "local";
                    break;
                default:

                    profileName = "rx-ci";
                    break;
            }
        } catch (UnknownHostException e) {
            log.error(String.format("An Exception has occur for host '%s'. Please see stacktrace below", osHostName));
            e.printStackTrace();
        }
        return profileName;
    }
}
