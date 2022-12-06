package com.oss.pages.iaa.keycloak;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import com.oss.pages.platform.LoginPage;

import io.qameta.allure.Step;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class KeycloakPage extends BasePage {

    private static final String HTTP_URL_TO_KEYCLOAK_USERS = "%s/auth/admin/master/console/#/realms/OSS/users";
    private static final String IMPERSONATE_BUTTON_XPATH = "//td[@data-ng-click='impersonate(user.id)']";
    private static final String SEARCH_BUTTON_ID = "userSearch";
    private static final String INPUT_SEARCH_PANEL_XPATH = "//input[@placeholder='Search...']";

    public KeycloakPage(WebDriver driver) {
        super(driver);
    }

    @Step("Login to Keycloak")
    public void loginToKeycloak(String basicUrl) {
        String webURL = String.format(HTTP_URL_TO_KEYCLOAK_USERS, basicUrl);
        driver.navigate().to(webURL);
        LoginPage loginPage = new LoginPage(driver, webURL);
        loginPage.login(CONFIGURATION.getLogin(), CONFIGURATION.getPassword());
    }

    @Step("I navigate to Users Tab")
    private void navigateToUserTab(String basicUrl) {
        DelayUtils.sleep(3000);
        String webURL = String.format(HTTP_URL_TO_KEYCLOAK_USERS, basicUrl);
        driver.navigate().to(webURL);
        DelayUtils.sleep(3000);
    }

    @Step("I perform impersonate")
    private void findUserAndImpersonate(String userName) {
        fillSearchPanel(userName);
        clickSearchButton();
        clickImpersonateButton();
        DelayUtils.sleep(3000);
    }

    @Step("I click impersonate")
    private void clickImpersonateButton() {
        DelayUtils.sleep(3000);
        WebElement impersonateButton = driver.findElement(By.xpath(IMPERSONATE_BUTTON_XPATH));
        impersonateButton.click();
    }

    @Step("I click search button")
    private void clickSearchButton() {
        DelayUtils.sleep(1000);
        WebElement searchButton = driver.findElement(By.id(SEARCH_BUTTON_ID));
        searchButton.click();
    }

    @Step("I fill search panel")
    private void fillSearchPanel(String userName) {
        DelayUtils.sleep(3000);
        WebElement searchPanel = driver.findElement(By.xpath(INPUT_SEARCH_PANEL_XPATH));
        searchPanel.sendKeys(userName);
    }

    @Step("I close opened new tab and change back to main window")
    private void handleAllWindows() {
        String windowHandle = driver.getWindowHandle();
        Set<String> allWindowHandles = driver.getWindowHandles();
        String[] iterator = allWindowHandles.toArray(new String[0]);
        driver.switchTo().window(iterator[1]);
        driver.close();
        driver.switchTo().window(windowHandle);
        DelayUtils.sleep(1000);
    }

    @Step("I login as impersonated account")
    public void impersonateLogin(String basicUrl, String userName) {
        navigateToUserTab(basicUrl);
        findUserAndImpersonate(userName);
        handleAllWindows();
    }
}