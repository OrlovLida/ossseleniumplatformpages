package com.oss.pages.softwaremanagement.repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;

import com.google.common.collect.Iterables;
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

    private static final String ROOT_PATH_TREE_NODE_NAME = "/";

    private final TreeWidget softwareRepositoryTree;
    private final String rootPath;

    public SoftwareRepositoryPage(WebDriver driver, String rootPath) {
        super(driver);
        this.softwareRepositoryTree = TreeWidget.createById(driver, wait, SOFTWARE_REPOSITORY_TREE_ID);
        this.rootPath = rootPath;
    }

    @Step("Expand root folder")
    public void expandRootFolder() {
        if (softwareRepositoryTree.isTreeRowExpanded(ROOT_PATH_TREE_NODE_NAME)) {
            expandMainFolders();
        } else {
            softwareRepositoryTree.expandTreeRow(ROOT_PATH_TREE_NODE_NAME);
            DelayUtils.waitForPageToLoad(driver, wait);
            expandMainFolders();
        }
    }

    private void expandMainFolders() {
        Arrays.stream(rootPath.split("/"))
            .filter(directory -> !directory.isEmpty())
            .forEach(directory -> {
                expandFolder(directory);
                DelayUtils.waitForPageToLoad(driver, wait);
            });
    }

    private void expandFolder(String directory) {
        if (!softwareRepositoryTree.isTreeRowExpanded(directory)) {
            softwareRepositoryTree.expandTreeRow(directory);
        }
    }

    @Step("Select root directory")
    public void selectRootDirectory() {
        Collection<String> rootDirectories = Arrays.stream(rootPath.split("/"))
            .filter(directory -> !directory.isEmpty())
            .collect(Collectors.toList());

        if (rootDirectories.isEmpty()) {
            softwareRepositoryTree.selectTreeRow(ROOT_PATH_TREE_NODE_NAME);
        } else {
            softwareRepositoryTree.selectTreeRow(Iterables.getLast(rootDirectories));
        }
    }

    @Step("Select file or directory with name: {name}")
    public void selectFileOrDirectory(String name) {
        softwareRepositoryTree.selectTreeRow(name);
    }

    @Step("Open folder creation popup")
    public void openFolderCreationPopup() {
        softwareRepositoryTree.callActionById(CREATE_DIRECTORY_ACTION_ID);
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
