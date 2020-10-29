package com.oss.pages.platform;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.listwidget.CommonList;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class GlobalSearchPage extends BasePage {

    private static final String OBJECTS_LIST_DATA_ATTRIBUTE_NAME = "objectsList";

    public GlobalSearchPage(WebDriver driver) {
        super(driver);
    }

    @Step("Expand {option} and select {viewName} from the drop-down list")
    public void expandShowOnAndChooseView(String objectName, String option, String viewName) {
        getResultsList().expandListElementKebab(objectName);
        DropdownList threeDotsList = DropdownList.create(driver, wait);
        threeDotsList.selectOption(option);
        threeDotsList.selectOptionWithId(viewName);
    }

    public CommonList getResultsList() {
        return CommonList.create(driver, wait, OBJECTS_LIST_DATA_ATTRIBUTE_NAME);
    }

}