package br.com.dbc.devser.colabore.functional.pageObjects;

import org.openqa.selenium.By;

public class HeaderColabore {
    public static By exploreButton = By.xpath("//*[@id=\"root\"]/header/nav/ul/li[1]/a");
    public static By searchBar = By.xpath("//*[@id=\"root\"]/header/nav/ul/li[2]/div");
    public static By createButton = By.xpath("//*[@id=\"root\"]/header/nav/ul/li[3]/button");
    public static By logoutButton = By.xpath("//*[@id=\"root\"]/header/nav/ul/li[4]/button");
}
