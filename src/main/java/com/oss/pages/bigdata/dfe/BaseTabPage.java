package com.oss.pages.bigdata.dfe;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsWidget;

public abstract class BaseTabPage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(BaseTabPage.class);
    private final TabsWidget tabsWidget;

    protected BaseTabPage(WebDriver driver, WebDriverWait wait, String widgetId) {
        super(driver, wait);
        tabsWidget = TabsWidget.createById(driver, wait, widgetId);
    }

    public OldTable createTabTable() {
        return OldTable
                .createById(driver, wait, getTableId());
    }

    protected void clickTabsContextAction(String actionLabel) {
        tabsWidget.callActionByLabel(actionLabel);
        log.debug("Click context action: {}", actionLabel);
    }

    protected void clickTabsContextActionAdd() {
        clickTabsContextAction(getContextActionAddLabel());
    }

    protected void clickTabsContextActionEdit() {
        clickTabsContextAction(getContextActionEditLabel());
    }

    protected void clickTabsContextActionDelete() {
        clickTabsContextAction(getContextActionDeleteLabel());
    }

    protected void selectTabTableRow(int row) {
        createTabTable().selectRow(row);
    }
}
