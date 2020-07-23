/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.dms;

import org.openqa.selenium.WebDriver;
import com.oss.framework.components.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

/**
 * @author Gabriela Kasza
 */
public class AttachFileWizardPage extends BasePage {
   private Wizard addFileWizard = Wizard.createByComponentId(driver, wait, "addFileComponentId");

    public AttachFileWizardPage(WebDriver driver) {
        super(driver);
    }
    public void attachFile(String filePath){
        Input input = addFileWizard.getComponent("file", Input.ComponentType.FILE_CHOOSER);
        input.setSingleStringValue(filePath);
    }
    public void nextButton(){
        addFileWizard.clickNext();
    }
    public void acceptButton(){
        addFileWizard.clickAccept();
    }
    public void deleteFile(){
        Input input = addFileWizard.getComponent("file", Input.ComponentType.FILE_CHOOSER);
        input.clear();

    }
    public String getAttachmentName(){
        Input input = addFileWizard.getComponent("file", Input.ComponentType.FILE_CHOOSER);
        return input.getStringValue();


    }



}
