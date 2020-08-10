package com.oss.pages.filtermanager;

import com.oss.framework.components.ComponentFactory;
import com.oss.framework.components.Input;
import com.oss.framework.components.SearchField;
import com.oss.framework.utils.DelayUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class ShareFilterPage  extends FilterManagerPage {

    public ShareFilterPage(WebDriver driver){
        super(driver);
    }

    private final String SEARCH_ID = "userSearch";
    private final String LEVEL_ELEMENT_PARTIAL_XPATH = "//li[contains (@class,  'levelElement')]//*[text() = '";
    private final String QUIT_MODAL_BUTTON_XPATH = "//a[@class = 'quitModalButton']";

    private WebElement getLevelElementByName(String name){
        return driver.findElement(By.xpath(LEVEL_ELEMENT_PARTIAL_XPATH + name + "']/../.."));
    }

    private WebElement getReadButtonByName(String name){
        return getLevelElementByName(name).findElement(By.xpath(".//button//*[text() = 'R']/../.."));
    }

    private WebElement getWriteButtonByName(String name){
        return getLevelElementByName(name).findElement(By.xpath(".//button//*[text() = 'W']/../.."));
    }
    private Input getComponent(String componentId, Input.ComponentType componentType) {
        return ComponentFactory.create(componentId,componentType,driver,wait);
    }

    @Step("Type user name in search")
    public ShareFilterPage typeUserNameInSearch(String userName){
        SearchField searchField = (SearchField) getComponent(SEARCH_ID, Input.ComponentType.SEARCH_FIELD);
        searchField.typeValue(userName);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Close Share View")
    public  FilterManagerPage closeShareView(){
        driver.findElement(By.xpath(QUIT_MODAL_BUTTON_XPATH)).click();
        DelayUtils.waitForPageToLoad(driver, wait);
        return new FilterManagerPage(driver);
    }

    @Step("Share for user")
    public  ShareFilterPage shareForUser(String userName){
        DelayUtils.waitForPageToLoad(driver, wait);
        getLevelElementByName("all users").click();
        getReadButtonByName(userName).click();
        DelayUtils.waitForPageToLoad(driver, wait);
        getWriteButtonByName(userName).click();
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }



}
