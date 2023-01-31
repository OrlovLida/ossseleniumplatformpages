package com.oss.pages.transport.trail;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class RoutingOverElementsWizardPage extends BasePage {
    private static final String COMPONENT_ID = "routingModifyPrompt_prompt-card";
    private static final String SEQUENCE_NUMBER_FIELD_ID = "columnSequenceNumberId-TEXT_FIELD";
    private static final String SEQUENCE_NUMBER_COLUMN_ID = "columnSequenceNumberId";
    private static final String RELATION_TYPE_FIELD_ID = "columnRelationTypeId";
    private static final String RELATION_TYPE_COLUMN_ID = "columnRelationTypeId";
    private static final String EDITABLE_LIST_ID = "ExtendedList-elementsListComponent";

    private final Wizard wizard;

    public RoutingOverElementsWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, COMPONENT_ID);
    }

    @Step("Set Sequence Number = {value}")
    public void setSequenceNumber(int rowNumber, String value) {
        waitForPageToLoad();
        EditableList editableList = EditableList.createById(driver, wait, EDITABLE_LIST_ID);
        waitForPageToLoad();
        editableList.setValue(rowNumber, value, SEQUENCE_NUMBER_COLUMN_ID, SEQUENCE_NUMBER_FIELD_ID);
        waitForPageToLoad();
    }

    @Step("Set Relation Type = {value}")
    public void setRelationType(int rowNumber, String value) {
        waitForPageToLoad();
        EditableList editableList = EditableList.createById(driver, wait, EDITABLE_LIST_ID);
        waitForPageToLoad();
        editableList.setValue(rowNumber, value, RELATION_TYPE_COLUMN_ID, RELATION_TYPE_FIELD_ID);
        waitForPageToLoad();
    }

    public void clickAccept() {
        wizard.clickAccept();
        waitForPageToLoad();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}
