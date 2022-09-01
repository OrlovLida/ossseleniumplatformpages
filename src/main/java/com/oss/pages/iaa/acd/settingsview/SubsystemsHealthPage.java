package com.oss.pages.iaa.acd.settingsview;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.icons.StatusIcon;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.acd.BaseACDPage;

import io.qameta.allure.Step;

public class SubsystemsHealthPage extends BaseACDPage {

    private static final Logger log = LoggerFactory.getLogger(SubsystemsHealthPage.class);

    private final HashMap<String, Boolean> iconsMap = new HashMap<>();

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

    @Step("I check if subsystems are up and running")
    public boolean isSubsystemUpAndRunning() {

        for (StatusIcon.IconItem icon : StatusIcon.createStatusIcon(driver, wait).getIcons()) {
            iconsMap.put(icon.getIconLabel(), icon.isIconGreen());
        }

        iconsMap.entrySet()
                .stream()
                .forEach(
                        pair -> {
                            if (Boolean.TRUE.equals(pair.getValue())) {
                                log.info("Subsystem - {} is up and running", pair.getKey());
                            } else {
                                log.info("Subsystem - {} is down", pair.getKey());
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
