import config.ServerConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

        //Получаем имя браузера из параметра -Dbrowser командной строки, если не указан то по умолчанию firefox
        String name = Optional.ofNullable(System.getProperty("browser")).orElse("firefox");

        //Получаем имя драйвера из класса Enum
        BrowserName browserName = BrowserName.findByName(name);

        //Если имя браузера не было распознано корректно, то логируем предупреждение
        if (browserName == BrowserName.DEFAULT) {
            logger.warn("WebDriver name from the cmdline is not recognized %" + name
                    + "% use Firefox");
        }

        //Создаём вебдрайвер через статический метод класса WebDriverFactory
        driver = WebDriverFactory.create(browserName);
        logger.info("Start WebDriver " + browserName.getBrowserName());

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    //Первый тест
    @Test
    public void yandexTest() {
        logger.info("Run Test 1 for Yandex");

        //Устанавливаем максимальный размер окна для браузера
        driver.manage().window().maximize();

        //Запрашиваем URL и проверяем title
        driver.get(cfg.urlYandex());
        assertEquals("Test failed, title is wrong", cfg.yandexTitle(), driver.getTitle());
        logger.info("Test 1 passed, title is " + driver.getTitle());
    }

    //Второй тест
    @Test
    public void teleTest() {
        logger.info("Run Test 2 for Tele-2");

        //Запрашиваем URL
        driver.get(cfg.urlTele2());

        //Ввод в поле "поиск номера" 97 и начало поиска
        driver.findElement(By.id("searchNumber")).sendKeys("97");

        //Ждём появления номеров в течении 10 секунд
        new WebDriverWait(driver, 10).
                until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("catalog-numbers")));
        logger.info("Test 2 passed");
    }

    @After
    public void setDown() {
        if (driver != null) {
            driver.close();
            logger.info("Shutdown WebDriver");
        } else {
            logger.error("Error WebDriver not found");
        }
    }
}
