package com.oss.pages.platform;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.components.portals.PopupV2;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.languageservice.LanguageServicePage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.physical.LocationWizardPage;
import com.oss.pages.schedulerservice.SchedulerServicePage;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;


public class HomePage extends BasePage {

    private static final String OLD_OBJECT_TYPE_DATA_ATTRIBUTE_NAME = "SearchUserViewsByType";
    private static final String NEW_OBJECT_TYPE_DATA_ATTRIBUTE_NAME = "SearchGraphqlTypes";

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

    @Step("Go to Home Page")
    public HomePage goToHomePage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/", basicURL));
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

    @Step("Set and select {object type}")
    public void setOldObjectType(String objectType) {
        SearchField searchField = (SearchField) getComponent(OLD_OBJECT_TYPE_DATA_ATTRIBUTE_NAME, Input.ComponentType.SEARCH_FIELD);
        searchField.typeValue(objectType);
        searchField.chooseFirstResult(Data.createFindFirst(objectType));
    }

    @Step("Set and select {object type}")
    public void setNewObjectType(String objectType) {
        SearchField searchField = (SearchField) getComponent(NEW_OBJECT_TYPE_DATA_ATTRIBUTE_NAME, Input.ComponentType.SEARCH_FIELD);
        searchField.typeValue(objectType);
        searchField.chooseFirstResult(Data.createFindFirst(objectType));
    }

    private Input getComponent(String componentId, Input.ComponentType componentType) {
        return ComponentFactory.create(componentId, componentType, driver, wait);
    }

}

