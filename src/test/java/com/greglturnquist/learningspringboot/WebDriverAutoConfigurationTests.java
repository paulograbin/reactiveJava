package com.greglturnquist.learningspringboot;

import com.greglturnquist.learningspringboot.webdriver.FirefoxDriverFactory;
import com.greglturnquist.learningspringboot.webdriver.WebDriverAutoConfiguration;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class WebDriverAutoConfigurationTests {

    private AnnotationConfigApplicationContext context;

    @After
    public void close() {
        if (this.context != null)
            context.close();
    }

    /**
     * It starts off very different from what we've seen up until now. Instead of using
     * various Spring Boot test annotations, this one starts with nothing. That way, we
     * can add only the bits of Boot that we want in a very fine-grained fashion.
     * We'll use Spring's AnnotationConfigApplicationContext as the DI container of
     * choice to programmatically register beans.
     * The @After annotation flags the close() method to run after every test case and
     * close the application context, ensuring the next test case has a clean start.
     * load() will be invoked by each test method as part of its setup, accepting a list of
     * Spring configuration classes as well as optional property settings, as it creates a
     * new application context.
     * load() then registers a WebDriverAutoConfiguration class (which we haven't written
     * yet).
     * After that, it registers any additional test configuration classes we wish.
     * It then uses Spring Boot's EnvironmentTestUtils to add any configuration property
     * settings we need to the application context. This is a convenient way to
     * programmatically set properties without mucking around with files or system
     * settings.
     * It then uses the application context's refresh() function to create all the beans.
     * Lastly, it assigns the application context to the test class's context field.
     */
    private void load(Class<?>[] configs, String... environment) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        applicationContext.register(WebDriverAutoConfiguration.class);

        if (configs.length > 0) {
            applicationContext.register(configs);
        }

        EnvironmentTestUtils.addEnvironment(applicationContext, environment);

        applicationContext.refresh();
        this.context = applicationContext;
    }

    @Test
    public void fallbackToNonGuiModeWhenAllBrowsersDisabled() {
        load(new Class[]{},
                "com.greglturnquist.webdriver.firefox.enabled:false",
                "com.greglturnquist.webdriver.safari.enabled:false",
                "com.greglturnquist.webdriver.chrome.enabled:false");

        WebDriver driver = context.getBean(WebDriver.class);
        assertThat(ClassUtils.isAssignable(TakesScreenshot.class,
                driver.getClass())).isFalse();
        assertThat(ClassUtils.isAssignable(HtmlUnitDriver.class,
                driver.getClass())).isTrue();
    }

    @Test
    public void testWithMockedFirefox() {
        load(new Class[]{MockFirefoxConfiguration.class},
                "com.greglturnquist.webdriver.safari.enabled:false",
                "com.greglturnquist.webdriver.chrome.enabled:false");

        WebDriver driver = context.getBean(WebDriver.class);
        assertThat(ClassUtils.isAssignable(TakesScreenshot.class,
                driver.getClass())).isTrue();
        assertThat(ClassUtils.isAssignable(FirefoxDriver.class,
                driver.getClass())).isTrue();
    }

    @Configuration
    protected static class MockFirefoxConfiguration {

        @Bean
        FirefoxDriverFactory firefoxDriverFactory() {
            FirefoxDriverFactory factory = mock(FirefoxDriverFactory.class);

            given(factory.getObject()).willReturn(mock(FirefoxDriver.class));

            return factory;
        }
    }
}
