package com.oss.pages.softwaremanagement.repository;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * Created by Bartłomiej Jędrzejczyk on 2022-04-19
 */
public class SoftwareRepositoryPage extends BasePage {

    private static final String SOFTWARE_REPOSITORY_TREE_ID = "smComponent_SoftwareRepositoryViewIdTreeWindowId";
    private static final String CREATE_DIRECTORY_ACTION_ID = "narComponent_SM_SOFTWARE_DIRECTORYActionCreateId,narComponent_SM_SOFTWARE_ROOT_DIRECTORYActionCreateId";
    private static final String RENAME_ACTION_ID = "narComponent_SM_SOFTWARE_DIRECTORYActionRenameId,narComponent_SM_SOFTWARE_FILEActionRenameId";
    private static final String DELETE_ACTION_ID = "narComponent_SM_SOFTWARE_DIRECTORYActionDeleteId,narComponent_SM_SOFTWARE_FILEActionDeleteId";

    private static final String ROOT_PATH = "/";
    private static final String SOFTWARE_MANAGEMENT_REPOSITORY_PATH = "SoftwareManagementRepository";

    private static final Logger LOG = LoggerFactory.getLogger(SoftwareRepositoryPage.class);
    private final TreeWidget softwareRepositoryTree;

    public SoftwareRepositoryPage(WebDriver driver) {
        super(driver);
        softwareRepositoryTree = TreeWidget.createById(driver, wait, SOFTWARE_REPOSITORY_TREE_ID);
    }

    @Step("Expand root folder")
    public void expandRootFolder() {
        if (softwareRepositoryTree.isTreeRowExpanded(ROOT_PATH)) {
            tryExpandSoftwareManagementRepositoryFolder();
        } else {
            softwareRepositoryTree.expandTreeRow(ROOT_PATH);
            DelayUtils.waitForPageToLoad(driver, wait);
            tryExpandSoftwareManagementRepositoryFolder();
        }
    }

    private void tryExpandSoftwareManagementRepositoryFolder() {
        try {
            expandSoftwareManagementRepositoryFolder();
        } catch (NoSuchElementException e) {
            LOG.debug("Software Repository has no {} directory", SOFTWARE_MANAGEMENT_REPOSITORY_PATH);
        }
    }

    private void expandSoftwareManagementRepositoryFolder() {
        if (!softwareRepositoryTree.isTreeRowExpanded(SOFTWARE_MANAGEMENT_REPOSITORY_PATH)) {
            softwareRepositoryTree.expandTreeRow(SOFTWARE_MANAGEMENT_REPOSITORY_PATH);
        }
    }

    @Step("Select root directory")
    public void selectRootDirectory() {
        try {
            softwareRepositoryTree.selectTreeRow(SOFTWARE_MANAGEMENT_REPOSITORY_PATH);
        } catch (NoSuchElementException e) {
            softwareRepositoryTree.selectTreeRow(ROOT_PATH);
        }
    }

    @Step("Select file or directory with name: {name}")
    public void selectFileOrDirectory(String name) {
        softwareRepositoryTree.selectTreeRow(name);
    }

    @Step("Open folder creation popup")
    public void openFolderCreationPopup() {
        softwareRepositoryTree.callActionById(ActionsContainer.CREATE_GROUP_ID, CREATE_DIRECTORY_ACTION_ID);
    }

    @Step("Open rename popup")
    public void openRenamePopup() {
        softwareRepositoryTree.callActionById(ActionsContainer.EDIT_GROUP_ID, RENAME_ACTION_ID);
    }

    @Step("Open deletion popup")
    public void openDeletionPopup() {
        softwareRepositoryTree.callActionById(ActionsContainer.EDIT_GROUP_ID, DELETE_ACTION_ID);
    }
}
