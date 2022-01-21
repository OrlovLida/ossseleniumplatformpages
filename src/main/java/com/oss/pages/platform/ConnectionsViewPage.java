package com.oss.pages.platform;

import com.oss.framework.components.portals.SaveConfigurationWizard.Field;
import com.oss.framework.components.mainheader.ButtonPanel;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class ConnectionsViewPage extends BasePage {

    public ConnectionsViewPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open Connections View")
    public static ConnectionsViewPage goToConnectionsViewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/views/management/views/connectivity-view/" +
                "?perspective=LIVE", basicURL));
        return new ConnectionsViewPage(driver);
    }

    @Step("Save configuration for page")
    public ConnectionsViewPage savePageConfiguration(Field... fields) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).openSaveConfigurationWizard().save();
        return this;
    }

    @Step("Save new configuration for page")
    public ConnectionsViewPage saveNewPageConfiguration(String configurationName, Field... fields) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).openSaveConfigurationWizard().saveAsNew(configurationName, fields);
        return this;
    }

    @Step("Apply configuration for page")
    public ConnectionsViewPage applyConfigurationForPage(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).openChooseConfigurationWizard().chooseConfiguration(configurationName).apply();
        return this;
    }

    @Step("Download configuration for page")
    public ConnectionsViewPage downloadConfigurationForPage(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).openDownloadConfigurationWizard().chooseConfiguration(configurationName).apply();
        return this;
    }
}
