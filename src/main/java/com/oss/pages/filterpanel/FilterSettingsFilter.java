package com.oss.pages.filterpanel;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.utils.DelayUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FilterSettingsFilter extends FilterSettings {

    public FilterSettingsFilter(WebDriver driver) {
        super(driver);
    }

    private static final String FILTERS_XPATH = "//div[@class='filters-element-list']/div[contains (@class, 'filters-element')]";
    private static final String SEARCH_FIELD_ID = "filters-search";
    private static final String APPLY_BUTTON_XPATH = "//div[@class='filters-component-container']//a[contains(@class, 'btn-primary btn-md')]";
    private static final String PARTIAL_FILTER_XPATH = "//div[@class='filter-label' and text()='";

    @Step("Type Filter Name in the Search Field")
    public FilterSettingsFilter findFilterInSearch(String filterName) {
        setValueOnSearchField(filterName);
        return this;
    }

    @Step("Select Filter")
    public FilterSettingsFilter selectFilter(String filterName) {
        filter(filterName).click();
        return this;
    }

    @Step("Mark filter as a favorite")
    public FilterSettingsFilter markAsFavorite(String filterName) {
        filter(filterName).findElement(By.xpath("./..//i")).click();
        return this;
    }

    @Step("Apply Filter")
    public FilterPanelPage applyFilter() {
        driver.findElement(By.xpath(APPLY_BUTTON_XPATH)).click();
        DelayUtils.waitForPageToLoad(driver, wait);
        return new FilterPanelPage(driver);
    }

    @Step("Checking that filter is visible")
    public boolean isFilterVisibleInFilterPanel(String filterName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return isFilterVisible(filterName);
    }

    @Step("Checking that filter is favorite")
    public boolean isFilterFavorite(String filterName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return filter(filterName).findElements(By.xpath("./..//i[@class='OSSIcon fa fa-star']")).size() == 1;
    }

    public int howManyFilters() {
        return driver.findElements(By.xpath(FILTERS_XPATH)).size();
    }

    private boolean isFilterVisible(String filterName) {
        return driver.findElements(By.xpath(PARTIAL_FILTER_XPATH + filterName + "']")).size() > 0;
    }

    private void setValueOnSearchField(String value) {
        SearchField searchField = (SearchField) getComponent(SEARCH_FIELD_ID, ComponentType.SEARCH_FIELD);
        searchField.typeValue(value);
    }

    private Input getComponent(String componentId, Input.ComponentType componentType) {
        return ComponentFactory.create(componentId, componentType, driver, wait);
    }

    private WebElement filter(String filterName) {
        return driver.findElement(By.xpath(PARTIAL_FILTER_XPATH + filterName + "']"));
    }
}
