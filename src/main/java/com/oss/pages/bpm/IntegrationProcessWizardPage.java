/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

/**
 * @author Gabriela Kasza
 */
public class IntegrationProcessWizardPage extends BasePage {
    public IntegrationProcessWizardPage(WebDriver driver) {
        super(driver);
    }

    private String WIZARD_ID ="ipd_integration_wizard_SetupIntegrationComponentId";
    private String PROCESS_NAME_COLUMN_ID = "processNameId";
    private String PROCESS_NAME_ATTRIBUTE_ID = "processNameId-TEXT_FIELD";
    private String FINISH_DUE_DATE_COLUMN_ID = "FINISHED_DUE_DATE";
    private String FINISH_DUE_DATE_ATTRIBUTE_ID = "FINISHED_DUE_DATE-DATE";
    private String NEXT_BUTTON = "wizard-next-button-ipd_integration_wizard_SetupIntegrationComponentId";
    private String ACCEPT_BUTTON ="wizard-submit-button-ipd_integration_wizard_SetupIntegrationComponentId";
    private String DROPDOWN_LIST_XPATH = ".//div[contains(@class,'DropdownList')]";
    private String DROPDOWN_LIST_LABEL_XPATH = ".//div[@class='categoryLabel']";
    private String DRAGGABLE_LIST_ROW_XPATH ="//ul[contains(@class,'DraggableListRows auto')]";
    private String DRAG_BUTTON_XPATH = "//div[contains(@class,'dragButton')]//div";
    private String DELETE_LABEL_ACTION = "Delete";

    private Wizard integrationWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    public void defineIntegrationProcess(String processName, String finishDueDate, int row){
        EditableList editableList = EditableList.create(driver, wait);
        editableList.addRow();
        editableList.setValue(processName,PROCESS_NAME_COLUMN_ID,row,PROCESS_NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD);
        editableList.setValue(finishDueDate,FINISH_DUE_DATE_COLUMN_ID,row,FINISH_DUE_DATE_ATTRIBUTE_ID,Input.ComponentType.DATE);
    }
    public void deleteIntegrationProcess(String processName ){
        EditableList editableList = EditableList.create(driver, wait);
        editableList.callActionByLabel(DELETE_LABEL_ACTION,PROCESS_NAME_COLUMN_ID,processName);
    }
    public void clickNext(){
        integrationWizard.clickActionById(NEXT_BUTTON);
    }
    public void clickAccept(){
        integrationWizard.clickActionById(ACCEPT_BUTTON);
        integrationWizard.waitToClose();
    }
    public void dragAndDrop(String objectName, String targetProcessName){

        DelayUtils.waitByXPath(wait,DROPDOWN_LIST_XPATH);
        DelayUtils.waitByXPath(wait, ".//div[text()='"+targetProcessName+"']");
        List<WebElement> targetLists = driver.findElements(By.xpath(DROPDOWN_LIST_XPATH));
        WebElement dropdownList = targetLists.stream()
                .filter(target -> target.findElement(By.xpath(DROPDOWN_LIST_LABEL_XPATH)).getText().equals(targetProcessName))
                .findFirst().get();
        WebElement target = dropdownList.findElement(By.xpath(DRAGGABLE_LIST_ROW_XPATH));
        WebElement row = driver.findElement(By.xpath("//*[contains(text(),'"+objectName+"')]/preceding::li[contains(@class, 'listElement')]"));
        WebElement source = row.findElement(By.xpath(DRAG_BUTTON_XPATH));
        DragAndDrop.dragAndDrop(source,target,driver);
    }

}
