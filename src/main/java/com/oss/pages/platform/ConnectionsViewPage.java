package com.oss.pages.platform;

import com.oss.pages.platform.configuration.ChooseConfigurationWizard;
import com.oss.pages.platform.configuration.SaveConfigurationWizard;
import com.oss.pages.platform.configuration.SaveConfigurationWizard.Field;
import com.oss.framework.components.mainheader.ButtonPanel;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class ConnectionsViewPage extends BasePage {

    private static final String CHOOSE_CONFIG_BUTTON_ID = "ButtonChooseViewConfig";
    private static final String SAVE_VIEW_CONFIG_BUTTON_ID = "ButtonSaveViewConfig";

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
        ButtonPanel.create(driver, wait).clickButton(SAVE_VIEW_CONFIG_BUTTON_ID);
        getSaveConfigurationWizard().save(fields);
        return this;
    }

    @Step("Save new configuration for page")
    public ConnectionsViewPage saveNewPageConfiguration(String configurationName, Field... fields) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickButton(SAVE_VIEW_CONFIG_BUTTON_ID);
        getSaveConfigurationWizard().saveAsNew(configurationName, fields);
        return this;
    }

    @Step("Apply configuration for page")
    public ConnectionsViewPage applyConfigurationForPage(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickButton(CHOOSE_CONFIG_BUTTON_ID);
        getChooseConfigurationWizard().chooseConfiguration(configurationName).apply();
        return this;
    }

    @Step("Download configuration for page")
    public ConnectionsViewPage downloadConfigurationForPage(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickButton(CHOOSE_CONFIG_BUTTON_ID);
        getChooseConfigurationWizard().chooseConfiguration(configurationName).apply();
        return this;
    }
    private SaveConfigurationWizard getSaveConfigurationWizard(){
        return SaveConfigurationWizard.create(driver, wait);
    }

    private ChooseConfigurationWizard getChooseConfigurationWizard(){
        return ChooseConfigurationWizard.create(driver,wait);
    }
}
