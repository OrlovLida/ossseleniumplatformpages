package com.oss.pages.bigdata.dfe.serverGroup;

import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class ServerPopupPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ServerPopupPage.class);
    private final String SERVER_NAME_LABEL = "serverName";
    private final String SERVER_ADDRESS_LABEL = "serverAddress";
    private final String USER_NAME_LABEL = "username";
    private final String PASSWORD_LABEL = "password";
    private final String DIRECTORY_LABEL = "directory";

    private final Wizard serverWizard;

    public ServerPopupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        serverWizard = Wizard.createWizard(driver, wait);
    }

    public void fillServerName(String serverName) {
        serverWizard.setComponentValue(SERVER_NAME_LABEL, serverName, TEXT_FIELD);
        log.debug("Setting Server Name with {}", serverName);
    }

    public void fillServerAddress(String serverAddress) {
        serverWizard.setComponentValue(SERVER_ADDRESS_LABEL, serverAddress, TEXT_FIELD);
        log.debug("Setting Server Address with {}", serverAddress);
    }

    public void fillUserName(String userName) {
        serverWizard.setComponentValue(USER_NAME_LABEL, userName, TEXT_FIELD);
        log.debug("Setting User Name with {}", userName);
    }

    public void fillPassword(String password) {
        serverWizard.setComponentValue(PASSWORD_LABEL, password, TEXT_FIELD);
        log.debug("Setting Password with {}", password);
    }

    public void fillDirectory(String directory) {
        serverWizard.setComponentValue(DIRECTORY_LABEL, directory, TEXT_FIELD);
        log.debug("Setting Directory with {}", directory);
    }

    @Step("I fill Add New Server Popup fields with " +
            "Server Name: {serverName}," +
            "Server Address: {serverAddress}," +
            "User Name: {userName}," +
            "Password: {password}," +
            "Directory: {directory}")
    public void fillAddNewServerPopup(String serverName, String serverAddress,
                                      String userName, String password, String directory) {
        fillServerName(serverName);
        fillServerAddress(serverAddress);
        fillUserName(userName);
        fillPassword(password);
        fillDirectory(directory);
        log.info("Filled Add New Server Popup fields");
    }

    @Step("I click Save")
    public void clickSave() {
        serverWizard.clickSave();
        log.info("Finishing by clicking 'Save'");
    }
}
