package com.oss.pages.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.widgets.tabs.TabWindowWidget;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.issue.wizard.ExternalPromptPage;

import io.qameta.allure.Step;

public class ExternalTab extends BaseSDPage {

    private static final String ADD_EXTERNAL_LABEL = "Add External";
    private static final String EDIT_EXTERNAL_LABEL = "EDIT";
    private static final String DELETE_EXTERNAL_LABEL = "DELETE";
    private static final String EXTERNAL_INFO_LABEL = "External Info";
    private static final String CONFIRM_REMOVE_BUTTON_ID = "ConfirmationBox__formCreateExternal_action_button";
    private static final String ADD_EXTERNAL_PROMPT_WIZARD_ID = "_addExternalPrompt";
    private static final String EDIT_EXTERNAL_PROMPT_WIZARD_ID = "_editExternalPrompt";

    private static final Logger log = LoggerFactory.getLogger(ExternalTab.class);

    public ExternalTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Click Add External")
    public ExternalPromptPage clickAddExternal() {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabWindowWidget.create(driver, wait).callActionByLabel(ADD_EXTERNAL_LABEL);
        log.info("Clicking Add External");

        return new ExternalPromptPage(driver, wait, ADD_EXTERNAL_PROMPT_WIZARD_ID);
    }

    @Step("Click Edit External")
    public ExternalPromptPage clickEditExternal(String externalListId) {
        CommonList.create(driver, wait, externalListId).getRows().get(0).callActionIcon(EDIT_EXTERNAL_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking Edit External");

        return new ExternalPromptPage(driver, wait, EDIT_EXTERNAL_PROMPT_WIZARD_ID);
    }

    @Step("Delete External")
    public void clickDeleteExternal(String externalListId) {
        CommonList.create(driver, wait, externalListId).getRows().get(0).callActionIcon(DELETE_EXTERNAL_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        confirmDelete();
        log.info("Clicking Delete External");
    }

    private void confirmDelete() {
        Button.createById(driver, CONFIRM_REMOVE_BUTTON_ID).click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Checking if expected external {externalName} exists on the list")
    public boolean isExternalCreated(String externalName, String externalListId) {
        DelayUtils.sleep(5000);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Checking if expected external '{}' exists on the list", externalName);
        if (CommonList.create(driver, wait, externalListId).isRowDisplayed(EXTERNAL_INFO_LABEL, externalName)) {
            log.info("Expected external '{}' exists on the list", externalName);
            return true;
        } else {
            log.info("Expected external '{}' does not exists on the list", externalName);
            return false;
        }
    }

    @Step("Check if External list is empty")
    public boolean isExternalListEmpty(String externalListId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return CommonList.create(driver, wait, externalListId).hasNoData();
    }
}

