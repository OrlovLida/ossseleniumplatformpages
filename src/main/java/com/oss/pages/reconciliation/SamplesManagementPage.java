package com.oss.pages.reconciliation;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.oss.framework.components.Input;
import com.oss.framework.components.Input.ComponentType;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;

public class SamplesManagementPage extends BasePage {

    private TreeWidget mainTree;

    public static SamplesManagementPage goToNetworkInconsistenciesViewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/reco/network-repository-view/cm-samples-management" +
                "?perspective=NETWORK", basicURL));
        return new SamplesManagementPage(driver);
    }

    public SamplesManagementPage(WebDriver driver) {
        super(driver);
    }

    public TreeWidget getTreeView() {
        if (mainTree == null) {
            Widget.waitForWidget(wait, "TreeView");
            mainTree = TreeWidget.createByClass(driver, "TreeView", wait);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("TreeRow"))));
        }
        return mainTree;
    }

    public void selectPath() {
        DelayUtils.sleep(1000);
        getTreeView().selectTreeRowByOrder(0);
    }

    public void uploadSamples(String path) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(driver.findElement(By.xpath("//div[@class='OssWindow']//div[@class='context-actions-wrapper']")), driver, wait);
        actionsContainer.callActionByLabel("OTHER", "Upload samples");
        URL res = getClass().getClassLoader().getResource(path);
        try {
            File file = Paths.get(res.toURI()).toFile();
            String absolutePath = file.getAbsolutePath();
            Wizard wizard = Wizard.createByComponentId(driver, wait, "narComponent_CMSamplesManagementViewIdUploadSamplesFormItemsId");
            Input input = wizard.getComponent("narComponent_CMSamplesManagementViewIdUploadSamplesFormItemsId", ComponentType.FILE_CHOOSER);
            input.setSingleStringValue(absolutePath);
            wizard.clickOK();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cant load file", e);
        }
    }

    public void deleteDirectoryContent() {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(driver.findElement(By.xpath("//div[@class='OssWindow']//div[@class='context-actions-wrapper']")), driver, wait);
        actionsContainer.callActionByLabel("EDIT", "Delete directory content");
        Wizard wizard = Wizard.createWizard(driver, wait);
        wizard.clickDelete();
    }

    public void createDirectory(String cmDomainName) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(driver.findElement(By.xpath("//div[@class='OssWindow']//div[@class='context-actions-wrapper']")), driver, wait);
        actionsContainer.callActionByLabel("CREATE", "Create directory");
        Wizard wizard = Wizard.createWizard(driver, wait);
        Input name = wizard.getComponent("narComponent_CMSamplesManagementViewIdFileNameTextFieldId", ComponentType.TEXT_FIELD);
        name.setSingleStringValue(cmDomainName);
        DelayUtils.sleep(500);
        wizard.clickCreate();
    }
}
