package com.oss.pages.filterpanel;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FilterSettings extends BasePage {

    public FilterSettings(WebDriver driver){
        super(driver);
    }

    private final String PARTIAL_TAB_XPATH = "//div[@class='filters-buttons-container']/div[text()='";

    @Step("Change Tab to Filters Tab")
    public FilterSettingsFilter changeTabToFilters(){
        changeTab("Saved Filters");
        DelayUtils.waitForPageToLoad(driver, wait);
        return new FilterSettingsFilter(driver);
    }

    private void changeTab(String tabName){
        driver.findElement(By.xpath(PARTIAL_TAB_XPATH + tabName + "']")).click();
    }

}
