package com.oss.pages.iaa.servicedesk.infomanagement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.pages.iaa.servicedesk.BaseSDPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.INFO_MANAGEMENT_URL_PATTERN;

public class ConfigurationPanelPage extends BaseSDPage {

    private static final String CONFIGURATION_PANEL_URL = "mail-box/configuration";
    private static final String MAILBOXES_COMMON_LIST_ID = "mailbox-list-id";
    private static final String CREATE_NEW_CONFIGURATION_ID = "create-mailbox";
    private static final String WIZARD_ID = "mailbox-wizard-view_prompt-card";
    private static final String NAME_LABEL = "Name";
    private static final String EMAIL_LABEL = "E-mail";
    private static final String STATUS_LABEL = "Status";
    private static final String EDIT_BUTTON_ICON_LABEL = "EDIT";
    private static final String DELETE_BUTTON_ICON_LABEL = "DELETE";
    private static final String DELETE_BUTTON_LABEL = "Delete";
    private static final String DISABLE_BUTTON_LABEL = "Disable";

    public ConfigurationPanelPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public ConfigurationPanelPage goToConfigurationPanelPage(String basicUrl) {
        String url = String.format(INFO_MANAGEMENT_URL_PATTERN, basicUrl, CONFIGURATION_PANEL_URL);
        openPage(driver, url);
        return new ConfigurationPanelPage(driver, wait);
    }

    @Step("Click Create New Configuration")
    public SDWizardPage clickCreateNewConfiguration() {
        Button.createById(driver, CREATE_NEW_CONFIGURATION_ID).click();
        log.info("Clicking Create New Configuration");
        return new SDWizardPage(driver, wait, WIZARD_ID);
    }

    @Step("Check if Mailboxes table is empty")
    public boolean isMailboxesTableEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getMailboxesList().hasNoData();
    }

    @Step("Click Edit Object with name: {objectName}")
    public SDWizardPage clickEditOnObjectWithName(String objectName) {
        getMailboxesList().getRow(NAME_LABEL, objectName).callActionIcon(EDIT_BUTTON_ICON_LABEL);
        return new SDWizardPage(driver, wait, WIZARD_ID);
    }

    @Step("Click Disable Object with name: {objectName}")
    public void clickDisableOnObjectWithName(String objectName) {
        getMailboxesList().getRow(NAME_LABEL, objectName).callAction(DISABLE_BUTTON_LABEL);
    }

    @Step("Click Delete object with name {objectName}")
    public void clickDeleteOnObjectWithName(String objectName) {
        getMailboxesList().getRow(NAME_LABEL, objectName).callActionIcon(DELETE_BUTTON_ICON_LABEL);
        log.info("Clicking Delete object with name {}", objectName);
    }

    @Step("Click confirm Disable")
    public void clickConfirmDisable() {
        ConfirmationBox.create(driver, wait).clickButtonByLabel(DISABLE_BUTTON_LABEL);
        log.info("Clicking confirm Disable");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Click confirm Delete")
    public void clickConfirmDelete() {
        ConfirmationBox.create(driver, wait).clickButtonByLabel(DELETE_BUTTON_LABEL);
        log.info("Clicking confirm Delete");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Get Email of object with name {objectName}")
    public String getEmailOfObject(String objectName) {
        return getRowOfObjectWithName(objectName).getValue(EMAIL_LABEL);
    }

    @Step("Get Status of object with name {objectName}")
    public String getStatusOfObject(String objectName) {
        return getRowOfObjectWithName(objectName).getValue(STATUS_LABEL);
    }

    @Step("Check if object with name {objectName} is in the Table")
    public boolean isObjectInTable(String objectName) {
        return getMailboxesList().isRowDisplayed(NAME_LABEL, objectName);
    }

    private CommonList.Row getRowOfObjectWithName(String name) {
        return getMailboxesList().getRowContains(NAME_LABEL, name);
    }

    private CommonList getMailboxesList() {
        return CommonList.create(driver, wait, MAILBOXES_COMMON_LIST_ID);
    }
}
