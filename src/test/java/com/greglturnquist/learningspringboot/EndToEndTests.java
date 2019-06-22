package com.greglturnquist.learningspringboot;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class EndToEndTests {

    static ChromeDriverService service;
    static ChromeDriver driver;

    @LocalServerPort
    int port;


    @BeforeClass
    public static void setUp() throws IOException {
        System.setProperty("webdriver.chrome.driver", "ext/chromedriver");

        service = ChromeDriverService.createDefaultService();
        driver = new ChromeDriver(service);

        Path testResults = Paths.get("build", "test-results");
        if (!Files.exists(testResults)) {
            Files.createDirectory(testResults);
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        service.stop();
    }

    /**
     * @Test indicates this is a JUnit test case
     * driver navigates to the home page using the injected port
     * It takes a screenshot so we can inspect things after the fact
     * We verify the title of the page is as expected
     * Next, we grab the entire page's HTML content and verify one of the links
     * Then we hunt down that link using a W3C CSS selector (there are other options
     * as well), move to it, and click on it
     * We grab another snapshot and then click on the back button
     * @throws IOException
     */
    @Test
    public void homePageShouldWork() throws IOException {
        driver.get("http://localhost:" + port);

        takeScreenshot("homePageShouldWork-1");

        assertThat(driver.getTitle()).isEqualTo("Learning Sprint Boot: Spring-a-Gram");

        String pageContent = driver.getPageSource();

        assertThat(pageContent).contains("<a href=\"/images/bazinga.jpg/raw\">");
        WebElement element = driver.findElement(By.cssSelector("a[href*=\"bazinga.jpg\"]"));

        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().perform();

        takeScreenshot("homePageShouldWork-2");
        driver.navigate().back();
    }

    private void takeScreenshot(String s) throws IOException {
        FileCopyUtils.copy(driver.getScreenshotAs(OutputType.FILE), new File("build/test-results/TEST-" + s + ".png"));
    }
}
