package com.greglturnquist.learningspringboot.webdriver;

import com.greglturnquist.learningspringboot.webdriver.WebDriverConfigurationProperties;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;


public class ChromeDriverFactory implements ObjectFactory<FirefoxDriver> {

    private WebDriverConfigurationProperties properties;

    public ChromeDriverFactory(WebDriverConfigurationProperties properties) {
        this.properties = properties;
    }

    @Override
    public FirefoxDriver getObject() throws BeansException {
        if(properties.getChrome().isEnabled()) {
            try {
                return new FirefoxDriver();
            } catch (WebDriverException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
