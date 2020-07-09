/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.Input;
import com.oss.framework.data.Data;
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

    public void createIntegrationProcess(String processName, String finishDueDate){
        Wizard integrationWizard = Wizard.createWizard(driver, wait);
        EditableList editableList = EditableList.create(driver, wait);
        editableList.addRow();
        editableList.setValue(processName,"processNameId",1,"processNameId-TEXT_FIELD", Input.ComponentType.TEXT_FIELD);
        editableList.setValue(finishDueDate,"FINISHED_DUE_DATE",1,"FINISHED_DUE_DATE-DATE",Input.ComponentType.DATE);
    }
    public void deleteIntegrationProcess(String processName ){
        Wizard integrationWizard = Wizard.createWizard(driver, wait);
        EditableList editableList = EditableList.create(driver, wait);
        editableList.callActionByLabel("Delete","processNameId",processName);
    }
}
