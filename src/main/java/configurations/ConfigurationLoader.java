package configurations;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

import static java.lang.String.format;

@Slf4j
public class ConfigurationLoader {

    private static final String FILENAME_PREFIX = "application";

    public static EnvConfiguration getConfig() {
        String environmentName = ActiveSystemProfileResolver.resolveCurrentProfile().toLowerCase();

        return getConfig(environmentName);
    }

    private static EnvConfiguration getConfig(String env) {
        String filename = format("%s-%s.yml", FILENAME_PREFIX, env);
        Yaml yaml = new Yaml();
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(filename);

        return  yaml.loadAs(in, EnvConfiguration.class);
    }
}
