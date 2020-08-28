/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.dms;

import java.util.List;

import org.openqa.selenium.WebDriver;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

/**
 * @author Gabriela Kasza
 */
public class AttachFileWizardPage extends BasePage {

    private String WIZARD_ID ="addFileComponentId";
    private String FILE_COMPONENT_ID = "file";
    private String NEXT_BUTTON_ID = "wizard-next-button-addFileComponentId";
    private String ACCEPT_BUTTON_ID = "wizard-submit-button-addFileComponentId";
    private String RADIO_BUTTONS_ID = "duplicateFilesRadioButtons";

   private Wizard addFileWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID );

    public AttachFileWizardPage(WebDriver driver) {
        super(driver);
    }
    public void attachFile(String filePath){
        Input input = addFileWizard.getComponent(FILE_COMPONENT_ID, Input.ComponentType.FILE_CHOOSER);
        input.setSingleStringValue(filePath);
    }
    public void nextButton(){
        Wizard.createByComponentId(driver, wait, WIZARD_ID).clickActionById(NEXT_BUTTON_ID);
    }
    public void acceptButton(){
        addFileWizard.clickActionById(ACCEPT_BUTTON_ID);

    }
    public void deleteFiles(){
        Input input = addFileWizard.getComponent(FILE_COMPONENT_ID, Input.ComponentType.FILE_CHOOSER);
        input.clear();

    }
    public List<String> getAttachmentName(){
        Input input = addFileWizard.getComponent(FILE_COMPONENT_ID, Input.ComponentType.FILE_CHOOSER);
        return input.getStringValues();
    }
    public void selectRadioButton(String label){
        Input radio = Wizard.createByComponentId(driver, wait, WIZARD_ID)
                .getComponent(RADIO_BUTTONS_ID, Input.ComponentType.RADIO_BUTTON);
        radio.setSingleStringValue(label);
        DelayUtils.sleep();

    }



}
