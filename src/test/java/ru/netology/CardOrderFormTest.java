package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

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

        // Открытие страницы в браузере вынесено в метод setup в качестве предусловия
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testFormSubmissionSuccess() {
        // Ввод корректных данных
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пастухова");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+71112225544");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();

        // Проверка успешного сообщения
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    public void testFormSubmissionInvalidName() {
        // Ввод некорректных данных (имя содержит цифры)
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пастухова1 Лидия");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+71112225566");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();

        // Проверка наличия сообщения об ошибке
        String errorMessage = driver.findElement(By.cssSelector("[data-test-id=name] .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", errorMessage.trim());
    }

    @Test
    public void testFormSubmissionInvalidPhone() {
        // Ввод некорректных данных (неверный формат номера телефона)
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пастухова Лидия");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("1122");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();

        // Проверка наличия сообщения об ошибке
        String errorMessage = driver.findElement(By.cssSelector("[data-test-id=phone] .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", errorMessage.trim());
    }


    @Test
    public void testFormSubmissionUncheckedCheckbox() {
        // Ввод корректных данных, но без установленного чекбокса
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Пастухова Лидия");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+71112225566");
        // Не устанавливаем чекбокс
        driver.findElement(By.className("button")).click();

        // Проверка наличия сообщения об ошибке
        String errorMessage = driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__text")).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", errorMessage.trim());
    }

    @Test
    public void testFormSubmissionEmptyFields() {
        // Ввод пустых данных
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("");
        driver.findElement(By.className("button")).click();

        // Проверка наличия сообщений об ошибке для каждого поля
        String nameErrorMessage = driver.findElement(By.cssSelector("[data-test-id=name] .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", nameErrorMessage.trim());

        String phoneErrorMessage = driver.findElement(By.cssSelector("[data-test-id=phone] .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", phoneErrorMessage.trim());
    }
}





