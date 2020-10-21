package com.oss.pages.platform;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.listwidget.CommonList;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class GlobalSearchPage extends BasePage {

    public GlobalSearchPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click three dots")
    public GlobalSearchPage clickThreeDots(String objectName) {
        CommonList objectsList = CommonList.create(driver, wait, "objectsList");
        objectsList.expandListElementKebab(objectName);
        return new GlobalSearchPage(driver);
    }

    @Step("Delete object")
    public void deleteObject() {
        DropdownList threeDotsList = DropdownList.create(driver, wait);
        threeDotsList.selectOption("Edit");
        threeDotsList.selectOptionWithId("Delete");
    }
}