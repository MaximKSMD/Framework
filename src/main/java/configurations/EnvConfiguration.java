package configurations;

import lombok.Data;

@Data
public  class EnvConfiguration {

    private String baseUrl;
    private String browser;
    private Integer timeout;
    private Boolean headless;
    private String baseApiUrl;
    private String browserSize;
    private Boolean screenshots;
    private Boolean enableProxy;
    private String downloadsDir;
    private String selenoidHost;
    private Integer selenoidPort;
    private Boolean enableSelenoid;
    private String pageLoadStrategy;
    private Boolean holdBrowserOpen;
    private Boolean reopenBrowserOnFail;
}
