/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import java.util.List;
import java.util.Optional;

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
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

/**
 * @author Gabriela Kasza
 */
public class IntegrationProcessWizardPage extends BasePage {
    public IntegrationProcessWizardPage(WebDriver driver) {
        super(driver);
    }
    private Wizard integrationWizard = Wizard.createWizard(driver, wait);

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
        integrationWizard.clickNext();
    }
    public void dragAndDrop(String objectName, String targetProcessName){

        DelayUtils.waitByXPath(wait,"//div[text()='"+targetProcessName+"']/preceding::div[contains(@class, 'DropdownList')]");
        //DelayUtils.waitByXPath(wait,"//div[contains(@class,'DropdownList')]");
       // WebElement wizardContainer = driver.findElement(By.xpath("//div[contains(@class,'container-fluid')]"));
        //WebElement targetList = driver.findElement(By.xpath(".//div[text()='"+targetProcessName+"']/../.."));
        WebElement targetList = driver.findElement(By.xpath("//div[text()='"+targetProcessName+"']/preceding::div[contains(@class, 'DropdownList')]"));
        WebElement target = targetList.findElement(By.xpath("//div[contains(@class,'droppable-wrapper')]//div//ul"));

        //WebElement target = targetList.findElement(By.xpath("//ul[contains(@class,'DraggableListRows auto')]"));
//        WebElement draggableListRows = wizardContainer.findElement(By.xpath("//ul[contains(@class,'DraggableListRows')]"));
//        List<WebElement> rows = draggableListRows.findElements(By.xpath("*"));
        //WebElement source = first.get().findElement(By.xpath("//*[name()='svg' and @data-icon='grip-vertical']"));
       // Optional<WebElement> first = rows.stream().findFirst();
        //WebElement source = first.get().findElement(By.xpath("//*[name()='svg' and @data-icon='grip-vertical']"));

        WebElement row = driver.findElement(By.xpath("//*[contains(text(),'"+objectName+"')]/preceding::li[contains(@class, 'listElement')]"));
        //WebElement source = row.findElement(By.xpath("//*[name()='svg' and @data-icon='grip-vertical']"));
        WebElement source = row.findElement(By.xpath("//div[contains(@class,'dragButton')]//div"));
        Actions action = new Actions(driver);
        action.click(source);
        action.moveToElement(target,5,5);
        action.perform();
        DelayUtils.sleep(250);
        action.release(target);
        action.perform();
      // action.dragAndDrop(source,target).build().perform();
       //action.clickAndHold(source).moveToElement(target).release(source).build().perform();
        //dragNdrop.perform();
        DelayUtils.sleep(10000);


        //List<WebElement> draggableListRows2 = wizardContainer.findElements(By.xpath("//div[contains(@class,'draggableBox')]"));

    }

}
