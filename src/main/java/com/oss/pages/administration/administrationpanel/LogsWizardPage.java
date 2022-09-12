package com.oss.pages.administration.administrationpanel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.configuration.Configuration;
import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.wizard.Wizard;

public class LogsWizardPage extends BaseAdminPanelPage {

    private static final String LOGS_WIZARD_ID = "ADMINISTRATIVE_PANEL_LOGS_WIZARD_ID";

    private final Wizard logsWizard;

    public LogsWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        logsWizard = Wizard.createByComponentId(driver, wait, LOGS_WIZARD_ID);
    }

    public void toggleNode(String path) {
        getTree().getNodeByPath(path).toggleNode();
    }

    private TreeComponent getTree() {
        return logsWizard.getTreeComponent();
    }

    public ModulesPage clickAcceptInLogsWizard() {
        logsWizard.clickAccept();
        log.info("Clicking Accept in Logs Wizard");
        logsWizard.waitToClose();
        return new ModulesPage(driver, wait);
    }

    public String getPathToFileName(String moduleName, String logFileName) {
        return getMainNodePath() + "." + moduleName.toLowerCase() + "." + logFileName;
    }

    private String getMainNodePath() {
        String[] mainNodePath = Configuration.CONFIGURATION.getUrl().split(":");
        return mainNodePath[1].substring(2);
    }
}
