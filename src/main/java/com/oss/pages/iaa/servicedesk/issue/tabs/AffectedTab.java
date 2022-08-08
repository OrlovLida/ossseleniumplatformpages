package com.oss.pages.iaa.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.iaa.servicedesk.BaseSDPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

public class AffectedTab extends BaseSDPage {

    private static final String AFFECTED_TABLE_ID = "_affectedServicesApp";
    private static final String ADD_SERVICE_BUTTON_ID = "_addResource";
    private static final String OLD_ACTIONS_CONTAINER_ID = "_tablesWindow-windowToolbar";
    private static final String ADD_SERVICES_PROMPT_ID = "_assigneeServiceModal_prompt-card";
    private static final String MO_IDENTIFIER_LABEL = "MO Identifier";
    private static final String SERVICE_MULTI_SEARCH_ID = "TT_WIZARD_INPUT_MANAGED_OBJECT_LABEL";
    private static final String ADD_SERVICE_IN_PROMPT_ID = "SaveButtonId";

    public AffectedTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Click Add Service Button")
    public SDWizardPage clickAddServices() {
        OldActionsContainer.createById(driver, wait, OLD_ACTIONS_CONTAINER_ID).callActionById(ADD_SERVICE_BUTTON_ID);

        return new SDWizardPage(driver, wait, ADD_SERVICES_PROMPT_ID);
    }

    public int countServicesInTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getAffectedTable().countRows(MO_IDENTIFIER_LABEL);
    }

    @Step("Click Add Service and fill prompt")
    public AffectedTab addServiceToTable(String serviceMOIdentifier) {
        clickAddServices()
                .insertValueContainsToComponent(serviceMOIdentifier, SERVICE_MULTI_SEARCH_ID)
                .clickButton(ADD_SERVICE_IN_PROMPT_ID);
        return this;
    }

    private OldTable getAffectedTable() {
        return OldTable.createById(driver, wait, AFFECTED_TABLE_ID);
    }
}
