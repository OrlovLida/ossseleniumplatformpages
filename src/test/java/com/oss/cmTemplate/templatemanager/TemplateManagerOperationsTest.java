package com.oss.cmTemplate.templatemanager;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.toolsmanager.ToolsManagerPage;
import com.oss.pages.templatecm.templatemanager.CreateEditTemplateFolderPromptPage;
import com.oss.pages.templatecm.templatemanager.TemplateDetailsPage;
import com.oss.pages.templatecm.templatemanager.TemplateTreePage;

import io.qameta.allure.Description;

/**
 * Created by Bartłomiej Jędrzejczyk on 2022-04-15
 */
public class TemplateManagerOperationsTest extends BaseTestCase {

    private static final String CONFIGURATION_MANAGEMENT_CATEGORY_NAME = "Configuration Management";
    private static final String TEMPLATES_MANAGER_APPLICATION_NAME = "Templates Manager";

    private static final String FOLDER_NAME = "cm_selenium_folder_operations";
    private static final String FOLDER_DESCRIPTION = "folder_description";

    private TemplateTreePage templateTreePage;
    private TemplateDetailsPage templateDetailsPage;
    private CreateEditTemplateFolderPromptPage createEditTemplateFolderPromptPage;

    @BeforeClass
    public void getToolsManager() {
        ToolsManagerPage toolsManagerPage = new ToolsManagerPage(driver);
        ToolsManagerWindow toolsManagerWindow = toolsManagerPage.getToolsManager();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        toolsManagerWindow.openApplication(CONFIGURATION_MANAGEMENT_CATEGORY_NAME, TEMPLATES_MANAGER_APPLICATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    @Description("Create template folder")
    public void createTemplateFolder() {
        templateTreePage = new TemplateTreePage(driver);
        templateTreePage.openTemplateFolderCreationPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        createEditTemplateFolderPromptPage = new CreateEditTemplateFolderPromptPage(driver);
        createEditTemplateFolderPromptPage.setFolderName(FOLDER_NAME);
        createEditTemplateFolderPromptPage.setFolderDescription(FOLDER_DESCRIPTION);
        createEditTemplateFolderPromptPage.clickSaveButton();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        templateTreePage.selectTemplateFolder(FOLDER_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        templateDetailsPage = new TemplateDetailsPage(driver);

        Assert.assertEquals(templateDetailsPage.getName(), FOLDER_NAME);
        Assert.assertEquals(templateDetailsPage.getDescription(), FOLDER_DESCRIPTION);
    }
}
