package com.oss.pages.reconciliation;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
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
    private static final String UPLOAD_WIZARD_ID = "narComponent_CMSamplesManagementViewIdUploadSamplesPromptId_prompt-card";
    private static final String CREATE_DIRECTORY_WIZARD_ID = "narComponent_CMSamplesManagementViewIdFileActionPromptId_prompt-card";
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
            DelayUtils.waitForPageToLoad(driver, wait);
            Input input = wizard.getComponent(UPLOAD_WIZARD_ID, ComponentType.FILE_CHOOSER);
            input.setSingleStringValue(absolutePath);
            wizard.clickOK();
        } catch (URISyntaxException e) {
            throw new URISyntaxException("Cant load file", e.getReason());
        }
    }

    @Step("Upload samples from path for CM Domain")
    public void uploadSamplesFromPath(String packagePath) throws IOException, URISyntaxException {
        URL res = getClass().getClassLoader().getResource(packagePath);
        Stream<Path> stream = null;
        try {
            assert res != null;
            stream = Files.walk(Paths.get(res.toURI()).toRealPath());
            List<String> fileNames = stream
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(n -> !packagePath.endsWith(n))
                    .collect(Collectors.toList());

            for (String fileName : fileNames
            ) {
                DelayUtils.waitForPageToLoad(driver, wait);
                uploadSamples(packagePath + "/" + fileName);
            }
        } catch (URISyntaxException e) {
            throw new URISyntaxException("Cant load file", e.getReason());
        } finally {
            assert stream != null;
            stream.close();
        }
    }

    @Step("Delete samples for CM Domain")
    public void deleteDirectoryContent() {
        getTreeView().callActionById(ActionsContainer.EDIT_GROUP_ID, DELETE_CONTENT_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard wizard = Wizard.createByComponentId(driver, wait, CREATE_DIRECTORY_WIZARD_ID);
        wizard.clickButtonById(CONFIRM_ID);
    }

    @Step("Create samples directory for CM Domain")
    public void createDirectory(String cmDomainName) {
        getTreeView().callActionById(ActionsContainer.CREATE_GROUP_ID, CREATE_DIRECTORY_ID);
        Wizard wizard = Wizard.createByComponentId(driver, wait, CREATE_DIRECTORY_WIZARD_ID);
        Input name = wizard.getComponent(WIZARD_NAME_ID, ComponentType.TEXT_FIELD);
        name.setSingleStringValue(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep(500);
        wizard.clickButtonById(CONFIRM_ID);
    }

    private TreeWidget getTreeView() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return TreeWidget.createById(driver, wait, SAMPLES_MANAGEMENT_WIDGET_ID);
    }
}
