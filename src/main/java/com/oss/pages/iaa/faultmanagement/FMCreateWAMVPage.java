package com.oss.pages.iaa.faultmanagement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.list.DraggableList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class FMCreateWAMVPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(FMCreateWAMVPage.class);
    private static final String WIZARD_ID = "UserViewWizardModal";
    private static final String NAME_TEXT_FIELD_ID = "UserViewNameInput";
    private static final String DESCRIPTION_TEXT_FIELD_ID = "UserViewDescriptionInput";
    private static final String DROPDOWNLIST_AVAILABLE_ID = "Available";
    private static final String DROPDOWNLIST_SELECTED_ID = "Selected";
    private static final String WIZARD_MODAL_ID = "card-content_UserViewWizardModal";
    private final Wizard folderWizard;

    public FMCreateWAMVPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        folderWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("I set Name of the WAMV")
    public void setName(String name) {
        folderWizard.setComponentValue(NAME_TEXT_FIELD_ID, name);
        log.info("Set WAMV name {}", name);
    }

    @Step("I set description of the WAMV")
    public void setDescription(String description) {
        folderWizard.setComponentValue(DESCRIPTION_TEXT_FIELD_ID, description);
        log.info("Add following description {} to WAMV", description);
    }

    @Step("I drag and drop filter by name")
    public void dragAndDropFilterByName(String filterName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getDraggableListSelected().drop(getDraggableListAvailable().getDraggableElement(filterName));
        log.info("Drag filter {} and drop it", filterName);
    }

    @Step("I select N-th filter from filter list")
    public void selectFilterFromList(int row) {
        EditableList filters = EditableList.createById(driver, wait, WIZARD_MODAL_ID);
        filters.getRow(row).click();
        log.info("Selecting {}. filter from the list", row);
    }

    @Step("I click accept button")
    public void clickAcceptButton() {
        folderWizard.clickAccept();
        log.info("Clicking accept button");
    }

    @Step("I click Next button")
    public void clickNextButton() {
        folderWizard.clickNext();
        log.info("Clicking Next button");
    }

    private DraggableList getDraggableListAvailable() {
        return DraggableList.create(driver, wait, DROPDOWNLIST_AVAILABLE_ID);
    }

    private DraggableList getDraggableListSelected() {
        return DraggableList.create(driver, wait, DROPDOWNLIST_SELECTED_ID);
    }
}