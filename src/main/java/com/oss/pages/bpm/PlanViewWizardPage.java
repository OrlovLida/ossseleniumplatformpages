package com.oss.pages.bpm;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabWindowWidget;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.pages.BasePage;

public class PlanViewWizardPage extends BasePage {
    private static final String VALIDATION_TABLE_DATA_ATTRIBUTE_NAME = "plaPlanView_validationTable";

    public PlanViewWizardPage(WebDriver driver) {
        super(driver);
    }

    public void selectTab(String label) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tab = TabWindowWidget.create(driver, wait);
        tab.selectTabByLabel(label);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public boolean validationErrorsPresent() {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable oldTable = OldTable.createByComponentDataAttributeName(driver, wait, VALIDATION_TABLE_DATA_ATTRIBUTE_NAME);
        return !oldTable.hasNoData();
    }
}
