package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CredentialPage {
    private WebDriver driver;

    public CredentialPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "logoutButton")
    private WebElement logoutButton;


    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;


    @FindBy(id = "homeCredentialUrl")
    private WebElement homeCredentialUrl;

    @FindBy(id = "homeCredentialUsername")
    private WebElement homeCredentialUsername;

    @FindBy(id = "homeCredentialPassword")
    private WebElement homeCredentialPassword;

    public WebElement getCredentialsTab() {
        return credentialsTab;
    }

    public void logout() {
        logoutButton.click();
    }


    public WebElement getLogoutButton() {
        return logoutButton;
    }


    public WebElement getHomeCredentialUrl() {
        return homeCredentialUrl;
    }

    public WebElement getHomeCredentialUsername() {
        return homeCredentialUsername;
    }

    public WebElement getHomeCredentialPassword() {
        return homeCredentialPassword;
    }


}