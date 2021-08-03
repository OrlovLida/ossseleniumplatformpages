package com.oss.pages.faultmanagement;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;

public class FMDashboardPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(FMDashboardPage.class);
    private Input input;

    public FMDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open FM Dashboard")
    public static FMDashboardPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 90);

        String webURL = String.format("%s/#/dashboard/predefined/id/_FaultManagement", basicURL);
        driver.navigate().to(webURL);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", webURL);

        return new FMDashboardPage(driver, wait);
    }

    @Step
    public void searchForAlarmList() {
//        CommonList commonList = CommonList.create(driver,wait,"_UserViewsListALARM_MANAGEMENT");
//        commonList.callAction("search");
//        Input input = ComponentFactory.create("search", SEARCH_FIELD, driver, wait);
//        input.click();
//        input.setValue(Data.createSingleData("11"));
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement searchField = driver.findElement(By.xpath("//*[data-attributename='search_ga1zplwqxxv']"));
        searchField.click();
        searchField.sendKeys("11");
        searchField.sendKeys(Keys.ENTER);


    }

}
