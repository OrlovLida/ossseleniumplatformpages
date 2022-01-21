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
import com.oss.framework.wizard.Wizard;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class SamplesManagementPage extends BasePage {

    private static final String SAMPLES_MANAGEMENT_WIDGET_ID = "narComponent_CMSamplesManagementViewIdFilesTreeId";
    private static final String UPLOAD_ID = "narComponent_CmSampleActionUploadId";
    private static final String DELETE_CONTENT_ID = "narComponent_CmSampleActionDeleteContentId";
    private static final String CREATE_DIRECTORY = "narComponent_CmSampleActionCreateId";
    private static final String UPLOAD_WIZARD_ID = "narComponent_CMSamplesManagementViewIdUploadSamplesPromptId";
    private static final String CREATE_DIRECTORY_WIZARD_ID = "narComponent_CMSamplesManagementViewIdFileNameTextFieldId";
    private static final String CREATE_DIRECTORY_WIZARD_CONFIRM_ACTION = "narComponent_CMSamplesManagementViewIdFileActionButtonsId-1";

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
    public void uploadSamples(String path) throws URISyntaxException {
        TreeWidget widget = TreeWidget.createByDataAttributeName(driver, wait, SAMPLES_MANAGEMENT_WIDGET_ID);
        widget.callOssWindowActionById("OTHER", UPLOAD_ID);
        URL res = getClass().getClassLoader().getResource(path);
        try {
            assert res != null;
            File file = Paths.get(res.toURI()).toFile();
            String absolutePath = file.getAbsolutePath();
            Wizard wizard = Wizard.createByComponentId(driver, wait, UPLOAD_WIZARD_ID);
            Input input = wizard.getComponent(UPLOAD_WIZARD_ID, ComponentType.FILE_CHOOSER);
            input.setSingleStringValue(absolutePath);
            wizard.clickOK();
        } catch (URISyntaxException e) {
            throw new URISyntaxException("Cant load file", e.getReason());
        }
    }

    @Step("Delete samples for CM Domain")
    public void deleteDirectoryContent() {
        TreeWidget widget = TreeWidget.createByDataAttributeName(driver, wait, SAMPLES_MANAGEMENT_WIDGET_ID);
        widget.callOssWindowActionById("EDIT", DELETE_CONTENT_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard wizard = Wizard.createWizard(driver, wait);
        wizard.clickDelete();
    }

    @Step("Create samples directory for CM Domain")
    public void createDirectory(String cmDomainName) {
        TreeWidget widget = TreeWidget.createByDataAttributeName(driver, wait, SAMPLES_MANAGEMENT_WIDGET_ID);
        widget.callOssWindowActionById("CREATE", CREATE_DIRECTORY);
        Wizard wizard = Wizard.createWizard(driver, wait);
        Input name = wizard.getComponent(CREATE_DIRECTORY_WIZARD_ID, ComponentType.TEXT_FIELD);
        name.setSingleStringValue(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep(500);
        wizard.clickButtonById(CREATE_DIRECTORY_WIZARD_CONFIRM_ACTION);
    }
}
