/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.list.DraggableList;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

/**
 * @author Gabriela Kasza
 */
public class IntegrationProcessWizardPage extends BasePage {
    private static final String WIZARD_ID = "ipd_integration_wizard_SetupIntegrationComponentId";
    private static final String IP_LIST = "IntegrationProcessWizardComponentId";
    private static final String PROCESS_NAME_COLUMN_ID = "processNameId";
    private static final String PROCESS_NAME_ATTRIBUTE_ID = "processNameId-TEXT_FIELD";
    private static final String FINISH_DUE_DATE_COLUMN_ID = "FINISHED_DUE_DATE";
    private static final String FINISH_DUE_DATE_ATTRIBUTE_ID = "FINISHED_DUE_DATE-DATE";
    private static final String NEXT_BUTTON = "wizard-next-button-ipd_integration_wizard_SetupIntegrationComponentId";
    private static final String ACCEPT_BUTTON = "wizard-submit-button-ipd_integration_wizard_SetupIntegrationComponentId";
    private static final String DELETE_LABEL_ACTION = "Delete";

    public IntegrationProcessWizardPage(WebDriver driver) {
        super(driver);
    }

    public void defineIntegrationProcess(String processName, String finishDueDate, int row) {
        EditableList editableList = EditableList.createById(driver, wait, IP_LIST);
        editableList.addRow();
        editableList.setValueByRowIndex(row, processName, PROCESS_NAME_COLUMN_ID, PROCESS_NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD);
        editableList.setValueByRowIndex(row, finishDueDate, FINISH_DUE_DATE_COLUMN_ID, FINISH_DUE_DATE_ATTRIBUTE_ID, Input.ComponentType.DATE);
    }

    public void deleteIntegrationProcess(String processName) {
        EditableList editableList = EditableList.create(driver, wait);
        editableList.callActionByLabel(DELETE_LABEL_ACTION, PROCESS_NAME_COLUMN_ID, processName);
    }

    public void clickNext() {
        Wizard.createByComponentId(driver, wait, WIZARD_ID).clickButtonById(NEXT_BUTTON);
    }

    public void clickAccept() {
        Wizard integrationWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
        integrationWizard.clickButtonById(ACCEPT_BUTTON);
        integrationWizard.waitToClose();
    }

    public void dragAndDrop(String objectName, String sourceProcessName, String targetProcessName) {
        DraggableList sourceList = DraggableList.create(driver, wait, sourceProcessName);
        DraggableList targetList = DraggableList.create(driver, wait, targetProcessName);
        DragAndDrop.DraggableElement element = sourceList.getDraggableElement(objectName);
        targetList.drop(element);
    }
}
