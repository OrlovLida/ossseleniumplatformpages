package com.oss.pages.filtermanager;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.list.DraggableList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.widgets.Wizard;

import io.qameta.allure.Step;

public class EditFilterPage extends FilterManagerPage {

    private static final String TO_DROP_XPATH = "//div[@data-rbd-droppable-id ='filterManager_wizard_rel_enabled']";
    private static final String TO_TAKE_XPATH = "//div[@class='btn-drag']";
    private static final String ACCEPT_BUTTON_ID = "wizard-submit-button-webFilter_wizard_filter_widget";
    private static final String WIZARD_ID = "webFilter_wizard_filter_widget";
    private Wizard folderWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    public EditFilterPage(WebDriver driver) {
        super(driver);
    }

    @Step("Change Folder for filer using drag and drop")
    public EditFilterPage changeFolderForFilter(String folderName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        DraggableList availableFolders = DraggableList.create(driver, wait, "Available folders");
        DraggableList enabledFolders = DraggableList.create(driver, wait, "Enabled folders");
        DragAndDrop.DraggableElement source = availableFolders.getDraggableElement(folderName);
        enabledFolders.drop(source);
        //     DragAndDrop.dragAndDrop(TO_TAKE_XPATH, TO_DROP_XPATH, driver);
        return this;
    }

    @Step("Click on Accept and close the wizard")
    public FilterManagerPage clickAccept() {
        DelayUtils.waitForPageToLoad(driver, wait);
        folderWizard.clickButtonById(ACCEPT_BUTTON_ID);
        return new FilterManagerPage(driver);
    }
}
