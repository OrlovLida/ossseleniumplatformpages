package com.oss.pages.servicedesk.infomanagement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.INFO_MANAGEMENT_URL_PATTERN;

public class TemplatesPage extends BaseSDPage {

    private static final String TEMPLATES_URL = "template-list";
    private static final String TEMPLATES_COMMON_LIST_ID = "template-list-app";
    private static final String CREATE_HEADER_ID = "create-header-id";
    private static final String CREATE_FOOTER_ID = "create-footer-id";
    private static final String CREATE_TEMPLATE_ID = "create-template-id";
    private static final String WIZARD_ID = "template-wizard-wizard";
    private static final String NAME_LABEL = "Name";
    private static final String TYPE_LABEL = "Type";
    private static final String CREATOR_LABEL = "Creator";
    private static final String EDIT_BUTTON_ICON_LABEL = "EDIT";
    private static final String DELETE_BUTTON_ICON_LABEL = "DELETE";
    private static final String DELETE_BUTTON_LABEL = "Delete";

    public TemplatesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public TemplatesPage goToTemplatesPage(String basicUrl) {
        String url = String.format(INFO_MANAGEMENT_URL_PATTERN, basicUrl, TEMPLATES_URL);
        openPage(driver, url);
        return new TemplatesPage(driver, wait);
    }

    @Step("Click Create Header")
    public SDWizardPage clickCreateHeader() {
        clickCreateObject(CREATE_HEADER_ID);
        log.info("Clicking Create Header");
        return new SDWizardPage(driver, wait, WIZARD_ID);
    }

    @Step("Click Create Footer")
    public SDWizardPage clickCreateFooter() {
        clickCreateObject(CREATE_FOOTER_ID);
        log.info("Clicking Create Footer button");
        return new SDWizardPage(driver, wait, WIZARD_ID);
    }

    @Step("Click Create Template")
    public SDWizardPage clickCreateTemplate() {
        clickCreateObject(CREATE_TEMPLATE_ID);
        log.info("Clicking Create Template button");
        return new SDWizardPage(driver, wait, WIZARD_ID);
    }

    private void clickCreateObject(String actionId) {
        getTemplatesList().callAction(actionId);
    }

    @Step("Check if Templates table is empty")
    public boolean isTemplatesTableEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getTemplatesList().hasNoData();
    }

    @Step("Click Edit Object with name: {objectName}")
    public SDWizardPage clickEditOnObjectWithName(String objectName) {
        getTemplatesList().getRow(NAME_LABEL, objectName).callActionIcon(EDIT_BUTTON_ICON_LABEL);
        return new SDWizardPage(driver, wait, WIZARD_ID);
    }

    @Step("Click Delete object with name {objectName}")
    public void clickDeleteOnObjectWithName(String objectName) {
        getTemplatesList().getRow(NAME_LABEL, objectName).callActionIcon(DELETE_BUTTON_ICON_LABEL);
        log.info("Clicking Delete object with name {}", objectName);
    }

    @Step("Click confirm Delete")
    public void clickConfirmDelete() {
        ConfirmationBox.create(driver, wait).clickButtonByLabel(DELETE_BUTTON_LABEL);
        log.info("Clicking confirm Delete");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Get Type of object with name {objectName}")
    public String getTypeOfObject(String objectName) {
        return getRowOfObjectWithName(objectName).getValue(TYPE_LABEL);
    }

    @Step("Get Creator user name of object with name {objectName}")
    public String getCreatorOfObject(String objectName) {
        return getRowOfObjectWithName(objectName).getValue(CREATOR_LABEL);
    }

    @Step("Check if object with name {objectName} is in the Table")
    public boolean isObjectInTable(String objectName) {
        return getTemplatesList().isRowDisplayed(NAME_LABEL, objectName);
    }

    private CommonList.Row getRowOfObjectWithName(String name) {
        return getTemplatesList().getRowContains(NAME_LABEL, name);
    }

    private CommonList getTemplatesList() {
        return CommonList.create(driver, wait, TEMPLATES_COMMON_LIST_ID);
    }
}
