package com.oss.pages.reconciliation;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

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

    @Step("Select samples path")
    public void selectPath() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeView().selectTreeRowByOrder(0);
    }

    @Step("Upload samples for CM Domain")
    public void uploadSamples(String path) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(driver.findElement(By.xpath("//div[@class='OssWindow']//div[@class='context-actions-wrapper']")), driver, wait);
        actionsContainer.callActionById("OTHER", "narComponent_CmSampleActionUploadId");
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

    @Step("Delete samples for CM Domain")
    public void deleteDirectoryContent() {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(driver.findElement(By.xpath("//div[@class='OssWindow']//div[@class='context-actions-wrapper']")), driver, wait);
        actionsContainer.callActionById("EDIT", "narComponent_CmSampleActionDeleteContentId");
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard wizard = Wizard.createWizard(driver, wait);
        wizard.clickDelete();
    }

    @Step("Create samples directory for CM Domain")
    public void createDirectory(String cmDomainName) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(driver.findElement(By.xpath("//div[@class='OssWindow']//div[@class='context-actions-wrapper']")), driver, wait);
        actionsContainer.callActionById("CREATE", "narComponent_CmSampleActionCreateId");
        Wizard wizard = Wizard.createWizard(driver, wait);
        Input name = wizard.getComponent("narComponent_CMSamplesManagementViewIdFileNameTextFieldId", ComponentType.TEXT_FIELD);
        name.setSingleStringValue(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep(500);
        wizard.clickActionById("narComponent_CMSamplesManagementViewIdFileActionButtonsId-1");
    }
}
