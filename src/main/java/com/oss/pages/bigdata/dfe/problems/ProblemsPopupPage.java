package com.oss.pages.bigdata.dfe.problems;

import com.oss.pages.bigdata.dfe.BasePopupPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProblemsPopupPage extends BasePopupPage {

    private static final Logger log = LoggerFactory.getLogger(ProblemsPopupPage.class);

    public ProblemsPopupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I fill Problems Popup fields with name: {name} and description: {description}")
    public void fillProblemsPopup(String name, String description) {
        fillName(name);
        fillDescription(description);
        log.info("Filled Problems Popup fields");
    }
}
