package managers;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverProvider;
import com.codeborne.selenide.WebDriverRunner;
import configurations.EnvConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.codeborne.selenide.FileDownloadMode.PROXY;
import static configurations.ConfigurationLoader.getConfig;
import static io.qameta.allure.Allure.*;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static org.openqa.selenium.remote.CapabilityType.TAKES_SCREENSHOT;

@Slf4j
public class WebDriverManager {

    private WebDriver driver;
    private static String currentTestClassName;
    private static EnvConfiguration config = getConfig();

    public WebDriverManager(String testDisplayName) {
        Configuration.pageLoadStrategy = config.getPageLoadStrategy();
        Configuration.browserSize = config.getBrowserSize();
        Configuration.holdBrowserOpen = config.getHoldBrowserOpen();
        Configuration.reopenBrowserOnFail = config.getReopenBrowserOnFail();
        Configuration.screenshots = config.getScreenshots();
        Configuration.timeout = config.getTimeout();
        Configuration.headless = config.getHeadless();
        currentTestClassName = testDisplayName;
        setProxyEnabled(config.getEnableProxy());
        Configuration.startMaximized = true;
        this.currentTestClassName = testDisplayName;
        setSelenoidEnabled(config.getEnableSelenoid());
        Configuration.downloadsFolder = config.getDownloadsDir();
    }

    private void setProxyEnabled(boolean enabled) {
        if (enabled) {
            Configuration.proxyEnabled = true;
            Configuration.fileDownload = PROXY;
        } else {
            Configuration.proxyEnabled = false;
        }
    }

    private void setSelenoidEnabled(boolean enabled) {
        if (enabled) {
            Configuration.driverManagerEnabled = false;
            getProvider();
        } else {
            Configuration.driverManagerEnabled = true;
            Configuration.browser = config.getBrowser();
        }
    }

    private void getProvider() {
        switch (config.getBrowser()) {
            case "edge":
                log.error("Edge browser is not supported");
                break;
            case "chrome":
            default:
                Configuration.browser = CustomProvider.class.getName();
        }
    }

    public static class CustomProvider implements WebDriverProvider {

        @Override
        public WebDriver createDriver(final DesiredCapabilities capabilities) {
            final ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setAcceptInsecureCerts(true);
            chromeOptions.setCapability("enableVNC", true);
            chromeOptions.setCapability("enableVideo", true);
            chromeOptions.setCapability("videoFrameRate", 24);
            chromeOptions.setCapability("videoName",
                    format("%s-%s.mp4", currentTestClassName, Thread.currentThread().getId()));
            chromeOptions.setCapability("name",
                    format("%s-%s.mp4", currentTestClassName, Thread.currentThread().getId()));

            chromeOptions.setCapability(TAKES_SCREENSHOT, true);
            chromeOptions.setCapability("enableLog", true);
            chromeOptions.setCapability("env", Collections.singletonList("VERBOSE=true"));
            chromeOptions.setCapability("timeZone", "Europe/Kiev");
            RemoteWebDriver driver;
            try {
                driver = new RemoteWebDriver(
                        new URL(format("%s:%s/wd/hub", config.getSelenoidHost(), config.getSelenoidPort())), chromeOptions);
                driver.setFileDetector(new LocalFileDetector());
            } catch (final MalformedURLException e) {
                log.error("Unable to create remote driver due to MalformedURLException");

                throw new RuntimeException("Unable to create remote driver due to MalformedURLException\n", e);
            } catch (final UnreachableBrowserException e) {
                log.error(format("Please check if selenoid is turned on and is accessible by %s:%s",
                        config.getSelenoidHost(), config.getSelenoidPort()));

                throw new RuntimeException("Unable to create remote driver due to MalformedURLException\n", e);
            }

            return driver;
        }
    }

    public void closeWebDriverSession() {
        log.info("Current test class {}", currentTestClassName);
        if (!config.getHoldBrowserOpen()) {
            log.info("Checking weather WebDriver session has been started in thread {}", currentThread().getId());
            try {
                driver = WebDriverRunner.getWebDriver();
                this.closeBrowserAlertsIfPresent();

            } catch (NullPointerException e) {
                log.warn("WebDriver is null  in thread {}", currentThread().getId());
            } catch (NoSuchSessionException e) {
                log.warn("WebDriver Session hasn't been started in thread {}. Nothing to close", currentThread().getId());
            } catch (IllegalStateException e) {
                log.warn("No WebDriver is bound to current thread: '{}'. You need to call open(url) first.", currentThread().getId());
            }
        } else {
            log.info("WebDriver will not close the session due to holdBrowserOpened = 'true'");
        }
    }

    public String getBrowserLogs() {
        log.info("Collecting Browser Log");
        String browserLogString = "";
        try {
            browserLogString = driver.manage().logs().get(LogType.BROWSER)
                    .getAll().stream()
                    .map(LogEntry::toString)
                    .collect(Collectors.joining("\n"));
        } catch (NullPointerException e) {
            log.warn("WebDriver is null  in thread {}", currentThread().getId());
        } catch (NoSuchSessionException e) {
            log.warn("WebDriver Session hasn't been started in thread {}. Nothing to close", currentThread().getId());
        } catch (IllegalStateException e) {
            log.warn("No WebDriver is bound to current thread: '{}'. You need to call open(url) first.", currentThread().getId());
        }

        return browserLogString;
    }

    public void attachTheBrowserConsoleLog() {
        try {
            if (driver != null) {
                log.info("Attaching Browser Console log to Allure Report");
                InputStream is = new ByteArrayInputStream(this.getBrowserLogs().getBytes(StandardCharsets.UTF_8));
                addAttachment("Browser log", is);
            } else {
                log.warn("Can't attach browser log because Browser wasn't started");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeBrowserAlertsIfPresent() {
        log.info("Accepting browser alerts if they are presented");
        try {
            driver.switchTo().alert().accept();
            log.info("Closing webDriver session in thread {}", currentThread().getId());
        } catch (NoAlertPresentException e) {
            log.info("Closing webDriver session in thread {} without alerts", currentThread().getId());
        }
        driver.quit();
    }
}
