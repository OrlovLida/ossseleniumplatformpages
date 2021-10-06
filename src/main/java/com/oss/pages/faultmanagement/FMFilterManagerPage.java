package com.oss.pages.faultmanagement;

import com.oss.framework.components.portals.PopupV2;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;

public class FMFilterManagerPage extends BasePage {

    private static final String HTTP_URL_TO_FM_FILTER_MANAGER = "%s/#/view/filter-manager/manager?perspective=LIVE";
    private static final String NEW_FOLDER_ID = "new_folder";
    private static final String NEW_FILTER_ID = "new_filter";
    private static final String COMMON_LIST_APP_ID = "_FilterManagerList";
    private static final String ADD_BUTTON_LABEL = "Add";


    public FMFilterManagerPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open Filter Manager Page")
    public static FMFilterManagerPage goToFilterManagerPage(WebDriver driver, String baseURL) {
        driver.get(String.format(HTTP_URL_TO_FM_FILTER_MANAGER, baseURL));
        return new FMFilterManagerPage(driver);
    }

    @Step("Open Create New Folder Wizard")
    public FMCrateWizardPage openCreateNewFolderWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList().callAction(NEW_FOLDER_ID);
        return new FMCrateWizardPage(driver);
    }

    @Step("Open Create New Folder Wizard")
    public FMCrateWizardPage openCreateNewFilterWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList().callAction(NEW_FILTER_ID);
        return new FMCrateWizardPage(driver);
    }

    private CommonList getCommonList() {
        return CommonList.create(driver, wait, COMMON_LIST_APP_ID);
    }

    @Step("Create Folder")
    public void createFolder(String name, String description) {
        FMCrateWizardPage fmWizardPage = openCreateNewFolderWizard();
        fmWizardPage.setName(name);
        fmWizardPage.setDescription(description);
        fmWizardPage.clickAccept();
    }

    @Step("Create Folder")
    public void createFilter(String name, String description, String type) {
        FMCrateWizardPage fmWizardPage = openCreateNewFilterWizard();
        fmWizardPage.setName(name);
        fmWizardPage.setDescription(description);
        fmWizardPage.setTypeValue(type);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        fmWizardPage.clickOnConditon(ADD_BUTTON_LABEL);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        ConditionPopup popupV2 = new ConditionPopup(driver, wait);
//        popupV2.typeAndEnterInGlobalSearch("coś do wyszukania");

        PopupV2 popupV2 = PopupV2.create(driver,wait);
        popupV2.setComponentValue("search","coś tam wpisz", SEARCH_FIELD);
//        popupV2.getComponent("search", SEARCH_FIELD).click();
//        popupV2.getComponent("search", SEARCH_FIELD).setSingleStringValue("halo halo co się dzieje");
    }
}
