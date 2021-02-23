package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FilePage {
    private WebDriver driver;

    public FilePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "logoutButton")
    private WebElement logoutButton;

    @FindBy(id = "nav-files-tab")
    private WebElement filesTab;



    public void logout() {
        logoutButton.click();
    }


    public WebElement getLogoutButton() {
        return logoutButton;
    }

    public WebElement getFilesTab() {
        return filesTab;
    }


}