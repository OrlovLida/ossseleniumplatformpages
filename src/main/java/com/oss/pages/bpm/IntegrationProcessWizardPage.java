/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.common.DraggableElement;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.list.DropdownList;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

/**
 * @author Gabriela Kasza
 */
public class IntegrationProcessWizardPage extends BasePage {
    public IntegrationProcessWizardPage(WebDriver driver) {
        super(driver);
    }
    
    private final static String WIZARD_ID = "ipd_integration_wizard_SetupIntegrationComponentId";
    private final static String PROCESS_NAME_COLUMN_ID = "processNameId";
    private final static String PROCESS_NAME_ATTRIBUTE_ID = "processNameId-TEXT_FIELD";
    private final static String FINISH_DUE_DATE_COLUMN_ID = "FINISHED_DUE_DATE";
    private final static String FINISH_DUE_DATE_ATTRIBUTE_ID = "FINISHED_DUE_DATE-DATE";
    private final static String NEXT_BUTTON = "wizard-next-button-ipd_integration_wizard_SetupIntegrationComponentId";
    private final static String ACCEPT_BUTTON = "wizard-submit-button-ipd_integration_wizard_SetupIntegrationComponentId";
    private final static String DELETE_LABEL_ACTION = "Delete";
    
    public void defineIntegrationProcess(String processName, String finishDueDate, int row) {
        EditableList editableList = EditableList.create(driver, wait);
        editableList.addRow();
        editableList.setValue(processName, PROCESS_NAME_COLUMN_ID, row, PROCESS_NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD);
        editableList.setValue(finishDueDate, FINISH_DUE_DATE_COLUMN_ID, row, FINISH_DUE_DATE_ATTRIBUTE_ID, Input.ComponentType.DATE);
    }
    
    public void deleteIntegrationProcess(String processName) {
        EditableList editableList = EditableList.creates(driver, wait);
        editableList.callActionByLabel(DELETE_LABEL_ACTION, PROCESS_NAME_COLUMN_ID, processName);
    }
    
    public void clickNext() {
        Wizard.createByComponentId(driver, wait, WIZARD_ID).clickActionById(NEXT_BUTTON);
    }
    
    public void clickAccept() {
        Wizard integrationWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
        integrationWizard.clickActionById(ACCEPT_BUTTON);
        integrationWizard.waitToClose();
    }
    
    public void dragAndDrop(String objectName, String sourceProcessName, String targetProcessName) {
        DropdownList sourceList = DropdownList.create(driver, wait, sourceProcessName);
        DropdownList targetList = DropdownList.create(driver, wait, targetProcessName);
        
        DraggableElement element = sourceList.getDraggableElement(objectName);
        targetList.drop(element);
        
    }
    
}
