package com.oss.pages.faultmanagement.filtermanager;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.categorylist.CategoryList;
import com.oss.framework.components.prompts.Popup;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.pages.administration.BaseManagerPage;

import io.qameta.allure.Step;

/**
 * @author Bartosz Nowak
 */
public class FMFilterManagerPage extends BaseManagerPage {
    private static final Logger log = LoggerFactory.getLogger(FMFilterManagerPage.class);
    private static final String HTTP_URL_TO_FM_FILTER_MANAGER = "%s/#/view/filter-manager/manager?perspective=LIVE";
    private static final String NEW_FOLDER_ID = "new_folder";
    private static final String NEW_FILTER_ID = "new_filter";
    private static final String COMMON_LIST_APP_ID = "_FilterManagerList";
    private static final String REMOVE_ACTION_ID = "remove_action";
    private static final String SEARCH_FIELD_ID = "search";

    private static final String ADAPTER_NAME_LABEL = "Adapter Name";
    private static final String PERCEIVED_SEVERITY_LABEL = "Perceived Severity";
    private static final String ADD_BUTTON_LABEL = "Add";
    private static final String NAME_LABEL = "Name";

    public FMFilterManagerPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Open Filter Manager Page")
    public static FMFilterManagerPage goToFilterManagerPage(WebDriver driver, String baseURL) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        driver.get(String.format(HTTP_URL_TO_FM_FILTER_MANAGER, baseURL));
        log.info("Opening filter manager page");
        return new FMFilterManagerPage(driver, wait);
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

    @Step("I create Folder with filter")
    public void createFolder(String name, String description, String filterName) {
        FMCreateWizardPage fmWizardPage = openCreateNewFolderWizard();
        fmWizardPage.setName(name).setDescription(description);
        fmWizardPage.dragAndDropFolderByName(filterName);
        fmWizardPage.clickAccept();
        log.info("Creating a folder name: {}, description: {}, filterName: {}", name, description, filterName);
    }

    @Step("I delete folders by name")
    public void deleteFolder(String nameLabel) {
        DelayUtils.sleep(2000);
        List<CategoryList> categories =
                getCommonList().getCategories().stream().filter(category -> category.getValue().equals(nameLabel))
                        .collect(Collectors.toList());
        categories.forEach(category -> category.callAction(REMOVE_ACTION_ID));
        log.info("Deleting folder : {}", nameLabel);
    }

    @Step("I check if folder exists")
    public boolean checkIfFolderNameExists(String nameLabel) {
        DelayUtils.sleep(2000);
        log.info("Checking if folder : {} exists", nameLabel);
        for (int i = 0; i < 100; i++) {
            List<CategoryList> categories =
                    getCommonList().getCategories().stream().filter(category -> category.getValue().contains(nameLabel))
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
            List<CategoryList> categories =
                    getCommonList().getCategories().stream().filter(category -> category.getValue().contains(nameLabel))
                            .collect(Collectors.toList());
            if (categories.isEmpty()) {
                return true;
            }
            DelayUtils.sleep(50);
        }
        return false;
    }

    public boolean checkIfFilterExists(String filterName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getCommonList().isRowDisplayed(NAME_LABEL, filterName);
    }

    @Step("I search for text in View")
    public void searchInFilterManager(String searchedText) {
        AdvancedSearch search = AdvancedSearch.createByWidgetId(driver, wait, COMMON_LIST_APP_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        search.fullTextSearch(searchedText);
        DelayUtils.sleep(8000); //  TODO change it after fix OSSNGSA-11102
        log.info("Searching in Filter Manager view");
    }

    @Step("Expand found category")
    public void expandFilterList(String categoryName) {
        getList().expandCategory(categoryName);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I expanded category: {}", categoryName);
    }

    @Step("Search for element")
    public void searchForElement(String elementName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        searchInList(SEARCH_FIELD_ID, elementName);
        log.info("Searching element {}", elementName);
    }

    @Step("Click Delete Filter")
    public void clickDeleteFilter(String filterName) {
        clickDeleteElementOnList(filterName);
        log.info("Click delete Filter {}", filterName);
    }

    @Step("I create filter")
    public void createFilter(String name, String description, String type, String filterName) {
        FMCreateWizardPage fmWizardPage = openCreateNewFilterWizard();
        fmWizardPage.setName(name).setDescription(description).setTypeValue(type);
        fmWizardPage.clickSwitcher("mode");
        fmWizardPage.setValueInExpressionEditor("FilterValueInput", "[Perceived Severity] LIKE REGEX \"2\"");
        fmWizardPage.dragAndDropFilterByName(filterName);
        fmWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, wait);
        //TODO add second method after fix OSSWEB-19284
//        fmWizardPage.clickOnAddCondition();
//        getPopup().getComponent("search").setSingleStringValueContains(PERCEIVED_SEVERITY_LABEL);
//        getTreeComponentInPopUp().getNodeByLabelsPath(PERCEIVED_SEVERITY_LABEL);
//        getPopup().clickButtonByLabel(ADD_BUTTON_LABEL);
    }

    private CommonList getCommonList() {
        return CommonList.create(driver, wait, COMMON_LIST_APP_ID);
    }

    private Popup getPopup() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return Popup.create(driver, wait);
    }

    @Override
    public String getListId() {
        return COMMON_LIST_APP_ID;
    }
}