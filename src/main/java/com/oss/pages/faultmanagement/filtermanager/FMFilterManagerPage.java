package com.oss.pages.faultmanagement.filtermanager;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.prompts.Popup;
import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * @author Bartosz Nowak
 */
public class FMFilterManagerPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(FMFilterManagerPage.class);
    private static final String HTTP_URL_TO_FM_FILTER_MANAGER = "%s/#/view/filter-manager/manager?perspective=LIVE";
    private static final String NEW_FOLDER_ID = "new_folder";
    private static final String NEW_FILTER_ID = "new_filter";
    private static final String COMMON_LIST_APP_ID = "_FilterManagerList";
    private static final String REMOVE_ACTION_ID = "remove_action";
    private static final String ADAPTER_NAME_LABEL = "Adapter Name";
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
    public FMCreateWizardPage openCreateNewFolderWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList().callAction(NEW_FOLDER_ID);
        log.info("Opening wizard to create new folder");
        return new FMCreateWizardPage(driver);
    }

    @Step("Open Create New Filter Wizard")
    public FMCreateWizardPage openCreateNewFilterWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList().callAction(NEW_FILTER_ID);
        log.info("Opening wizard to create new filter");
        return new FMCreateWizardPage(driver);
    }

    @Step("I create Folder")
    public void createFolder(String name, String description) {
        FMCreateWizardPage fmWizardPage = openCreateNewFolderWizard();
        fmWizardPage.setName(name).setDescription(description).clickAccept();
        log.info("Creating a folder name: {}, description: {}", name, description);
    }

    @Step("I create Folder")
    public void createFolder(String name, String description, String filterName) {
        FMCreateWizardPage fmWizardPage = openCreateNewFolderWizard();
        fmWizardPage.setName(name).setDescription(description);
        fmWizardPage.dragAndDropFilterByName(filterName);
        fmWizardPage.clickAccept();
        log.info("Creating a folder name: {}, description: {}, filterName: {}", name, description, filterName);
    }

    @Step("I delete folders by name")
    public void deleteFolder(String nameLabel) {
        DelayUtils.sleep(2000);
        List<CommonList.Category> categories =
                getCommonList().createCategories().stream().filter(category -> category.getValue().equals(nameLabel))
                        .collect(Collectors.toList());
        categories.forEach(category -> category.callAction(REMOVE_ACTION_ID));
        log.info("Deleting folder : {}", nameLabel);
    }

    @Step("I check if folder exists")
    public boolean checkIfFolderNameExists(String nameLabel) {
        DelayUtils.sleep(2000);
        log.info("Checking if folder : {} exists", nameLabel);
        for (int i = 0; i < 100; i++) {
            List<CommonList.Category> categories =
                    getCommonList().createCategories().stream().filter(category -> category.getValue().contains(nameLabel))
                            .collect(Collectors.toList());
            if (!categories.isEmpty()) {
                return true;
            }
            DelayUtils.sleep(50);
        }
        return false;
    }

    @Step("I check if folder not exists")
    public boolean checkIfFolderNameNotExists(String nameLabel) {
        DelayUtils.sleep(2000);
        log.info("Checking if folder : {} not exists", nameLabel);
        for (int i = 0; i < 100; i++) {
            List<CommonList.Category> categories =
                    getCommonList().createCategories().stream().filter(category -> category.getValue().contains(nameLabel))
                            .collect(Collectors.toList());
            if (categories.isEmpty()) {
                return true;
            }
            DelayUtils.sleep(50);
        }
        return false;
    }

    @Step("I create filter")
    public void createFilter(String name, String description, String type) {
        FMCreateWizardPage fmWizardPage = openCreateNewFilterWizard();
        fmWizardPage.setName(name).setDescription(description).setTypeValue(type);
        fmWizardPage.clickOnAddConditon();
        Popup popup = Popup.create(driver, wait);
        TreeComponent tree = popup.getTreeComponent();
        tree.getNodeByLabelsPath(ADAPTER_NAME_LABEL);
        popup.clickButtonByLabel(ADD_BUTTON_LABEL);
        //TODO skończyć gdy zostanie dostarczone OSSNGSA-9444
        log.info("Creating a filter name: {}, description: {}, type: {}", name, description, type);
    }

    private CommonList getCommonList() {
        return CommonList.create(driver, wait, COMMON_LIST_APP_ID);
    }
}
