package com.oss.pages.faultmanagement.filtermanager;

import com.oss.framework.components.portals.PopupV2;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bartosz Nowak
 */
public class FMFilterManagerPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(FMFilterManagerPage.class);
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
        log.info("Opening filter manager page");
        return new FMFilterManagerPage(driver);
    }

    @Step("Open Create New Folder Wizard")
    public FMCrateWizardPage openCreateNewFolderWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList().callAction(NEW_FOLDER_ID);
        log.info("Opening wizard to create new folder");
        return new FMCrateWizardPage(driver);
    }

    @Step("Open Create New Filter Wizard")
    public FMCrateWizardPage openCreateNewFilterWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList().callAction(NEW_FILTER_ID);
        log.info("Opening wizard to create new filter");
        return new FMCrateWizardPage(driver);
    }

    private CommonList getCommonList() {
        return CommonList.create(driver, wait, COMMON_LIST_APP_ID);
    }

    @Step("I create Folder")
    public void createFolder(String name, String description) {
        FMCrateWizardPage fmWizardPage = openCreateNewFolderWizard();
        fmWizardPage.setName(name).setDescription(description).clickAccept();
        log.info("Creating a folder name: {}, description: {}", name, description);
    }

    @Step("I create Folder")
    public void createFolder(String name, String description, String filterName) {
        FMCrateWizardPage fmWizardPage = openCreateNewFolderWizard();
        fmWizardPage.setName(name).setDescription(description);
        fmWizardPage.dragAndDropFilterByName(filterName);
        fmWizardPage.clickAccept();
        log.info("Creating a folder name: {}, description: {}, filterName: {}", name, description, filterName);

    }

    @Step("I delete folders by name")
    public void deleteFolder(String nameLabel) {
        List<CommonList.Category> categories =
                getCommonList().createCategories().stream().filter(category -> category.getValue().equals(nameLabel))
                        .collect(Collectors.toList());
        categories.forEach(category -> category.callAction("remove_action"));
        log.info("Deleting folder : {}", nameLabel);
    }

    @Step("I check if folder exists")
    public boolean checkIfFolderNameExists(String nameLabel) {
        log.info("Checking if folder : {} exists", nameLabel);
        for (int i = 0; i < 100; i++) {
            List<CommonList.Category> categories =
                    getCommonList().createCategories().stream().filter(category -> category.getValue().contains(nameLabel))
                            .collect(Collectors.toList());
            if (categories.size() > 0)
                return true;
            DelayUtils.sleep(50);
        }
        return false;
    }

    @Step("I check if folder not exists")
    public boolean checkIfFolderNameNotExists(String nameLabel) {
        log.info("Checking if folder : {} not exists", nameLabel);
        for (int i = 0; i < 100; i++) {
            List<CommonList.Category> categories =
                    getCommonList().createCategories().stream().filter(category -> category.getValue().contains(nameLabel))
                            .collect(Collectors.toList());
            if (categories.size() == 0)
                return true;
            DelayUtils.sleep(50);
        }
        return false;
    }

    @Step("I create filter")
    public void createFilter(String name, String description, String type) {
        FMCrateWizardPage fmWizardPage = openCreateNewFilterWizard();
        fmWizardPage.setName(name).setDescription(description).setTypeValue(type);
        fmWizardPage.clickOnAddConditon();
        PopupV2 popup = PopupV2.create(driver, wait);
        TreeWidget tree = TreeWidget.createByClass(driver, "tree-component", wait);
        tree.selectNodeByLabel("Adapter Name");
        popup.clickButtonByLabel("Add");
        //TODO skończyć gdy zostanie dostarczone OSSNGSA-9444
        log.info("Creating a filter name: {}, description: {}, type: {}", name, description, type);
    }
}
