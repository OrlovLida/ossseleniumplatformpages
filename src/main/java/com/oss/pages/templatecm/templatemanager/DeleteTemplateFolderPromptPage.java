package com.oss.pages.templatecm.templatemanager;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * Created by Bartłomiej Jędrzejczyk on 2022-04-15
 */
public class DeleteTemplateFolderPromptPage extends BasePage {

    private static final String DELETE_TEMPLATE_FORM_ID = "card-content_deleteFolderPromptId_prompt-card";
    private static final String DELETE_BUTTON_ID = "TemplateFolderRemovalButtonApp-1";

    private final Wizard deleteTemplateFolderWizard;

    public DeleteTemplateFolderPromptPage(WebDriver driver) {
        super(driver);
        deleteTemplateFolderWizard = Wizard.createByComponentId(driver, wait, DELETE_TEMPLATE_FORM_ID);
    }

    @Step("Click delete button")
    public void clickDeleteButton() {
        deleteTemplateFolderWizard.clickButtonById(DELETE_BUTTON_ID);
    }
}
