package com.oss.softwaremanagement.repository;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.toolsmanager.ToolsManagerPage;
import com.oss.pages.softwaremanagement.repository.FileActionPromptPage;
import com.oss.pages.softwaremanagement.repository.PropertiesPage;
import com.oss.pages.softwaremanagement.repository.SoftwareRepositoryPage;
import com.oss.softwaremanagement.infrastructure.repository.SoftwareRepositoryClient;

import io.qameta.allure.Description;

/**
 * Created by Bartłomiej Jędrzejczyk on 2022-04-19
 */
public class SoftwareRepositoryOperationsTest extends BaseTestCase {

    private static final String CONFIGURATION_MANAGEMENT_CATEGORY_NAME = "Configuration Management";
    private static final String REPOSITORY_MANAGER_ACTION_NAME = "Repository Manager";

    private static final String SELENIUM_TEST_FOLDER_UNDER_ROOT_NAME = "selenium_test_folder_under_root";
    private static final String SELENIUM_TEST_FOLDER_UNDER_ROOT_EDITED_NAME = SELENIUM_TEST_FOLDER_UNDER_ROOT_NAME + "_edited";

    private SoftwareRepositoryClient softwareRepositoryClient;
    private String rootPath;

    @BeforeClass
    public void init() {
        softwareRepositoryClient = SoftwareRepositoryClient.getInstance(environmentRequestClient);
        rootPath = softwareRepositoryClient.getRootPath();
        moveToSoftwareRepository();
    }

    private void moveToSoftwareRepository() {
        ToolsManagerPage toolsManagerPage = new ToolsManagerPage(driver);
        ToolsManagerWindow toolsManagerWindow = toolsManagerPage.getToolsManager();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        toolsManagerWindow.openApplication(CONFIGURATION_MANAGEMENT_CATEGORY_NAME, REPOSITORY_MANAGER_ACTION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    @Description("Create folder under root directory")
    public void createFolderUnderRootDirectory() {
        SoftwareRepositoryPage softwareRepositoryPage = new SoftwareRepositoryPage(driver, rootPath);
        softwareRepositoryPage.expandRootFolder();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        softwareRepositoryPage.selectRootDirectory();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        softwareRepositoryPage.openFolderCreationPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        FileActionPromptPage fileActionPromptPage = new FileActionPromptPage(driver);
        fileActionPromptPage.setName(SELENIUM_TEST_FOLDER_UNDER_ROOT_NAME);
        fileActionPromptPage.clickSubmitButton();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        softwareRepositoryPage.selectFileOrDirectory(SELENIUM_TEST_FOLDER_UNDER_ROOT_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PropertiesPage propertiesPage = new PropertiesPage(driver);

        Assert.assertEquals(propertiesPage.getFileName(), SELENIUM_TEST_FOLDER_UNDER_ROOT_NAME);
        Assert.assertEquals(propertiesPage.getPath(), rootPath + "/" + SELENIUM_TEST_FOLDER_UNDER_ROOT_NAME);
    }

    @Test(priority = 2, expectedExceptions = NoSuchElementException.class)
    @Description("Rename folder")
    public void renameFolder() {
        SoftwareRepositoryPage softwareRepositoryPage = new SoftwareRepositoryPage(driver, rootPath);
        softwareRepositoryPage.openRenamePopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        FileActionPromptPage fileActionPromptPage = new FileActionPromptPage(driver);
        fileActionPromptPage.setName(SELENIUM_TEST_FOLDER_UNDER_ROOT_EDITED_NAME);
        fileActionPromptPage.clickSubmitButton();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        softwareRepositoryPage.selectFileOrDirectory(SELENIUM_TEST_FOLDER_UNDER_ROOT_EDITED_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PropertiesPage propertiesPage = new PropertiesPage(driver);

        Assert.assertEquals(propertiesPage.getFileName(), SELENIUM_TEST_FOLDER_UNDER_ROOT_EDITED_NAME);
        Assert.assertEquals(propertiesPage.getPath(), rootPath + "/" + SELENIUM_TEST_FOLDER_UNDER_ROOT_EDITED_NAME);

        softwareRepositoryPage.selectFileOrDirectory(SELENIUM_TEST_FOLDER_UNDER_ROOT_NAME);
    }

    @Test(priority = 3, expectedExceptions = NoSuchElementException.class)
    @Description("Delete folder")
    public void deleteFolder() {
        SoftwareRepositoryPage softwareRepositoryPage = new SoftwareRepositoryPage(driver, rootPath);
        softwareRepositoryPage.openDeletionPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        FileActionPromptPage fileActionPromptPage = new FileActionPromptPage(driver);
        fileActionPromptPage.clickSubmitButton();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        softwareRepositoryPage.selectFileOrDirectory(SELENIUM_TEST_FOLDER_UNDER_ROOT_EDITED_NAME);
    }
}
