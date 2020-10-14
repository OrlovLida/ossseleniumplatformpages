package com.oss.pages.platform;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.PopupV2;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.languageservice.LanguageServicePage;
import com.oss.pages.schedulerservice.SchedulerServicePage;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.pages.BasePage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.physical.LocationWizardPage;


public class HomePage extends BasePage {

    @FindBy(className = "oss-header-logo")
    private WebElement logo;

    @FindBy(className = "globalNotification")
    private WebElement globalNotificationButton;

    @FindBy(className = "loginButton")
    private WebElement loginButton;

    @FindBy(className = "titleText")
    private WebElement pageTitle;

    @FindBy(css = "span.notificationType")
    private WebElement numberOfNotificationsLabel;

    @FindBy(xpath = "//button[@data-original-title='Save bookmark']/i")
    //@FindBy (xpath = "//i[contains(@class,'buttonIcon fa fa-floppy-o')]")
    private WebElement saveBookmarksButton;

    public HomePage(WebDriver driver) {
        super(driver);
        WebDriverWait wait = new WebDriverWait(driver, 45);
        DelayUtils.waitForVisibility(wait, logo);
    }

    @Step("Open Home Page")
    public static HomePage goToHomePage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/" +
                "?perspective=LIVE", basicURL));
        return new HomePage(driver);
    }

    public WebElement getLoginButton() {
        return loginButton;
    }

    public String getPageTitle() {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        DelayUtils.waitForVisibility(wait, pageTitle);
        return pageTitle.getText();
    }

    public String getNumberOfNotifications() {
        try {
            return numberOfNotificationsLabel.getText();

        } catch (NoSuchElementException e) {
            return "0";
        }
    }

    public FormAppPage goToFormPage(String url) {
        driver.get(url);
        return new FormAppPage(driver);
    }

    public PopupV2 goToCreateBookmarkPopUp() {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        DelayUtils.waitForVisibility(wait, saveBookmarksButton);
        saveBookmarksButton.click();
        return new PopupV2(driver);
    }

    public InputsWizardPage goToInputsWizardPage(String url) {
        driver.get(url);
        return new InputsWizardPage(driver);
    }

    public DeviceWizardPage goToDeviceWizardPage(String url) {
        driver.get(url);
        return new DeviceWizardPage(driver);
    }

    public LocationWizardPage goToLocationWizardPage(String url) {
        driver.get(url);
        return new LocationWizardPage(driver);
    }

    public NewInventoryViewPage goToInventoryViewPage(String url) {
        driver.get(url);
        return new NewInventoryViewPage(driver);
    }

    public HierarchyViewPage goToHierarchyViewPage(String url) {
        driver.get(url);
        return new HierarchyViewPage(driver);
    }

    public LanguageServicePage goToLanguageServicePage(String url) {
        driver.get(url);
        return new LanguageServicePage(driver);
    }

    public SchedulerServicePage goToSchedulerServicePage(String url) {
        driver.get(url);
        return new SchedulerServicePage(driver);
    }


    //temporary
    String searchObjectTypeTxtXpath = ".//input[(@data-attributename=\"SearchUserViewsByType\")]";
    private String objectTypeListXpath = "//div[contains(text(),'%s')]/..";
    //temporary

    @Step("Type object type")
    public HomePage typeObjectType(String objectType) {
        DelayUtils.waitByXPath(wait, searchObjectTypeTxtXpath);
        driver.findElement(By.xpath(searchObjectTypeTxtXpath)).sendKeys(objectType);
        driver.findElement(By.xpath(searchObjectTypeTxtXpath)).sendKeys(Keys.ENTER);
        return new HomePage(driver);
    }

    @Step("Confirm object type")
    public OldInventoryViewPage confirmObjectType(String expectedObjectType) {
        String objectTypeList = String.format(objectTypeListXpath, expectedObjectType);
        DelayUtils.waitByXPath(wait, objectTypeList);
        driver.findElement(By.xpath(objectTypeList)).click();
        return new OldInventoryViewPage(driver);
    }

}

