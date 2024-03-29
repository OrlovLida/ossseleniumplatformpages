package com.oss.pages.platform;

import com.comarch.oss.web.pages.FormAppPage;
import com.comarch.oss.web.pages.HierarchyViewPage;
import com.comarch.oss.web.pages.InputsWizardPage;
import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.comarch.oss.web.pages.languageservice.LanguageServicePage;
import com.comarch.oss.web.pages.toolsmanager.ToolsManagerPage;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.Popup;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.physical.LocationWizardPage;
import com.oss.pages.schedulerservice.SchedulerServicePage;
import io.qameta.allure.Step;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * @deprecated use {@link com.comarch.oss.web.pages.HomePage}
 */
@Deprecated(since = "4.0.x")
public class HomePage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(HomePage.class);
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

    // @FindBy(xpath = "//button[@data-original-title='Save bookmark']/i")
    @FindBy(xpath = "//i[contains(@class,'buttonIcon fa fa-floppy-o')]")
    private WebElement saveBookmarksButton;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Step("Go to Home Page")
    public HomePage goToHomePage(WebDriver driver, String basicURL) {
        DelayUtils.waitForPageToLoad(driver, wait);
        driver.get(String.format("%s/#/", basicURL));
        return new HomePage(driver);
    }

    public void openApplication(String categoryName, String applicationName) {
        ToolsManagerPage toolsManagerPage = new ToolsManagerPage(driver);
        ToolsManagerWindow toolsManagerWindow = toolsManagerPage.getToolsManager();
        toolsManagerWindow.openApplication(categoryName, applicationName);
    }

    public WebElement getLoginButton() {
        return loginButton;
    }

    public String getPageTitle() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        DelayUtils.waitForPageToLoad(driver, wait);
        return pageTitle.getText();
    }

    public String getPageTitle(WebDriverWait wait) {
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

    public Popup goToCreateBookmarkPopUp() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        DelayUtils.waitForVisibility(wait, saveBookmarksButton);
        saveBookmarksButton.click();
        return Popup.create(driver, wait);
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
        return new NewInventoryViewPage(driver, wait);
    }

    public HierarchyViewPage goToHierarchyViewPage(String url) {
        driver.get(url);
        return HierarchyViewPage.getHierarchyViewPage(driver, wait);
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
        Input searchField = getComponent(OLD_OBJECT_TYPE_DATA_ATTRIBUTE_NAME, Input.ComponentType.SEARCH_BOX);
        searchField.setSingleStringValue(objectType);
    }

    @Step("Set and select {object type}")
    public void setNewObjectType(String objectType) {
        Input searchField = getComponent(NEW_OBJECT_TYPE_DATA_ATTRIBUTE_NAME, Input.ComponentType.SEARCH_BOX);
        searchField.setSingleStringValue(objectType);
    }

    private Input getComponent(String componentId, Input.ComponentType componentType) {
        return ComponentFactory.create(componentId, componentType, driver, wait);
    }

    private String getHomePageLinkWithContext() {
        String href = logo.getAttribute("href");
        log.info("Get current link: {}", href);
        return href;
    }

    @Step("Go to Home Page")
    public void goToHomePageWithContext(WebDriver driver) {
        driver.get(getHomePageLinkWithContext());
    }

    @Step("Go to specific Page with context")
    public void goToSpecificPageWithContext(WebDriver driver, String view) {
        String link = getHomePageLinkWithContext();
        link = link.replace("/#/", view);
        log.info("Open link: {}", link);
        driver.get(link);
    }
}