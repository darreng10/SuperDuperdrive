package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NotePage {
    private WebDriver driver;

    public NotePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "logoutButton")
    private WebElement logoutButton;


    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;


    @FindBy(id = "homeNoteTitle")
    private WebElement homeNoteTitle;

    @FindBy(id = "homeNoteDescription")
    private WebElement homeNoteDescription;



    public void logout(){
        logoutButton.click();
    }

    public WebElement getNotesTab(){
        return notesTab;
    }

    public WebElement getLogoutButton() {
        return logoutButton;
    }


    public WebElement getHomeNoteTitle() {
        return homeNoteTitle;
    }

    public WebElement getHomeNoteDescription() {
        return homeNoteDescription;
    }


}
