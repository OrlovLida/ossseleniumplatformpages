package com.oss.pages.languageservice;

import com.oss.pages.BasePage;
import com.oss.pages.exportguiwizard.ExportGuiWizardPage;
import com.oss.pages.platform.LoginPanelPage;
import com.oss.pages.platform.NotificationWrapperPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class LanguageServicePage extends BasePage {

    @Step("Open Language Service Page")
    public static LanguageServicePage goToLanguageServicePage(WebDriver driver, String baseURL){
        driver.get(String.format("%s/#/views/languagesservice/views/translations" +
                "?perspective=LIVE", baseURL));
        return new LanguageServicePage(driver);
    }

    public LanguageServicePage(WebDriver driver) {super(driver);}

    @FindBy(xpath = "//a[contains(@class, 'loginButton')]")
    private WebElement loginButton;
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

    private String MENU_BUTTON_ID = "frameworkCustomButtonsGroup";

    private ExportGuiWizardPage exportGuiWizard;
//    private boolean existsElement(List<WebElement> element) {return element.size() != 0;}

    private LanguageServicePage expandMenu() {
        waitForComponent("//div[@id='"+MENU_BUTTON_ID+"']");
        driver.findElement(By.id(MENU_BUTTON_ID)).click();
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

    @Step("Open Export File Wizard")
    public ExportGuiWizardPage openExportFileWizard(){
        waitForInvisibilityOfLoadbars();
        expandMenu();
        waitForVisibility(exportButton);
        exportButton.click();
        return new ExportGuiWizardPage(driver);
    }

    private NotificationWrapperPage openNotificationPanel() {
        waitforclickability(notificationButton);
        notificationButton.click();
        return new NotificationWrapperPage(driver);
    }

    @Step("Clear Notifications")
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

    @Step("Type ID of First Service in Search")
    public LanguageServicePage typeIdOfFirstServiceInSearch() {
        waitForVisibility(firstService);
        String idOfFirstElement = firstService.getAttribute("id");
        searchField.sendKeys(idOfFirstElement);
        return this;
    }
}

