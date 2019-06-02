package com.greglturnquist.learningspringboot.learningspringboot.webdriver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties("com.greglturnquist.webdriver")
public class WebDriverConfigurationProperties {

    private Firefox firefox = new Firefox();
    private Chrome chrome = new Chrome();
    private Safari safari = new Safari();

    @Data
    static class Firefox {
        private boolean enabled = true;
    }

    @Data
    static class Chrome {
        private boolean enabled = true;
    }

    @Data
    static class Safari {
        private boolean enabled = true;
    }
}
