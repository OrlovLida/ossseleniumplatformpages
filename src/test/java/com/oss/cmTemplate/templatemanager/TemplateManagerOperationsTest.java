package com.oss.cmTemplate.templatemanager;

import java.util.UUID;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.toolsmanager.ToolsManagerPage;
import com.oss.pages.templatecm.templatemanager.CreateEditTemplateFolderPromptPage;
import com.oss.pages.templatecm.templatemanager.DeleteTemplateFolderPromptPage;
import com.oss.pages.templatecm.templatemanager.TemplateDetailsPage;
import com.oss.pages.templatecm.templatemanager.TemplateTreePage;
import com.oss.template.infrastructure.template.folder.TemplateFolderClient;

import io.qameta.allure.Description;

/**
 * Created by Bartłomiej Jędrzejczyk on 2022-04-15
 */
public class TemplateManagerOperationsTest extends BaseTestCase {

    private static final String CONFIGURATION_MANAGEMENT_CATEGORY_NAME = "Configuration Management";
    private static final String TEMPLATES_MANAGER_APPLICATION_NAME = "Templates Manager";

    private static final String CREATED_FOLDER_NAME = "cm_selenium_folder_operations";
    private static final String EDITED_FOLDER_DESCRIPTION = UUID.randomUUID().toString();

    @BeforeClass
    public void init() {
        moveToTemplateManager();
    }

    private void moveToTemplateManager() {
        ToolsManagerPage toolsManagerPage = new ToolsManagerPage(driver);
        ToolsManagerWindow toolsManagerWindow = toolsManagerPage.getToolsManager();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        toolsManagerWindow.openApplication(CONFIGURATION_MANAGEMENT_CATEGORY_NAME, TEMPLATES_MANAGER_APPLICATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    @Description("Create template folder")
    public void createTemplateFolder() {
        TemplateTreePage templateTreePage = new TemplateTreePage(driver);
        templateTreePage.openTemplateFolderCreationPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        CreateEditTemplateFolderPromptPage createEditTemplateFolderPromptPage = new CreateEditTemplateFolderPromptPage(driver);
        createEditTemplateFolderPromptPage.setFolderName(CREATED_FOLDER_NAME);
        createEditTemplateFolderPromptPage.clickSaveButton();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        templateTreePage.selectTemplateFolder(CREATED_FOLDER_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TemplateDetailsPage templateDetailsPage = new TemplateDetailsPage(driver);

        Assert.assertEquals(templateDetailsPage.getName(), CREATED_FOLDER_NAME);
    }

    @Test(priority = 2)
    @Description("Edit template folder")
    public void editTemplateFolder() {
        TemplateTreePage templateTreePage = new TemplateTreePage(driver);
        templateTreePage.openTemplateFolderModificationPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        CreateEditTemplateFolderPromptPage createEditTemplateFolderPromptPage = new CreateEditTemplateFolderPromptPage(driver);
        createEditTemplateFolderPromptPage.setFolderDescription(EDITED_FOLDER_DESCRIPTION);
        createEditTemplateFolderPromptPage.clickSaveButton();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        templateTreePage.selectTemplateFolder(CREATED_FOLDER_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TemplateDetailsPage templateDetailsPage = new TemplateDetailsPage(driver);

        Assert.assertEquals(templateDetailsPage.getName(), CREATED_FOLDER_NAME);
        Assert.assertEquals(templateDetailsPage.getDescription(), EDITED_FOLDER_DESCRIPTION);
    }

    @Test(priority = 3, expectedExceptions = NoSuchElementException.class)
    @Description("Delete template folder")
    public void deleteTemplateFolder() {
        TemplateTreePage templateTreePage = new TemplateTreePage(driver);
        templateTreePage.openTemplateFolderDeletionPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        DeleteTemplateFolderPromptPage deleteTemplateFolderPromptPage = new DeleteTemplateFolderPromptPage(driver);
        deleteTemplateFolderPromptPage.clickDeleteButton();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        templateTreePage.selectTemplateFolder(CREATED_FOLDER_NAME);
    }

    @AfterClass(alwaysRun = true)
    public void clear() {
        TemplateFolderClient templateFolderClient = TemplateFolderClient.getInstance(environmentRequestClient);
        templateFolderClient.deleteFolderPermanently(CREATED_FOLDER_NAME);
    }
}
