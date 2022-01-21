package com.oss.pages.bigdata.dfe.KQIs;

import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.bigdata.dfe.BaseDfePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KQIsPage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(KQIsPage.class);

    private final String ADD_NEW_KQI_LABEL = "Add New KQI";
    private final String TABLE_ID = "kqi-listAppId";
    private final String KQI_WIZARD_ID = "kqiWizardWindow";
    private final String KQI_NAME_COLUMN_LABEL = "Name";
    private final String SEARCH_INPUT_ID = "kqi-listSearchAppId";
    private final String EDIT_KQI_LABEL = "Edit KQI";
    private final String DELETE_KQI_LABEL = "Delete KQI";
    private final String CONFIRM_DELETE_LABEL = "Delete";
    private final String DETAILS_TAB = "Details";
    private final String PARAMETERS_TAB = "Parameters";
    private final String PARAMETERS_TABLE_ID = "kqi/tabs/parametersAppId";

    public KQIsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open KQIs View")
    public static KQIsPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        BaseDfePage.openDfePage(driver, basicURL, wait, "kqi");
        return new KQIsPage(driver, wait);
    }

    @Step("I click add new KQI")
    public void clickAddNewKQI() {
        clickContextActionAdd();
    }

    @Step("I check if KQI: {name} exists into table")
    public Boolean kqiExistIntoTable(String name) {
        return feedExistIntoTable(name, KQI_NAME_COLUMN_LABEL);
    }

    @Step("I select found KQI")
    public void selectFoundKQI() {
        getTable(driver, wait).selectRow(0);
    }

    @Step("I click Edit KQI")
    public void clickEditKQI() {
        clickContextActionEdit();
    }

    @Step("I click Delete KQI")
    public void clickDeleteKQI() {
        clickContextActionDelete();
    }

    @Step("I confirm the removal")
    public void clickConfirmDelete() {
        confirmDelete(CONFIRM_DELETE_LABEL);
    }

    @Step("I click Details Tab")
    public void selectDetailsTab() {
        selectTab(DETAILS_TAB);
    }

    @Step("Check label and value in details tab")
    public String checkValueForPropertyInDetails(String propertyName) {
        String propertyValue = OldPropertyPanel.create(driver, wait).getPropertyValue(propertyName);
        log.info("Value of: {} is: {}", propertyName, propertyValue);

        return propertyValue;
    }

    @Step("I click Parameters Tab")
    public void selectParametersTab() {
        selectTab(PARAMETERS_TAB);
    }

    @Step("I check if Parameters Table is not empty")
    public boolean parametersTableHasData() {
        return OldTable
                .createByComponentId(driver, wait, PARAMETERS_TABLE_ID)
                .getNumberOfRowsInTable("Name") > 0;
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
