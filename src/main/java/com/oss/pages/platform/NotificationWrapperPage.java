package com.oss.pages.platform;

import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class NotificationWrapperPage extends BasePage {

    public NotificationWrapperPage(WebDriver driver){super(driver);}

    @FindBy(xpath = "//i[contains(@class, 'notificationIcon fa fa-bell-o')]")
    private WebElement notificationButton;
    @FindBy(xpath = "//div[@class='notificationContainer']/div")
    private List<WebElement> notificationsList ;
    @FindBy(xpath = "//a[@class ='clear-action']")
    private WebElement clearAllNotifications;

    public NotificationWrapperPage clearNotifications(){
        clearAllNotifications.click();
        return this;
    }

    public NotificationWrapperPage waitForExportFinish(){
        waitForComponent("//a[contains (text(), 'Download file')]");
        return this;
    }

    public int amountOfNotifications(){
        return notificationsList.size();
    }
}
