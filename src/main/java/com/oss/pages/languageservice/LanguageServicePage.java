package com.oss.pages.languageservice;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import com.oss.pages.exportguiwizard.ExportGuiWizardPage;
import com.oss.pages.platform.LoginPanelPage;
import com.oss.pages.platform.NotificationWrapperPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class LanguageServicePage extends BasePage {

    public LanguageServicePage(WebDriver driver) {super(driver);}

    @FindBy(xpath = "//a[contains(@class, 'loginButton')]")
    private WebElement loginButton;
    @FindBy(id = "frameworkCustomButtonsGroup")
    private WebElement menu;
    @FindBy(id = "tableExportGUI")
    private List<WebElement> exportGui;
    @FindBy(id = "exportButton")
    private WebElement exportButton;
    @FindBy(xpath = "//i[contains(@class, 'notificationIcon fa fa-bell-o')]")
    private WebElement notificationButton;
    @FindBy(xpath = "//div[@data-attributename ='search']//input")
    private WebElement searchField;
    @FindBy(xpath = "(//div[@type='Translation'])[1]")
    private WebElement firstService;

    private ExportGuiWizardPage exportGuiWizard;
//    private boolean existsElement(List<WebElement> element) {return element.size() != 0;}

    private LanguageServicePage expandMenu() {
        waitforclickability(menu);
        menu.click();
        return this;
    }

    private LoginPanelPage openLoginPanel() {
        waitForVisibility(loginButton);
        loginButton.click();
        return new LoginPanelPage(driver);
    }

    private LanguageServicePage closeLoginPanel() {
        waitForVisibility(loginButton);
        loginButton.click();
        return this;
    }

    public LanguageServicePage changeForAlphaMode() {
        openLoginPanel()
                .changeForAlphaMOde();
        closeLoginPanel();
        return this;
    }

    public ExportGuiWizardPage openExportFileWizard(){
        DelayUtils.sleep(100);
        expandMenu();
        waitforclickability(exportButton);
        exportButton.click();
        return new ExportGuiWizardPage(driver);
    }

    private NotificationWrapperPage openNotificationPanel() {
        waitforclickability(notificationButton);
        notificationButton.click();
        return new NotificationWrapperPage(driver);
    }

    public LanguageServicePage clearNotifications(){
        openNotificationPanel()
                .clearNotifications();
        closeNotificationPanel();
        return this;
    }

    private LanguageServicePage closeNotificationPanel(){
        waitforclickability(notificationButton);
        notificationButton.click();
        return this;
    }

    public int howManyNotifications(){
        int amountOfNotifications = openNotificationPanel()
                .waitForExportFinish()
                .amountOfNotifications();
        closeNotificationPanel();
        return amountOfNotifications;
    }

    public LanguageServicePage changeLanguageForEnglish(){
        String lang = driver.findElement(By.xpath("/html")).getAttribute("lang");
        if (!lang.equals("en")){
            openLoginPanel().changeLanguageForEnglish();
        }
        return this;
    }

    private boolean notificationIsOpen(){
        return notificationButton.findElement(By.xpath("./ancestor::*/div[contains(@class,'globalNotification')]")).getAttribute("class").contains("clicked");
    }

    public LanguageServicePage typeIdOfFirstServiceInSearch() {
        waitForVisibility(firstService);
        String idOfFirstElement = firstService.getAttribute("id");
        searchField.sendKeys(idOfFirstElement);
        return this;
    }
}

