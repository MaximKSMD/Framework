package managers;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.proxy.CaptureType;
import com.browserup.harreader.model.HarEntry;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.browserup.bup.proxy.CaptureType.*;
import static java.time.Duration.ofSeconds;

@Slf4j
@NoArgsConstructor
public class BrowserUpProxyManager {

    public static BrowserProxyBuilder getProxy() {

        return new BrowserProxyBuilder();
    }

    public static class BrowserProxyBuilder {

        private static final BrowserUpProxy proxy = WebDriverRunner.getSelenideProxy().getProxy();

        public BrowserProxyBuilder createNewHar(String harName, HarCaptureTypes type) {
            proxy.setHarCaptureTypes(getAllContentCaptureTypes());
            proxy.enableHarCaptureTypes(this.setHarCaptureType(type));
            proxy.newHar(harName);

            return this;
        }

        public BrowserProxyBuilder setActionAndWrite(SelenideElement elem, Condition condition) {
            elem.shouldBe(condition, ofSeconds(5));

            return this;
        }

        public List<HarEntry> getResult() {

            return proxy.getHar().getLog().getEntries();
        }

        private CaptureType[] setHarCaptureType(HarCaptureTypes type) {
            switch (type) {
                case REQUEST:

                    return new CaptureType[] {REQUEST_CONTENT};
                case RESPONSE:

                    return new CaptureType[] {RESPONSE_CONTENT};
                case ALL:
                default:

                    return new CaptureType[] {REQUEST_CONTENT, RESPONSE_CONTENT};
            }
        }
    }
}
