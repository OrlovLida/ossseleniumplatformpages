package com.oss.pages.bigdata.dfe.serverGroup;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.dfe.BasePopupPage;
import com.oss.pages.bigdata.dfe.dictionary.EntryPopupPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddNewServerPopupPage extends BasePopupPage {

    private static final Logger log = LoggerFactory.getLogger(EntryPopupPage.class);
    private final String SERVER_NAME_LABEL = "serverName";
    private final String SERVER_ADDRESS_LABEL = "serverAddress";
    private final String USER_NAME_LABEL = "username";
    private final String PASSWORD_LABEL = "password";
    private final String DIRECTORY_LABEL = "directory";

    public AddNewServerPopupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void fillServerName(String serverName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(serverName, SERVER_NAME_LABEL);
        log.debug("Setting Server Name with {}", serverName);
    }

    public void fillServerAddress(String serverAddress) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(serverAddress, SERVER_ADDRESS_LABEL);
        log.debug("Setting Server Address with {}", serverAddress);
    }

    public void fillUserName(String userName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(userName, USER_NAME_LABEL);
        log.debug("Setting User Name with {}", userName);
    }

    public void fillPassword(String password) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(password, PASSWORD_LABEL);
        log.debug("Setting Password with {}", password);
    }

    public void fillDirectory(String directory) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(directory, DIRECTORY_LABEL);
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
        log.info("Filled Add New Serwer Popup fields");
    }

}
