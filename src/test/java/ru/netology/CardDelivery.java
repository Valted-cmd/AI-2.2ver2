package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

class CardDelivery {



    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");}

    @Test
    void shouldCardOrder() {
        String planningDate = generateDate(12);
        $("[placeholder=\"Город\"]").setValue("Москва"); //Ввод города
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[type=\"tel\"]").setValue(planningDate); //дата
        $("[name=\"name\"]").setValue("Иван Иванов");  // Ввод имени и фамилии
        $("[name=\"phone\"]").setValue("+79277614162");  // Ввод телефона
        $("[data-test-id=\"agreement\"]").click();  // Клик по чекбоксу
        $x("//*[text()=\"Забронировать\"]").click(); // Клик по кнопке забронировать
        $x("//*[text()=\"Успешно!\"]").should(visible, Duration.ofSeconds(16)); //сообщение о встрече с курьером+ установка времени ожидания появления сообщения
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldCardOrderWithInvalidCity() {
        String planningDate = generateDate(15);
        $("[placeholder=\"Город\"]").setValue("Тольятти"); //Ввод некорректно города
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[type=\"tel\"]").setValue(planningDate); //дата
        $("[name=\"name\"]").setValue("Иван Иванов");  // Ввод имени и фамилии
        $("[name=\"phone\"]").setValue("+79277614162");  // Ввод телефона
        $("[data-test-id=\"agreement\"]").click();  // Клик по чекбоксу
        $x("//*[text()=\"Забронировать\"]").click(); // Клик по кнопке забронировать

        $x("//*[text()=\"Доставка в выбранный город недоступна\"]").should(visible); // сообщение что в данный город доставка не возможна
    }
    @Test
    void shouldCardOrderWithInvalidPhone() {
        String planningDate = generateDate(4);
        $("[placeholder=\"Город\"]").setValue("Москва"); //Ввод  города
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[type=\"tel\"]").setValue(planningDate); //дата
        $("[name=\"name\"]").setValue("Иван Иванов");  // Ввод имени и фамилии
        $("[name=\"phone\"]").setValue("89277614162");  // Ввод  некорректно телефона
        $("[data-test-id=\"agreement\"]").click();  // Клик по чекбоксу
        $x("//*[text()=\"Забронировать\"]").click(); // Клик по кнопке забронировать

        $(withText("Телефон указан неверно.")).should(visible);  // сообщение о неккоректном телефоне
    }
    @Test
    void shouldCardOrderWithInvalidName() {
        String planningDate = generateDate(5);
        $("[placeholder=\"Город\"]").setValue("Москва"); //Ввод  города
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[type=\"tel\"]").setValue(planningDate); //дата
        $("[name=\"name\"]").setValue("John Sina№/");  // Ввод  неккоректного имени и фамилии
        $("[name=\"phone\"]").setValue("+79277614162");  // Ввод телефона
        $("[data-test-id=\"agreement\"]").click();  // Клик по чекбоксу
        $x("//*[text()=\"Забронировать\"]").click(); // Клик по кнопке забронировать

        $(withText("Имя и Фамилия указаные неверно.")).should(visible);  // сообщение о неккоректном имени
    }

    @Test
    void shouldCardOrderWithLetterЁInName() {
        $("[placeholder=\"Город\"]").setValue("Москва"); //Ввод города
        $("[type=\"tel\"]").setValue("planningDate"); //дата
        $("[name=\"name\"]").setValue("Калёнов Андрей");  // Ввод имени и фамилии
        $("[name=\"phone\"]").setValue("+79277614162");  // Ввод телефона
        $("[data-test-id=\"agreement\"]").click();  // Клик по чекбоксу
        $x("//*[text()=\"Забронировать\"]").click(); // Клик по кнопке забронировать
        $x("//*[text()=\"Успешно!\"]").should(Condition.visible, Duration.ofSeconds(16)); //сообщение о встрече с курьером+ установка времени ожидания появления сообщения
    }


    @Test
    void shouldCardOrderWithInvaliData() {
        String planningDate = generateDate(2);
        $("[placeholder=\"Город\"]").setValue("Москва"); //Ввод  города
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate); //дата
        $("[name=\"name\"]").setValue("Иван Иванов");  // Ввод имени и фамилии
        $("[name=\"phone\"]").setValue("+79277614162");  // Ввод телефона
        $("[data-test-id=\"agreement\"]").click();  // Клик по чекбоксу
        $x("//*[text()=\"Забронировать\"]").click(); // Клик по кнопке забронировать

        $(byText("Заказ на выбранную дату невозможен")).shouldBe(Condition.visible);  // сообщение о неккоректной дате
    }
    @Test
    void shouldCardOrderWithInvalidCheckBox() {
        String planningDate = generateDate(10);
        $("[placeholder=\"Город\"]").setValue("Москва"); //Ввод  города
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[type=\"tel\"]").setValue(planningDate); //дата
        $("[name=\"name\"]").setValue("Иван Иванов");  // Ввод  неккоректного имени и фамилии
        $("[name=\"phone\"]").setValue("+79277614162");  // Ввод телефона
        $("[data-test-id=\"agreement\"]"); //  НЕ кликать по чекбоксу
        $x("//*[text()=\"Забронировать\"]").click(); // Клик по кнопке забронировать
        $("[data-test-id=\"notification\"]").should(Condition.not(visible)); //текст подсвечивается красным, напоминалка

    }


}