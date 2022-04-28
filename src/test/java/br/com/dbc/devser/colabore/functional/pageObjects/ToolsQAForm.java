package br.com.dbc.devser.colabore.functional.pageObjects;

import org.openqa.selenium.By;

public class ToolsQAForm {

    /*



            Name, Id, Css, XPATH
            ABSOLUTO --> /html/body/div[2]/div/div/div[2]/div[2]/div[2]/form/div[1]/div[2]/input


            RELATIVO --> //*[@id="firstName"]
     */

    public static By inputFirstName = By.xpath("//input[@placeholder='First Name']");
    public static By inputLastName = By.xpath("//input[@placeholder='Last Name']");
    public static By radioGender = By.cssSelector("#gender-radio-1");

}
