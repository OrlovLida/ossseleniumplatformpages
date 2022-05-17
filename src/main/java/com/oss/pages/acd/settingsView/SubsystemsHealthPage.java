package com.oss.pages.acd.settingsView;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.icons.StatusIcon;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class SubsystemsHealthPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(SubsystemsHealthPage.class);

    public HashMap<String, Boolean> iconsMap = new HashMap<>();

    public SubsystemsHealthPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I go to Settings View")
    public static SubsystemsHealthPage goToSettingsView(WebDriver driver, String settingsViewSuffixURL, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 150);

        String pageUrl = String.format(settingsViewSuffixURL, basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new SubsystemsHealthPage(driver, wait);
    }

    @Step("I go to Subsystems Health tab")
    public void goToSubsystemsHealthTab(String tabId, String tabLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsWidget.createById(driver, wait, tabId).selectTabByLabel(tabLabel);
        log.info("I opened Subsystems Health tab");
    }

    @Step("I check if subsystems are up and running")
    public boolean isSubsystemUpAndRunning() {

        for (StatusIcon.IconItem icon : StatusIcon.createStatusIcon(driver, wait).getIcons()) {
            iconsMap.put(icon.getIconLabel(), icon.isIconGreen());
        }

        iconsMap.entrySet()
                .stream()
                .forEach(
                        pair -> {
                            if (pair.getValue() != true) {
                                log.info("Subsystem - {} is down", pair.getKey());
                            } else {
                                log.info("Subsystem - {} is up and running", pair.getKey());
                            }
                        }
                );

        if (iconsMap.containsValue(false)) {
            log.info("At least one subsystem is DOWN");
            iconsMap.clear();
            return false;
        }
        log.info("All subsystems are up and running");
        iconsMap.clear();
        return true;
    }
}
