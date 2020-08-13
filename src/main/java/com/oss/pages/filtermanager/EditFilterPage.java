package com.oss.pages.filtermanager;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.widgets.Wizard;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;


public class EditFilterPage extends FilterManagerPage {

    public EditFilterPage(WebDriver driver){
        super(driver);
    }

    private final String TO_DROP_XPATH = "//div[@data-rbd-droppable-id ='filterManager_wizard_rel_enabled']";
    private final String TO_TAKE_XPATH = "//div[@class='btn-drag']";
    private final String ACCEPT_BUTTON_ID = "wizard-submit-button-webFilter_wizard_filter_widget";
    private final String WIZARD_ID = "webFilter_wizard_filter_widget";

    private Wizard folderWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);


    @Step("Change Folder for filer using drag and drop")
    public EditFilterPage changeFolderForFilter(){
        DelayUtils.waitForPageToLoad(driver,wait);
        DragAndDrop.dragAndDrop(TO_TAKE_XPATH, TO_DROP_XPATH, driver);
        return this;
    }

    @Step("Click on Accept and close the wizard")
    public FilterManagerPage clickAccept(){
        DelayUtils.waitForPageToLoad(driver,wait);
        folderWizard.clickActionById(ACCEPT_BUTTON_ID);
        return new FilterManagerPage(driver);
    }
}
