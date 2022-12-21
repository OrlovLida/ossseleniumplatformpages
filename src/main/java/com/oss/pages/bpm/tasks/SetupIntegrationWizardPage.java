/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm.tasks;

import com.oss.framework.components.list.DraggableList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author Gabriela Kasza
 */
public class SetupIntegrationWizardPage extends BasePage {
    private static final String WIZARD_ID = "ipd_integration_wizard_SetupIntegrationComponentId";
    private static final String IP_LIST = "IntegrationProcessWizardComponentId";
    private static final String PROCESS_NAME_COLUMN_ID = "processNameId";
    private static final String PROCESS_NAME_ATTRIBUTE_ID = "processNameId-TEXT_FIELD";
    private static final String DESCRIPTION_COLUMN_ID = "processDescId";
    private static final String DESCRIPTION_ATTRIBUTE_ID = "processDescId-TEXT_FIELD";
    private static final String FINISH_DUE_DATE_COLUMN_ID = "FINISHED_DUE_DATE";
    private static final String FINISH_DUE_DATE_ATTRIBUTE_ID = "FINISHED_DUE_DATE-DATE";
    private static final String NEXT_BUTTON = "wizard-next-button-ipd_integration_wizard_SetupIntegrationComponentId";
    private static final String ACCEPT_BUTTON = "wizard-submit-button-ipd_integration_wizard_SetupIntegrationComponentId";
    private static final String DELETE_ACTION_ID = "deleteButton1";

    public SetupIntegrationWizardPage(WebDriver driver) {
        super(driver);
    }

    public void defineIntegrationProcess(String processName, LocalDate finishDueDate) {
        EditableList editableList = EditableList.createById(driver, wait, IP_LIST);
        EditableList.Row row = editableList.addRow();
        row.setValue(processName, PROCESS_NAME_COLUMN_ID, PROCESS_NAME_ATTRIBUTE_ID);
        row.setValue(String.valueOf(finishDueDate), FINISH_DUE_DATE_COLUMN_ID, FINISH_DUE_DATE_ATTRIBUTE_ID);
    }

    public void defineIntegrationProcess(String processName, String description, LocalDate finishDueDate) {
        EditableList editableList = EditableList.createById(driver, wait, IP_LIST);
        EditableList.Row row = editableList.addRow();
        row.setValue(processName, PROCESS_NAME_COLUMN_ID, PROCESS_NAME_ATTRIBUTE_ID);
        row.setValue(description, DESCRIPTION_COLUMN_ID, DESCRIPTION_ATTRIBUTE_ID);
        row.setValue(String.valueOf(finishDueDate), FINISH_DUE_DATE_COLUMN_ID, FINISH_DUE_DATE_ATTRIBUTE_ID);
    }

    public void deleteIntegrationProcess(String processName) {
        EditableList editableList = EditableList.createById(driver, wait, IP_LIST);
        editableList.getRowByValue(PROCESS_NAME_COLUMN_ID, processName).callAction(DELETE_ACTION_ID);
    }

    public void clickNext() {
        Wizard.createByComponentId(driver, wait, WIZARD_ID).clickButtonById(NEXT_BUTTON);
    }

    public void clickAccept() {
        Wizard integrationWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
        integrationWizard.clickButtonById(ACCEPT_BUTTON);
        integrationWizard.waitToClose();
    }

    public void dragAndDrop(String objectName, String sourceProcessCodeName, String targetProcessCodeName) {
        DraggableList sourceList = DraggableList.create(driver, wait, sourceProcessCodeName);
        DraggableList targetList = DraggableList.create(driver, wait, targetProcessCodeName);
        DragAndDrop.DraggableElement element = sourceList.getDraggableElement(objectName);
        targetList.drop(element, 0);
    }


    public void setupIntegration(String nrpCode, String nrpName, List<SetupIntegrationProperties> setupIntegrationPropertiesList) {
        String processNRPCodeName = nrpName + " (" + nrpCode + ")";
        for (SetupIntegrationProperties setupIntegrationProperties : setupIntegrationPropertiesList) {
            Optional<String> description = setupIntegrationProperties.getDescription();
            if (description.isPresent()) {
                defineIntegrationProcess(
                        setupIntegrationProperties.getIntegrationProcessName(),
                        description.get(),
                        setupIntegrationProperties.getFinishedDueDate());
            } else {
                defineIntegrationProcess(
                        setupIntegrationProperties.getIntegrationProcessName(),
                        setupIntegrationProperties.getFinishedDueDate());
            }
        }
        clickNext();
        DelayUtils.sleep(1500);
        for (SetupIntegrationProperties setupIntegrationProperty : setupIntegrationPropertiesList) {
            setupIntegrationProperty.getObjectIdentifiers().forEach(objectIdentifier ->
                    dragAndDrop(objectIdentifier, processNRPCodeName,
                            setupIntegrationProperty.getIntegrationProcessName()));
            DraggableList targetList = DraggableList.create(driver, wait, setupIntegrationProperty.getIntegrationProcessName());
            targetList.collapseCategory();
        }
        clickAccept();
    }

}
