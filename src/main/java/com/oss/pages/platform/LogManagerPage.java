package com.oss.pages.platform;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

public class LogManagerPage extends BasePage {
    private static final String CM_LOGS_BUTTON = "tab_CmLogsTabId";
    private static final String LOGS_TABLE_DATA_ATTRIBUTE_NAME = "CmLogsTableApp";

    public LogManagerPage(WebDriver driver) {
        super(driver);
    }

    public String getStatus() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createById(driver, CM_LOGS_BUTTON).click();
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface statusTable = OldTable.createById(driver, wait, LOGS_TABLE_DATA_ATTRIBUTE_NAME);
        String status = statusTable.getCellValue(0, "Status");
        return status;
    }
}
