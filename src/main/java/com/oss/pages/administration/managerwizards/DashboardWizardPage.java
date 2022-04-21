package com.oss.pages.administration.managerwizards;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.qameta.allure.Step;

public class DashboardWizardPage extends BaseWizardPage {

    private static final Logger log = LoggerFactory.getLogger(DashboardWizardPage.class);

    private static final String ADD_CUSTOM_DASHBOARD_WIZARD_ID = "addCustomDashboardForm_prompt-card";
    private static final String DASHBOARD_NAME_ID = "dashboardName";

    public DashboardWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Fill dashboard name")
    public void fillDashboardName(String dashboardName) {
        fillName(DASHBOARD_NAME_ID, dashboardName);
        log.info("Dashboard name filled with name: {}", dashboardName);
    }

    @Override
    public String getWizardId() {
        return ADD_CUSTOM_DASHBOARD_WIZARD_ID;
    }
}
