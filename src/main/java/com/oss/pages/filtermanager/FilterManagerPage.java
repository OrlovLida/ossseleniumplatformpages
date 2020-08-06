package com.oss.pages.filtermanager;

import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class FilterManagerPage extends BasePage {

    public FilterManagerPage(WebDriver driver){
        super(driver);
    }

    @Step("Open Filter Manager Page")
    public static FilterManagerPage goToFilterManagerPage(WebDriver driver, String baseURL){
        driver.get(String.format("%s/#/view/management/views/filter-view" + "?perspective=LIVE", baseURL));
        return new FilterManagerPage(driver);
    }


}
