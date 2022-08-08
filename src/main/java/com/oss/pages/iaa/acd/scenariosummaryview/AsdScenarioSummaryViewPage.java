package com.oss.pages.iaa.acd.scenariosummaryview;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;

import io.qameta.allure.Step;

public class AsdScenarioSummaryViewPage extends ScenarioSummaryBasePage {

    private static final Logger log = LoggerFactory.getLogger(AsdScenarioSummaryViewPage.class);

    public AsdScenarioSummaryViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open ASD Scenario Summary View")
    public static AsdScenarioSummaryViewPage goToPage(WebDriver driver, String suffixURL, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 150);

        String pageUrl = String.format(suffixURL, basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new AsdScenarioSummaryViewPage(driver, wait);
    }
}