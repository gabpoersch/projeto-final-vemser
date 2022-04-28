package br.com.dbc.devser.colabore.functional.actions;

import br.com.dbc.devser.colabore.functional.pageObjects.ToolsQAForm;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@RequiredArgsConstructor
public class FormAction {

    private final WebDriver driver;

   public FormAction writeFirstName (){
       WebElement input = driver.findElement(ToolsQAForm.inputFirstName);
       input.sendKeys("Gabriel Poersch");
       return this;
   }

   public FormAction clickRadio (){
       driver.findElement(ToolsQAForm.radioGender).click();
       return this;
   }

}
