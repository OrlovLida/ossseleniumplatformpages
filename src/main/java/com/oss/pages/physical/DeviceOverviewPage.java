package com.oss.pages.physical;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class DeviceOverviewPage extends BasePage {

    public DeviceOverviewPage(WebDriver driver) {
        super(driver);
    }

    private TreeWidget mainTree;

    public TreeWidget getTreeView() {
        if (mainTree == null) {
            Widget.waitForWidget(wait, "TreeView");
            mainTree = TreeWidget.createByClass(driver, "TreeView", wait);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("TreeRow"))));
        }
        return mainTree;
    }

    @Step("Select object on hierarchy view")
    public void selectTreeRow(String name) {
        waitForPageToLoad();
        getTreeView().selectTreeRow(name);
        waitForPageToLoad();
    }

    @Step("Use context action")
    public void useContextAction(String name) {
        waitForPageToLoad();
        ActionsInterface actionsContainer = ButtonContainer.create(driver, wait);
        actionsContainer.callActionByLabel(name);
        waitForPageToLoad();
    }
}