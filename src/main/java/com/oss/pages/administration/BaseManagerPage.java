package com.oss.pages.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public abstract class BaseManagerPage extends BasePage {

    private static final String LINK_TEXT = "Open";
    private static final String ROW_ATTRIBUTE_NAME = "Name";
    private static final String DELETE_LABEL = "Delete";

    public abstract String getListId();

    protected BaseManagerPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Search element in the list")
    public void searchInList(String searchFieldId, String searchedName) {
        ComponentFactory.create(searchFieldId, driver, wait)
                .setSingleStringValue(searchedName);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Open element from the list")
    public void openListElement(String elementName) {
        getList().getRowContains(ROW_ATTRIBUTE_NAME, elementName).clickLink(LINK_TEXT);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Check if list contains any elements")
    public boolean isAnyElementOnList() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return !getList().hasNoData();
    }

    @Step("Click Delete Element")
    public void clickDeleteElement(String confirmDeleteButtonId, String elementName) {
        getList().getRow(ROW_ATTRIBUTE_NAME, elementName).callActionByLabel(DELETE_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createById(driver, confirmDeleteButtonId).click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Check if element is deleted")
    public boolean isElementDeleted(String elementName) {
        return !getList().isRowDisplayed(ROW_ATTRIBUTE_NAME, elementName);
    }

    @Step("Click Create New Element Button")
    public void clickCreateNewElement(String actionLabel) {
        ButtonContainer.create(driver, wait).callActionByLabel(actionLabel);
    }

    protected CommonList getList() {
        return CommonList.create(driver, wait, getListId());
    }
}
