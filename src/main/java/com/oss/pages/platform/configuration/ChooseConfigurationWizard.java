package com.oss.pages.platform.configuration;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.prompts.Popup;
import com.oss.framework.widgets.list.ListGroup;
import com.oss.framework.wizard.Wizard;

public class ChooseConfigurationWizard {
    
    private static final String WIZARD_ID = "configuration_chooser";
    private static final String APPLY_BUTTON_ID = "configuration_chooser_apply_button";
    private static final String CANCEL_BUTTON_ID = "configuration_chooser_cancel_button";
    private static final String DOWNLOAD_BUTTON_ID = "configuration_chooser_apply_button";
    private final WebDriver driver;
    private final WebDriverWait wait;
    
    private ChooseConfigurationWizard(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    public static ChooseConfigurationWizard create(WebDriver driver, WebDriverWait wait) {
        return new ChooseConfigurationWizard(driver, wait);
    }
    
    public ChooseConfigurationWizard chooseConfiguration(String name) {
        ListGroup.create(driver, wait).selectItem(name);
        return this;
    }
    
    public ChooseConfigurationWizard deleteConfiguration(String name) {
        ListGroup.create(driver, wait).callAction(name, "Delete configuration");
        Popup.create(driver, wait).clickButtonByLabel("Delete");
        return this;
    }
    public ChooseConfigurationWizard deleteConfigurations(List<String> names){
        names.forEach(item ->{ListGroup.create(driver, wait).callAction(item, "Delete configuration");
            Popup.create(driver, wait).clickButtonByLabel("Delete");});
        return this;
    }

    public List<String> getConfigurationNames(){
       return ListGroup.create(driver,wait).getItemsName();
    }
    
    public void apply() {
        getWizard().clickButtonById(APPLY_BUTTON_ID);
    }
    
    public void download() {
        getWizard().clickButtonById(DOWNLOAD_BUTTON_ID);
    }
    
    public void cancel() {
        getWizard().clickButtonById(CANCEL_BUTTON_ID);
    }
    
    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
    
}
