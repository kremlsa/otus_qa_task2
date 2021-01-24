import config.ServerConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Домашнее задание №2
 * Тест Настройка Web-driver
 * Александр Кремлёв
 * 1.0
 */

public class Task2Test extends Assert {
    private final Logger logger = LogManager.getLogger(Task2Test.class);
    protected static WebDriver driver;
    private final ServerConfig cfg = ConfigFactory.create(ServerConfig.class);

    @Before
    public void Setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        driver = WebDriverFactory.create("Chrome");
        logger.info("Startup web driver");
    }

    @Test
    public void StartTest() {
        logger.info("Test start");
        driver.get(cfg.urlYandex());
        try {
            assertEquals(cfg.yandexTitle(), driver.getTitle());
            logger.info("Test passed, title is " + driver.getTitle());
        } catch(AssertionError e){
            logger.info("Test failed, title is " + driver.getTitle());
            throw e;
        }
        logger.info("Test finish");
    }

    @After
    public void setDown() {
        if (driver != null) {
            driver.close();
            logger.info("Shutdown web driver");
        } else {
            logger.error("Web driver not found");
        }
    }
}
