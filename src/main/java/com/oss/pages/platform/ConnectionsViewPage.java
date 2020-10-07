package com.oss.pages.platform;

import com.oss.framework.mainheader.ButtonPanel;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class ConnectionsViewPage extends BasePage {

    public ConnectionsViewPage (WebDriver driver) {
        super(driver);
    }

    @Step("Open Connections View")
    public static ConnectionsViewPage goToConnectionsViewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/views/management/views/connectivity-view/" +
                "?perspective=LIVE" , basicURL));
        return new ConnectionsViewPage(driver);
    }

    @Step("Open save configuration wizard for page")
    public SaveConfigurationWizard openSavePageConfigurationWizard(){
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickOnIcon("fa fa-fw fa-floppy-o");
        return new SaveConfigurationWizard(driver);
    }

    @Step("Open choose configuration wizard for page")
    public ChooseConfigurationWizard openChooseConfigurationForPageWizard(){
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickOnIcon("fa fa-fw fa-cog");
        return new ChooseConfigurationWizard(driver);
    }

    @Step("Open download configuration wizard for page")
    public ChooseConfigurationWizard openDownloadConfigurationForPageWizard(){
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickOnIcon("fa fa-fw fa-download");
        return new ChooseConfigurationWizard(driver);
    }
}
