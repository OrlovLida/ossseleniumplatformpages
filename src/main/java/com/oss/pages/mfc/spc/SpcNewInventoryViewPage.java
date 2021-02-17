package com.oss.pages.mfc.spc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.pages.platform.NewInventoryViewPage;

public class SpcNewInventoryViewPage extends NewInventoryViewPage {

    public static SpcNewInventoryViewPage create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return new SpcNewInventoryViewPage(driver, wait);
    }

    protected SpcNewInventoryViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public SpcWizardPage openCreateSpcWizard() {
        callAction("CREATE", "spcCreate");
        return SpcWizardPage.create(driver, wait, this);
    }

    public SpcDeleteWizardPage openDeleteSpcWizardForSelectedSpc() {
        callAction("EDIT", "spcDelete");
        return SpcDeleteWizardPage.create(driver, wait, this);
    }

    public SpcWizardPage openModifySpcWizardForSelectedSpc() {
        callAction("EDIT", "spcModify");
        return SpcWizardPage.create(driver, wait, this);
    }

    public SpcPropertyPanel getPropetiesPanelForSelectedSpc() {
        PropertyPanel propertyPanel = getPropertyPanel();
        return SpcPropertyPanel.create(propertyPanel);
    }

    public SpcNewInventoryViewPage searchAndSelect(String text) {
        getMainTable().clearAllFilters();
        searchObject(text);
        selectFirstRow();
        return this;
    }

}
