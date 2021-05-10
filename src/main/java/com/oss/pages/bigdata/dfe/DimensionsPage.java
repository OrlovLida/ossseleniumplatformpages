package com.oss.pages.bigdata.dfe;

import com.oss.framework.utils.DelayUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DimensionsPage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(DimensionsPage.class);

    private static final String TABLE_ID = "dimension-listAppId";

    private final String ADD_NEW_DIMENSION_LABEL = "Add New Dimension";
    private final String EDIT_DIMENSION_LABEL = "Edit Dimension";
    private final String DELETE_DIMENSION_LABEL = "Delete Dimension";
    private final String SEARCH_INPUT_ID = "dimension-listSearchAppId";

    private final String NAME_COLUMN_LABEL = "Name";

    private DimensionsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open Dimensions View")
    public static DimensionsPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);

        BaseDfePage.openDfePage(driver, basicURL, wait, "dimension");
        return new DimensionsPage(driver, wait);
    }

    @Step("I click add new Dimension")
    public void clickAddNewDimension() {
        clickContextActionAdd();
    }

    @Step("I click edit Dimension")
    public void clickEditDimension() {
        clickContextActionEdit();
    }

    @Step("I click delete Dimension")
    public void clickDeleteDimension() {
        clickContextActionDelete();
    }

    @Step("I check if Dimension: {dimensionName} exists into the table")
    public Boolean dimensionExistsIntoTable(String dimensionName) {
        DelayUtils.sleep(2000);
        searchFeed(dimensionName);
        DelayUtils.waitForPageToLoad(driver, wait);
        int numberOfRowsInTable = getNumberOfRowsInTable(NAME_COLUMN_LABEL);
        log.trace("Found rows count: {}. Filtered by {}", numberOfRowsInTable, dimensionName);
        return numberOfRowsInTable == 1;
    }

    @Step("I select found Dimension")
    public void selectFoundDimension() {
        getTable(driver, wait).selectRow(0);
    }

    @Override
    public String getTableId() {
        return TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_NEW_DIMENSION_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_DIMENSION_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return DELETE_DIMENSION_LABEL;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }

}
