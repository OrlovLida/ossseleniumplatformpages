package com.oss.pages.bpm;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class PlanViewWizardPage extends BasePage {
    private static final String VALIDATION_TABLE_DATA_ATTRIBUTE_NAME = "plaPlanView_validationTable";

    private static final String OBJECT_TABLE_DATA_ATTRIBUTE_NAME = "plaPlanView_objectsApp";
    private static final String PROCEED_WITH_CANCELLATION_ID = "ConfirmationBox_plaCancelObjectsWizard_confirmBox_action_button";
    private static final String CANCEL_ID = "objectsRemoveAction";
    private static final String TAB_ID = "plaPlanView_leftSideWindow";
    private static final String CLOSE_BUTTON_TITLE = "Close";

    public PlanViewWizardPage(WebDriver driver) {
        super(driver);
    }

    public void selectTab(String label) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tab = TabsWidget.createById(driver, wait, TAB_ID);
        tab.selectTabByLabel(label);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public boolean isValidationResultPresent() {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable oldTable = OldTable.createById(driver, wait, VALIDATION_TABLE_DATA_ATTRIBUTE_NAME);
        return !oldTable.hasNoData();
    }

    public void closeProcessDetailsPromt() {
        Button.createByLabel(driver, CLOSE_BUTTON_TITLE).click();
    }

    @Step("Removing all planned object")
    public void removeAllObjects() {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable oldTable = OldTable.createById(driver, wait, OBJECT_TABLE_DATA_ATTRIBUTE_NAME);
        final int totalCount = oldTable.getTotalCount();
        for (int i = 0; i < totalCount; i++) {
            oldTable.selectRow(0);
            oldTable.callAction(CANCEL_ID);
            ConfirmationBox.create(driver, wait).clickButtonById(PROCEED_WITH_CANCELLATION_ID);
        }
    }
}