package com.oss.pages.reconciliation;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class SamplesManagementPage extends BasePage {

    private String samplesManagementWidgetId = "narComponent_CMSamplesManagementViewIdFilesTreeId";
    private String upload = "narComponent_CmSampleActionUploadId";
    private String deleteContent = "narComponent_CmSampleActionDeleteContentId";
    private String createDirectory = "narComponent_CmSampleActionCreateId";
    private String uploadWizardId = "narComponent_CMSamplesManagementViewIdUploadSamplesFormItemsId";
    private String createDirectoryWizardId = "narComponent_CMSamplesManagementViewIdFileNameTextFieldId";
    private String createDirectoryWizardConfirmAction = "narComponent_CMSamplesManagementViewIdFileActionButtonsId";

    private TreeWidget mainTree;

    public SamplesManagementPage(WebDriver driver) {
        super(driver);
    }

    public TreeWidget getTreeView() {
        if (mainTree == null) {
            Widget.waitForWidget(wait, "TreeView");
            mainTree = TreeWidget.createByClass(driver, "TreeView", wait);
            DelayUtils.waitForPageToLoad(driver, wait);
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
        TreeWidget widget = TreeWidget.createByDataAttributeName(driver, wait, samplesManagementWidgetId);
        widget.callActionById("OTHER", upload);
        URL res = getClass().getClassLoader().getResource(path);
        try {
            File file = Paths.get(res.toURI()).toFile();
            String absolutePath = file.getAbsolutePath();
            Wizard wizard = Wizard.createByComponentId(driver, wait, uploadWizardId);
            Input input = wizard.getComponent(uploadWizardId, ComponentType.FILE_CHOOSER);
            input.setSingleStringValue(absolutePath);
            wizard.clickOK();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cant load file", e);
        }
    }

    @Step("Delete samples for CM Domain")
    public void deleteDirectoryContent() {
        TreeWidget widget = TreeWidget.createByDataAttributeName(driver, wait, samplesManagementWidgetId);
        widget.callActionById("EDIT", deleteContent);
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard wizard = Wizard.createWizard(driver, wait);
        wizard.clickDelete();
    }

    @Step("Create samples directory for CM Domain")
    public void createDirectory(String cmDomainName) {
        TreeWidget widget = TreeWidget.createByDataAttributeName(driver, wait, samplesManagementWidgetId);
        widget.callActionById("CREATE", createDirectory);
        Wizard wizard = Wizard.createWizard(driver, wait);
        Input name = wizard.getComponent(createDirectoryWizardId, ComponentType.TEXT_FIELD);
        name.setSingleStringValue(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep(500);
        wizard.clickActionById(createDirectoryWizardConfirmAction);
    }
}
