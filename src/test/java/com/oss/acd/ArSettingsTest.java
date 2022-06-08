package com.oss.acd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.acd.BaseACDPage;
import com.oss.pages.acd.settingsview.ArSettingsPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class ArSettingsTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(AsdScenarioSummaryViewTest.class);

    private ArSettingsPage arSettingsPage;
    private BaseACDPage baseACDPage;

    private static final String arSettingsViewSuffixUrl = "%s/#/view/acd/kaSettings";
    private static final String TYPE_ACTION_TEMPLATE_COMBOBOX_ID = "type";
    private static final String REASON_ACTION_TEMPLATE_COMBOBOX_ID = "reason";
    private static final String SCENARIO_ACTION_TEMPLATE_COMBOBOX_ID = "scenario";
    private static final String ACTION_TEMPLATE_TYPE_VALUE = "Software";
    private static final String ACTION_TEMPLATE_REASON_VALUE = "Fix";
    private static final String ALL_SCENARIOS = "ALL";
    private static final String DESCRIPTION_MULTI_SEARCH = "description";
    private static final String DESCRIPTION_MULTI_SEARCH_VALUE = "Desc123";
    private static final String PARAM_MULTI_SEARCH = "param";
    private static final String PARAM_MULTI_SEARCH_VALUE = "Param123";

    @BeforeClass
    public void goToArSettingsView() {
        arSettingsPage = ArSettingsPage.goToPage(driver, arSettingsViewSuffixUrl, BASIC_URL);
        baseACDPage = new BaseACDPage(driver, webDriverWait);
    }

    @Test(priority = 1, testName = "Add new Action Template", description = "Add new Action Template")
    @Description("Add new Action Template")
    public void addNewActionTemplate() {
        arSettingsPage.clickAddActionTemplate();
        arSettingsPage.chooseOptionInComboBox(TYPE_ACTION_TEMPLATE_COMBOBOX_ID, ACTION_TEMPLATE_TYPE_VALUE);
        arSettingsPage.chooseOptionInComboBox(REASON_ACTION_TEMPLATE_COMBOBOX_ID, ACTION_TEMPLATE_REASON_VALUE);
        baseACDPage.setValueInTextField(DESCRIPTION_MULTI_SEARCH, DESCRIPTION_MULTI_SEARCH_VALUE);
        baseACDPage.setValueInTextField(PARAM_MULTI_SEARCH, PARAM_MULTI_SEARCH_VALUE);
        arSettingsPage.chooseOptionInComboBox(SCENARIO_ACTION_TEMPLATE_COMBOBOX_ID, ALL_SCENARIOS);
        arSettingsPage.clickSaveButton();
        log.info("Action Template has been created");
    }

    @Test(priority = 2, testName = "Verify if created Action Template exists", description = "Verify if created Action Template exists")
    @Description("Verify if created Action Template exists")
    public void searchingForActionTemplate() {

        if (!arSettingsPage.searchingThroughActionTemplates(DESCRIPTION_MULTI_SEARCH, DESCRIPTION_MULTI_SEARCH_VALUE)) {
            log.error("Action Template table is empty");
            arSettingsPage.clearMultiSearchBox(DESCRIPTION_MULTI_SEARCH);
            Assert.fail();
        }

        if (!arSettingsPage.isThereActionTemplateCreated()) {
            log.error("Action Template table doesn't contain data for provided filters");
            arSettingsPage.clearMultiSearchBox(DESCRIPTION_MULTI_SEARCH);
            Assert.fail();
        }
        log.info("Action Template has been found. I clear MultiSearchBox.");
        arSettingsPage.clearMultiSearchBox(DESCRIPTION_MULTI_SEARCH);
    }

    @Test(priority = 3, testName = "Delete Action Template", description = "Delete Action Template")
    @Description("Delete Action Template")
    public void deleteActionTemplate() {

        if (!arSettingsPage.searchingThroughActionTemplates(DESCRIPTION_MULTI_SEARCH, DESCRIPTION_MULTI_SEARCH_VALUE)) {
            log.error("Action Template table is empty");
            arSettingsPage.clearMultiSearchBox(DESCRIPTION_MULTI_SEARCH);
            Assert.fail();
        }
        arSettingsPage.selectFirstActionTemplateFromTable();
        arSettingsPage.deleteActionTemplate();
        arSettingsPage.clearMultiSearchBox(DESCRIPTION_MULTI_SEARCH);
    }
}
