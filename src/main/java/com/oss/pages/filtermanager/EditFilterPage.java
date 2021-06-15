package com.oss.pages.filtermanager;

import com.oss.framework.components.list.DropdownList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.widgets.Wizard;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;


public class EditFilterPage extends FilterManagerPage {

    public EditFilterPage(WebDriver driver) {
        super(driver);
    }

    private static final String TO_DROP_XPATH = "//div[@data-rbd-droppable-id ='filterManager_wizard_rel_enabled']";
    private static final String TO_TAKE_XPATH = "//div[@class='btn-drag']";
    private static final String ACCEPT_BUTTON_ID = "wizard-submit-button-webFilter_wizard_filter_widget";
    private static final String WIZARD_ID = "webFilter_wizard_filter_widget";

    private Wizard folderWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);


    @Step("Change Folder for filer using drag and drop")
    public EditFilterPage changeFolderForFilter(String folderName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        DropdownList available_folders = DropdownList.create(driver, wait, "Available folders");
        DropdownList enabled_folders = DropdownList.create(driver, wait, "Enabled folders");
        DragAndDrop.DraggableElement source = available_folders.getDraggableElement(folderName);
        enabled_folders.drop(source);
        Assertions.assertThat(enabled_folders.sizeDropdownList()>0);

 //     DragAndDrop.dragAndDrop(TO_TAKE_XPATH, TO_DROP_XPATH, driver);
        return this;
    }

    @Step("Click on Accept and close the wizard")
    public FilterManagerPage clickAccept() {
        DelayUtils.waitForPageToLoad(driver, wait);
        folderWizard.clickActionById(ACCEPT_BUTTON_ID);
        return new FilterManagerPage(driver);
    }
}
