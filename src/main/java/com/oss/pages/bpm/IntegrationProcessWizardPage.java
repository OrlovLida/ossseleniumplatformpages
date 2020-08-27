/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import com.oss.framework.components.Input;
import com.oss.framework.data.Data;
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
    private Wizard integrationWizard = Wizard.createByComponentId(driver, wait, "ipd_integration_wizard_SetupIntegrationComponentId");

    public void defineIntegrationProcess(String processName, String finishDueDate, int row){
        //Wizard integrationWizard = Wizard.createWizard(driver, wait);
        EditableList editableList = EditableList.create(driver, wait);
        editableList.addRow();
        editableList.setValue(processName,"processNameId",row,"processNameId-TEXT_FIELD", Input.ComponentType.TEXT_FIELD);
        editableList.setValue(finishDueDate,"FINISHED_DUE_DATE",row,"FINISHED_DUE_DATE-DATE",Input.ComponentType.DATE);
    }
    public void deleteIntegrationProcess(String processName ){
        //Wizard integrationWizard = Wizard.createWizard(driver, wait);
        EditableList editableList = EditableList.create(driver, wait);
        editableList.callActionByLabel("Delete","processNameId",processName);
    }
    public void clickNext(){
        integrationWizard.clickActionById("wizard-next-button-ipd_integration_wizard_SetupIntegrationComponentId");
    }
    public void clickAccept(){
        integrationWizard.clickActionById("wizard-submit-button-ipd_integration_wizard_SetupIntegrationComponentId");
        integrationWizard.waitToClose();


    }
    public void dragAndDrop(String objectName, String targetProcessName){

        DelayUtils.waitByXPath(wait,".//div[contains(@class,'DropdownList')]");
        DelayUtils.waitByXPath(wait, ".//div[text()='"+targetProcessName+"']");

        List<WebElement> targetLists = driver.findElements(By.xpath(".//div[@class='DropdownList']"));
        WebElement dropdownList = targetLists.stream()
                .filter(target -> target.findElement(By.xpath(".//div[@class='categoryLabel']")).getText().equals(targetProcessName))
                .findFirst().get();

        WebElement target = dropdownList.findElement(By.xpath("//ul[contains(@class,'DraggableListRows auto')]"));

        WebElement row = driver.findElement(By.xpath("//*[contains(text(),'"+objectName+"')]/preceding::li[contains(@class, 'listElement')]"));
        WebElement source = row.findElement(By.xpath("//div[contains(@class,'dragButton')]//div"));
        DragAndDrop.dragAndDrop(source,target,driver);

    }

}
