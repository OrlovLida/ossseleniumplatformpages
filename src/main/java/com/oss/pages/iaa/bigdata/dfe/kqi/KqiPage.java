package com.oss.pages.iaa.bigdata.dfe.kqi;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.iaa.bigdata.dfe.BaseDfePage;

import io.qameta.allure.Step;

public class KqiPage extends BaseDfePage {

    private static final String ADD_NEW_KQI_LABEL = "Add New KQI";
    private static final String TABLE_ID = "kqi-listAppId";
    private static final String KQI_NAME_COLUMN_LABEL = "Name";
    private static final String SEARCH_INPUT_ID = "input_kqi-listSearchAppId";
    private static final String EDIT_KQI_LABEL = "Edit KQI";
    private static final String DELETE_KQI_LABEL = "Delete KQI";
    private static final String CONFIRM_DELETE_LABEL = "Delete";
    private static final String DETAILS_TAB = "Details";
    private static final String PARAMETERS_TAB = "Parameters";
    private static final String PARAMETERS_TABLE_ID = "kqi/tabs/parametersAppId";
    private static final String PROPERTY_PANEL_ID = "detailsId";
    private static final String NAME_PROPERTY = "Name";
    private static final String FORMULA_PROPERTY = "Formula";
    private static final String TAB_WIDGET_ID = "card-content_kqiTabsWindow";

    public KqiPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Open KQIs View")
    public static KqiPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        BaseDfePage.openDfePage(driver, basicURL, wait, "kqi");
        return new KqiPage(driver, wait);
    }

    @Step("Click add new KQI")
    public void clickAddNewKQI() {
        clickContextActionAdd();
    }

    @Step("Check if KQI: {name} exists into table")
    public boolean kqiExistIntoTable(String name) {
        return feedExistIntoTable(name, KQI_NAME_COLUMN_LABEL);
    }

    @Step("Select found KQI")
    public void selectFoundKQI() {
        getTable(driver, wait).selectRow(0);
    }

    @Step("Click Edit KQI")
    public void clickEditKQI() {
        clickContextActionEdit();
    }

    @Step("Click Delete KQI")
    public void clickDeleteKQI() {
        clickContextActionDelete();
    }

    @Step("Confirm the removal")
    public void clickConfirmDelete() {
        confirmDelete(CONFIRM_DELETE_LABEL);
    }

    @Step("Click Details Tab")
    public void selectDetailsTab() {
        selectTab(TAB_WIDGET_ID, DETAILS_TAB);
    }

    @Step("Check name value in details tab")
    public String checkNameInPropertyPanel() {
        return checkValueInPropertyPanel(PROPERTY_PANEL_ID, NAME_PROPERTY);
    }

    @Step("Check label value in details tab")
    public String checkFormulaInPropertyPanel() {
        return checkValueInPropertyPanel(PROPERTY_PANEL_ID, FORMULA_PROPERTY);
    }

    @Step("Click Parameters Tab")
    public void selectParametersTab() {
        selectTab(TAB_WIDGET_ID, PARAMETERS_TAB);
    }

    @Step("Check if Parameters Table is not empty")
    public boolean parametersTableHasData() {
        return OldTable
                .createById(driver, wait, PARAMETERS_TABLE_ID)
                .countRows("Name") > 0;
    }

    @Override
    public String getTableId() {
        return TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_NEW_KQI_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_KQI_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return DELETE_KQI_LABEL;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }
}