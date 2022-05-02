package br.com.dbc.devser.colabore.functional.pageObjects;

import org.openqa.selenium.By;

public class HomeColabore {
    public static By exploreButton = By.xpath("//*[@id=\"root\"]/header/nav/ul/li[1]/a");
    public static By searchBar = By.xpath("//*[@id=\"root\"]/header/nav/ul/li[2]/div");
    public static By createButton = By.xpath("//*[@id=\"root\"]/header/nav/ul/div/li[2]/button");
    public static By logoutButton = By.xpath("//*[@id=\"root\"]/header/nav/ul/li/button");

    public static By btnMyFundraisers = By.xpath("//*[@id=\"root\"]/div[1]/div[1]/button[1]");
    public static By btnMyContributions = By.xpath("//*[@id=\"root\"]/div[1]/div[1]/button[2]");

    public static By categorySelectionButton = By.xpath("//*[@id=\"root\"]/div[2]/div[1]/div/div[1]");
    public static By selectionButton = By.xpath("");
    public static By previousButton = By.xpath("//*[@id=\"root\"]/div[2]/div[3]/button[1]");
    public static By nextButton = By.xpath("//*[@id=\"root\"]/div[2]/div[3]/button[2]");
}
