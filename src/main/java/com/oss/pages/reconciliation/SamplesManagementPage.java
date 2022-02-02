package com.oss.pages.reconciliation;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class SamplesManagementPage extends BasePage {

    private static final String SAMPLES_MANAGEMENT_WIDGET_ID = "narComponent_CMSamplesManagementViewIdTreeWindowId";
    private static final String UPLOAD_ID = "narComponent_CmSampleActionUploadId";
    private static final String DELETE_CONTENT_ID = "narComponent_CmSampleActionDeleteContentId";
    private static final String CREATE_DIRECTORY_ID = "narComponent_CmSampleActionCreateId";
    private static final String UPLOAD_WIZARD_ID = "narComponent_CMSamplesManagementViewIdUploadSamplesPromptId";
    private static final String CREATE_DIRECTORY_WIZARD_ID = "narComponent_CMSamplesManagementViewIdFileActionPromptId";
    private static final String WIZARD_NAME_ID = "narComponent_CMSamplesManagementViewIdFileNameTextFieldId";
    private static final String CONFIRM_ID = "narComponent_CMSamplesManagementViewIdFileActionButtonsId-1";

    public SamplesManagementPage(WebDriver driver) {
        super(driver);
    }

    @Step("Select samples path")
    public void selectPath() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeView().selectTreeRowByOrder(0);
    }

    @Step("Upload samples for CM Domain")
    public void uploadSamples(String path) throws URISyntaxException {
        getTreeView().callActionById(ActionsContainer.OTHER_GROUP_ID, UPLOAD_ID);
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
        getTreeView().callActionById(ActionsContainer.EDIT_GROUP_ID, DELETE_CONTENT_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        confirm();
    }

    @Step("Create samples directory for CM Domain")
    public void createDirectory(String cmDomainName) {
        getTreeView().callActionById(ActionsContainer.CREATE_GROUP_ID, CREATE_DIRECTORY_ID);
        Wizard wizard = Wizard.createByComponentId(driver, wait, CREATE_DIRECTORY_WIZARD_ID);
        Input name = wizard.getComponent(WIZARD_NAME_ID, ComponentType.TEXT_FIELD);
        name.setSingleStringValue(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep(500);
        confirm();
    }

    private TreeWidget getTreeView() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return TreeWidget.createById(driver, wait, SAMPLES_MANAGEMENT_WIDGET_ID);
    }

    private void confirm() {
        ConfirmationBox confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonById(CONFIRM_ID);
    }
}
