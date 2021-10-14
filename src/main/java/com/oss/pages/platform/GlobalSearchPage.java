package com.oss.pages.platform;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.listwidget.CommonList;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class GlobalSearchPage extends BasePage {

    private static final String OBJECTS_LIST_DATA_ATTRIBUTE_NAME = "objectsList";
    private static final String OBJECT_TYPE_FILTER_COMBOBOX_ID = "filteringWidget";

    public GlobalSearchPage(WebDriver driver) {
        super(driver);
    }

    @Step("Expand {option} and select {viewName} from the drop-down list")
    public void expandShowOnAndChooseView(String objectName, String option, String viewName) {
        getResultsList().expandListElementKebab(objectName);
        DropdownList threeDotsList = DropdownList.create(driver, wait);
        threeDotsList.selectOptionWithId(option);
        threeDotsList.selectOptionWithId(viewName);
    }

    @Step("Filter by object type {objectType}")
    public void filterObjectType(String objectType) {
        Input input = ComponentFactory.create(OBJECT_TYPE_FILTER_COMBOBOX_ID, ComponentType.MULTI_COMBOBOX, driver, wait);
        input.setSingleStringValue(objectType);
    }

    public CommonList getResultsList() {
        return CommonList.create(driver, wait, OBJECTS_LIST_DATA_ATTRIBUTE_NAME);
    }

}
