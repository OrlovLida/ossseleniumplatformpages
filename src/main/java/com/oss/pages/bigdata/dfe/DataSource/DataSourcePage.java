package com.oss.pages.bigdata.dfe.DataSource;

import com.oss.framework.components.portals.DropdownList;
import com.oss.pages.bigdata.dfe.BaseDfePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DataSourcePage extends BaseDfePage {

    private static final String TABLE_ID = "datasource/datasource-listAppId";
    private final String SEARCH_INPUT_ID = "datasource/datasource-listSearchAppId";
    private final String ADD_NEW_DS_LABEL = "ADD";
    private final String CREATE_DS_QUERY = "OPEN_MODAL_QUERY";
    private final String CREATE_DS_CSV = "OPEN_MODAL_CSV";
    private final String CREATE_DS_KAFKA = "OPEN_MODAL_KAFKA";
    private final String DATA_SOURCE_NAME_COLUMN = "Name";
    private final String EDIT_DS_LABEL = "Edit";
    private final String DELETE_DS_LABEL = "Delete";
    private final String CONFIRM_DELETE_LABEL = "Delete";

    public DataSourcePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open Data Source View")
    public static DataSourcePage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);

        BaseDfePage.openDfePage(driver, basicURL, wait, "datasource");
        return new DataSourcePage(driver, wait);
    }

    @Step("I click add new")
    public void clickAddNewDS() {
        clickContextActionAdd();
    }

    @Step("I select Create Data Source - Query Result")
    public void selectDSFromQuery() {
        DropdownList.create(driver, wait).selectOptionWithId(CREATE_DS_QUERY);
    }

    @Step("I select Create Data Source - CSV File")
    public void selectDSFromCSV() {
        DropdownList.create(driver, wait).selectOptionWithId(CREATE_DS_CSV);
    }

    @Step("I select Create Data Source - Kafka")
    public void selectDSFromKafka() {
        DropdownList.create(driver, wait).selectOptionWithId(CREATE_DS_KAFKA);
    }

    @Step("I check if data source: {name} exists into table")
    public Boolean dataSourceExistIntoTable(String name) {
        return feedExistIntoTable(name, DATA_SOURCE_NAME_COLUMN);
    }

    @Step("I select found Data Source")
    public void selectFoundDataSource() {
        getTable(driver, wait).selectRow(0);
    }

    @Step("I click Edit Data Source")
    public void clickEditDS() {
        clickContextActionEdit();
    }

    @Step("I click Delete Data Source")
    public void clickDeleteDS() {
        clickContextActionDelete();
    }

    @Step("I confirm the removal")
    public void clickConfirmDelete() {
        confirmDelete(CONFIRM_DELETE_LABEL);
    }

    @Override
    public String getTableId() {
        return TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_NEW_DS_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_DS_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return DELETE_DS_LABEL;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }
}
