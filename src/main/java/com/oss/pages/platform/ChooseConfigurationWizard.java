package com.oss.pages.platform;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.listwidget.ListGroup;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ChooseConfigurationWizard extends BasePage {

    public ChooseConfigurationWizard(WebDriver driver){ super(driver);}

    private String WIZARD_ID = "";// wait for ID
    private Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    @Step("Choose Configuration")
    public ChooseConfigurationWizard chooseConfiguration(String name){
        ListGroup.create(driver,wait).selectItemByName(name);
        return this;
    }

    public ChooseConfigurationWizard apply(){
        wizard.clickButtonByLabel("Apply");
        return this;
    }

    public ChooseConfigurationWizard download(){
        wizard.clickButtonByLabel("Download");
        return this;
    }

}
