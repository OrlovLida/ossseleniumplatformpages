package com.oss.pages.acd.scenarioSummaryView;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;

import io.qameta.allure.Step;

public class AbgadScenarioSummaryViewPage extends ScenarioSummaryBasePage {

    private static final Logger log = LoggerFactory.getLogger(AbgadScenarioSummaryViewPage.class);

    public AbgadScenarioSummaryViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open ABGAD Scenario Summary View")
    public static AbgadScenarioSummaryViewPage goToPage(WebDriver driver, String suffixURL, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 150);

        String pageUrl = String.format(suffixURL, basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new AbgadScenarioSummaryViewPage(driver, wait);
    }
}
