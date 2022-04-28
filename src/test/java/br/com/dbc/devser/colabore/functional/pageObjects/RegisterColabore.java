package br.com.dbc.devser.colabore.functional.pageObjects;

import org.openqa.selenium.By;

public class RegisterColabore {

    public static By inputEmail = By.id("email");
    public static By inputName = By.name("login");
    public static By inputPassword = By.id("password");
    public static By confirmPassword = By.id("confirmPassword");
    public static By btnPhoto = By.id("profilePhoto");
    public static By btnRegister = By.cssSelector("button[type='submit']");

}
