package com.oss.acd;

import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.Button;
import com.oss.pages.acd.BaseACDPage;
import com.oss.pages.acd.settingsView.ArSettingsPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class ArSettingsTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(AsdScenarioSummaryViewTest.class);

    private ArSettingsPage arSettingsPage;
    private BaseACDPage baseACDPage;

    private final String arSettingsViewSuffixUrl = "%s/#/view/acd/kaSettings";
    private String TYPE_ACTION_TEMPLATE_COMBOBOX_ID = "type-input";
    private String REASON_ACTION_TEMPLATE_COMBOBOX_ID = "reason-input";
    private String SCENARIO_ACTION_TEMPLATE_COMBOBOX_ID = "scenario-input";
    private String ACTION_TEMPLATE_TYPE_VALUE = "Software";
    private String ACTION_TEMPLATE_REASON_VALUE = "Fix";
    private String ALL_SCENARIOS = "ALL";
    private String DESCRIPTION_COMBO_BOX = "description";
    private String DESCRIPTION_COMBO_BOX_VALUE = "Test123!?@Desc";
    private String PARAM_COMBO_BOX = "param";
    private String PARAM_COMBO_BOX_VALUE = "Test123!?@Param";
    private String SUBSYSTEMS_HEALTH_TAB = "Subsystems Health";

    @BeforeClass
    public void goToArSettingsView() {
        arSettingsPage = ArSettingsPage.goToPage(driver, arSettingsViewSuffixUrl, BASIC_URL);
        baseACDPage = new BaseACDPage(driver, webDriverWait);
    }

    @Test(priority = 1, testName = "Add new Action Template", description = "Add new Action Template")
    @Description("Add new Action Template")
    public void addNewActionTemplate() {
        log.info("Waiting in method addNewActionTemplate");
        arSettingsPage.clickAddActionTemplate();
        arSettingsPage.chooseOptionInComboBox(TYPE_ACTION_TEMPLATE_COMBOBOX_ID, ACTION_TEMPLATE_TYPE_VALUE);
        arSettingsPage.chooseOptionInComboBox(REASON_ACTION_TEMPLATE_COMBOBOX_ID, ACTION_TEMPLATE_REASON_VALUE);
        arSettingsPage.setValueInInputField(DESCRIPTION_COMBO_BOX, DESCRIPTION_COMBO_BOX_VALUE);
        arSettingsPage.setValueInInputField(PARAM_COMBO_BOX, PARAM_COMBO_BOX_VALUE);
        arSettingsPage.chooseOptionInComboBox(SCENARIO_ACTION_TEMPLATE_COMBOBOX_ID, ALL_SCENARIOS);
        arSettingsPage.clickSaveButton();
        log.info("Action Template has been created");
    }

    @Test(priority = 2, testName = "Verify if created Action Template exists", description = "Verify if created Action Template exists")
    @Description("Verify if created Action Template exists")
    public void searchingForActionTemplate() {

        if (!arSettingsPage.searchingThroughActionTemplates(DESCRIPTION_COMBO_BOX, DESCRIPTION_COMBO_BOX_VALUE)) {
            log.error("Action Template table is empty");
            Assert.fail();
        }

        if (!arSettingsPage.isThereActionTemplateCreated()) {
            log.error("Action Template table doesn't contain data for provided filters");
            Assert.fail();
        }
        log.info("Action Template has been found");
    }

    @Test(priority = 3, testName = "Delete Action Template", description = "Delete Action Template")
    @Description("Delete Action Template")
    public void deleteActionTemplate() {
        arSettingsPage.selectFirstActionTemplateFromTable();
        arSettingsPage.deleteActionTemplate();
    }

    @Test(priority = 4, testName = "Check Subsystems Health", description = "Check Subsystems Health")
    @Description("Check Subsystems Health")
    public void checkSubsystemsHealth() {
        arSettingsPage.goToSubsystemsHealthTab(SUBSYSTEMS_HEALTH_TAB);
        Assert.assertTrue(arSettingsPage.isSubsystemUpAndRunning());
    }
}
