package com.oss.pages.templatecm.templatemanager;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * Created by Bartłomiej Jędrzejczyk on 2022-04-15
 */
public class TemplateTreePage extends BasePage {

    private static final String TEMPLATES_TREE_ID = "card-content_TemplateBrowserWindow";
    private static final String TEMPLATE_APPLICATION_CREATE_FOLDER_ACTION_ID = "TemplateApplicationCreateFolderActionId";
    private static final String TEMPLATE_APPLICATION_EDIT_FOLDER_ACTION_ID = "TemplateApplicationEditFolderActionId";

    private final TreeWidget templatesTree;

    public TemplateTreePage(WebDriver driver) {
        super(driver);
        templatesTree = TreeWidget.createById(driver, wait, TEMPLATES_TREE_ID);
    }

    @Step("Open template folder creation popup")
    public void openTemplateFolderCreationPopup() {
        templatesTree.callActionById(ActionsContainer.CREATE_GROUP_ID, TEMPLATE_APPLICATION_CREATE_FOLDER_ACTION_ID);
    }

    @Step("Open template folder modification popup")
    public void openTemplateFolderModificationPopup() {
        templatesTree.callActionById(ActionsContainer.EDIT_GROUP_ID, TEMPLATE_APPLICATION_EDIT_FOLDER_ACTION_ID);
    }

    @Step("Open template folder creation popup")
    public void selectTemplateFolder(String folderName) {
        templatesTree.selectTreeRow(folderName);
    }
}
