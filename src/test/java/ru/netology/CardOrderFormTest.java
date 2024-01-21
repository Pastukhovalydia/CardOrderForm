package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardOrderFormTest {

    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @Test
    public void testFormSubmissionSuccess() {
        driver.get("http://localhost:9999/");

        // Ввод корректных данных
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пастухова");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79139211093");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();

        // Проверка успешного сообщения
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    public void testFormSubmissionInvalidName() {
        driver.get("http://localhost:9999/");

        // Ввод некорректных данных (имя содержит цифры)
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пастухова1 Лидия");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79139211093");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();

        // Проверка отсутствия успешного сообщения
        boolean successMessagePresent = driver.findElements(By.cssSelector("[data-test-id=order-success]")).size() > 0;
        assertEquals(false, successMessagePresent);
    }

    @Test
    public void testFormSubmissionInvalidPhone() {
        driver.get("http://localhost:9999/");

        // Ввод некорректных данных (неверный формат номера телефона)
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пастухова Лидия");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79134452255");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();

        // Проверка отсутствия успешного сообщения
        boolean successMessagePresent = driver.findElements(By.cssSelector("[data-test-id=order-success]")).size() > 0;
        assertEquals(false, successMessagePresent);
    }

    @Test
    public void testFormSubmissionUncheckedCheckbox() {
        driver.get("http://localhost:9999/");

        // Ввод корректных данных, но без установленного чекбокса
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пастухова Лидия");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79139211093");
        // Не устанавливаем чекбокс
        driver.findElement(By.className("button")).click();

        // Проверка отсутствия успешного сообщения
        boolean successMessagePresent = driver.findElements(By.cssSelector("[data-test-id=order-success]")).size() > 0;
        assertEquals(false, successMessagePresent);
    }


}





