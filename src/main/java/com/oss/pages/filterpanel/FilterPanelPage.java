package com.oss.pages.filterpanel;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.DelayUtils;
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

    private final String SELECTED_FILTERS_INFO_XPATH = "//div[@class='selected-filter-info']";
    private final String SAVE_BUTTON_CLASS_NAME = "save-buttons-dropdown";
    private final String SAVE_AS_A_NEW_FILTER_ID = "save_as_new_filter";
    private final String SAVE_FILTER_ID = "save_filter";


    @Step("Open Filter Settings by clicking on Manage filters icon")
    public FilterSettings openFilterSettings() {
        clickOnManageFiltersIcon();
        return new FilterSettings(driver);
    }

    @Step("Type Value in Location.Id Text Field")
    public FilterPanelPage typeValueInLocationIdInput(String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        setValueOnFilterInput("id", TEXT_FIELD, value);
        return this;
    }

    @Step("Change Value in Location.Id Text Field")
    public FilterPanelPage changeValueInLocationIdInput(String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        clearFilterInput("id", TEXT_FIELD);
        setValueOnFilterInput("id", TEXT_FIELD, value);
        return this;
    }

    @Step("Change Value in Location.Name Text Field")
    public FilterPanelPage changeValueInLocationNameInput(String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        clearFilterInput("name", TEXT_FIELD);
        setValueOnFilterInput("name", TEXT_FIELD, value);
        return this;
    }

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

    @Step("Save filter as a new one")
    public FilterPanelPage saveFilterAs(String filterName) {
        clickOnSaveButton();
        chooseOptionFromDropDownList(SAVE_AS_A_NEW_FILTER_ID);
        typeFilterNameAndSave(filterName);
        return this;
    }

    @Step("Save existing filter")
    public FilterPanelPage saveFilter() {
        clickOnSaveButton();
        chooseOptionFromDropDownList(SAVE_FILTER_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Apply Filter")
    public FilterPanelPage applyFilter() {
        advancedSearch.clickApply();
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Get value of LocaionId input")
    public String getValueOfLocationIdInput() {
        return getValueOfFilterInput("id", TEXT_FIELD);
    }

    public boolean isFilterApplied(String filterName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getNameOfSelectedFilter().equals(filterName);
    }

    private String getNameOfSelectedFilter() {
        return driver.findElement(By.xpath(SELECTED_FILTERS_INFO_XPATH)).getText();
    }

    private void clickOnSaveButton() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createByIcon(driver, "fa fa-save", SAVE_BUTTON_CLASS_NAME).click();
    }

    private void chooseOptionFromDropDownList(String OptionId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        DropdownList.create(driver, wait).selectOptionWithId(OptionId);
    }

    private void typeFilterNameAndSave(String filterName) {
        driver.findElement(By.xpath("//input/../label[text()='Name']/../input")).sendKeys(filterName); // wait fo ID
        driver.findElement(By.xpath("//div[@class='buttons']/a[text()='Save']")).click(); // wait fo ID
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void clickOnManageFiltersIcon() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createByIcon(driver, "fa-sliders", "btn-toggle-filters").click();
    }

    private void setValueOnFilterInput(String componentId, Input.ComponentType componentType, String value) {
        advancedSearch.getComponent(componentId, componentType).clearByAction();
        advancedSearch.getComponent(componentId, componentType).setSingleStringValue(value);
    }

    private void clearFilterInput(String componentId, Input.ComponentType componentType) {
        advancedSearch.getComponent(componentId, componentType).clearByAction();
    }

    private String getValueOfFilterInput(String componentId, Input.ComponentType componentType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        AdvancedSearch advancedSearch = new AdvancedSearch(driver, wait);
        return advancedSearch.getComponent(componentId, componentType).getStringValue();
    }
}
