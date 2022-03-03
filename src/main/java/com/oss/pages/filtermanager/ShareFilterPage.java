package com.oss.pages.filtermanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;

import io.qameta.allure.Step;

public class ShareFilterPage extends FilterManagerPage {
    
    public ShareFilterPage(WebDriver driver) {
        super(driver);
    }
    
    private static final String SEARCH_ID = "userSearch";
    private static final String LEVEL_ELEMENT_PARTIAL_XPATH = "//li[contains (@class,  'levelElement')]//*[text() = '";
    private static final String QUIT_MODAL_BUTTON_XPATH = "//span[@class = 'icon-button button-close']";
    
    @Step("Type user name in search")
    public ShareFilterPage typeUserNameInSearch(String userName) {
        Input searchField = getComponent(SEARCH_ID, Input.ComponentType.SEARCH_BOX);
        searchField.setSingleStringValue(userName);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }
    
    @Step("Close Share View")
    public FilterManagerPage closeShareView() {
        DelayUtils.waitForVisibility(wait, driver.findElement(By.xpath(QUIT_MODAL_BUTTON_XPATH)));
        driver.findElement(By.xpath(QUIT_MODAL_BUTTON_XPATH)).click();
        DelayUtils.waitForPageToLoad(driver, wait);
        return new FilterManagerPage(driver);
    }
    
    @Step("Share for user")
    public ShareFilterPage shareForUser(String userName, String permission) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getLevelElementByName("all users").click();
        if (permission.equals("R")) {
            DelayUtils.waitForClickability(wait, getReadButtonByName(userName));
            getReadButtonByName(userName).click();
        } else if (permission.equals("W")) {
            DelayUtils.waitForClickability(wait, getWriteButtonByName(userName));
            getWriteButtonByName(userName).click();
        }
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }
    
    private WebElement getLevelElementByName(String name) {
        return driver.findElement(By.xpath(LEVEL_ELEMENT_PARTIAL_XPATH + name + "']/../.."));
    }
    
    private WebElement getReadButtonByName(String name) {
        return getLevelElementByName(name).findElement(By.xpath(".//button//*[text() = 'R']/../.."));
    }
    
    private WebElement getWriteButtonByName(String name) {
        return getLevelElementByName(name).findElement(By.xpath(".//button//*[text() = 'W']/../.."));
    }
    
    private Input getComponent(String componentId, Input.ComponentType componentType) {
        return ComponentFactory.create(componentId, componentType, driver, wait);
    }
}
