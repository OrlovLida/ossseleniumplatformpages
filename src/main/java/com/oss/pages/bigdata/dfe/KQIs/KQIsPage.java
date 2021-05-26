package com.oss.pages.bigdata.dfe.KQIs;

import com.oss.pages.bigdata.dfe.BaseDfePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class KQIsPage extends BaseDfePage {
    private final String ADD_NEW_KQI_LABEL = "Add New KQI";
    private final String TABLE_ID = "kqi-listAppId";
    private final String KQI_WIZARD_ID = "kqiWizardWindow";
    private final String KQI_NAME_COLUMN_LABEL = "Name";
    private final String SEARCH_INPUT_ID = "kqi-listSearchAppId";
    private final String EDIT_KQI_LABEL = "Edit KQI";
    private final String DELETE_KQI_LABEL = "Delete KQI";

    private final KQIWizardPage kqiWizardPage;

    public KQIsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        kqiWizardPage = new KQIWizardPage(driver, wait, KQI_WIZARD_ID);
    }

    public KQIWizardPage getKqiWizardPage() {
        return kqiWizardPage;
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
