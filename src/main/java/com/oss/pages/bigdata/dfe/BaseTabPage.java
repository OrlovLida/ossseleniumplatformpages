package com.oss.pages.bigdata.dfe;

import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTabPage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(BaseTabPage.class);

    public BaseTabPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public OldTable createTabTable() {
        return OldTable
                .createByComponentDataAttributeName(driver, wait, getTableId());
    }

    protected void clickTabsContextAction(String actionLabel) {
        TabWindowWidget.create(driver, wait).callActionByLabel(actionLabel);
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