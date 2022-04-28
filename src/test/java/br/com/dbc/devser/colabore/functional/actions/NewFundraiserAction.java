package br.com.dbc.devser.colabore.functional.actions;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static br.com.dbc.devser.colabore.functional.pageObjects.NewFundraiser.*;

@RequiredArgsConstructor
public class NewFundraiserAction {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public NewFundraiserAction writeInputTitle(String text) {
        CommomActions.write(driver, wait, text, inputTitle);
        return this;
    }

    public NewFundraiserAction writeInputGoal(String text) {
        CommomActions.write(driver, wait, text, inputGoal);
        return this;
    }

    public NewFundraiserAction clickAutomaticClose() {
        driver.findElement(checkAutomaticClose).click();
        return this;
    }

    public NewFundraiserAction writeInputDate(String textDate) {
        CommomActions.write(driver, wait, textDate, inputDate);
        return this;
    }

    public NewFundraiserAction clickBtnCoverPhoto() {
        WebElement btnPhotoElement = wait.until(ExpectedConditions
                .elementToBeClickable(driver.findElement(btnCoverPhoto)));
        btnPhotoElement.sendKeys(System.getProperty("user.dir") + "/src/main/resources/img/doacao-foto.png");
        return this;
    }

    public NewFundraiserAction writeInputCategories(List<String> categories) {
        WebElement inputCreateCategories = wait.until(ExpectedConditions
                .visibilityOf(driver.findElement(inputCategories)));
        Actions act = new Actions(driver);
        for (int i = 0; i < categories.size(); i++) {
            act.sendKeys(inputCreateCategories, categories.get(i)).keyDown(Keys.ENTER).keyUp(Keys.ENTER);
        }
        return this;
    }

    public NewFundraiserAction writeDescription(String text) {
        CommomActions.write(driver, wait, text, textArea);
        return this;
    }

    public NewFundraiserAction clickBtnBack() {
        driver.findElement(buttonBackFundraiser).click();
        return this;
    }

    public NewFundraiserAction clickBtnSave() {
        driver.findElement(buttonSaveFundraiser).click();
        return this;
    }

}
