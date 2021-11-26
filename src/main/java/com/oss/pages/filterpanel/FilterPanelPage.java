package com.oss.pages.filterpanel;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.components.search.SearchPanel;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.*;

public class FilterPanelPage extends BasePage {

    public FilterPanelPage(WebDriver driver) {
        super(driver);
    }

    private AdvancedSearch advancedSearch = new AdvancedSearch(driver, wait);


    @Step("Set value")
    public FilterPanelPage setValue(Input.ComponentType componentType, String componentId, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        clearFilterInput(componentId, componentType);
        setValueOnFilterInput(componentId, componentType, value);
        return this;
    }

    @Step("Set value on combo with Tags ")
    public FilterPanelPage setValueOnComboWithTags(String componentId, String searchComponentId, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        advancedSearch.getComponent(componentId, COMBOBOX).click();
        setValueOnFilterInput(searchComponentId, COMBOBOX, value);
        return this;
    }

    @Step("Apply Filter")
    public FilterPanelPage applyFilter() {
        advancedSearch.clickApply();
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    private void setValueOnFilterInput(String componentId, Input.ComponentType componentType, String value) {
        advancedSearch.getComponent(componentId, componentType).clearByAction();
        advancedSearch.getComponent(componentId, componentType).setSingleStringValue(value);
    }

    private void clearFilterInput(String componentId, Input.ComponentType componentType) {
        advancedSearch.getComponent(componentId, componentType).clearByAction();
    }

}
