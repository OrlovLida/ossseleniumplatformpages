package com.oss.pages.bigdata.dfe.serverGroup;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.dfe.BaseDfePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerGroupPage extends BaseDfePage {


    private static final Logger log = LoggerFactory.getLogger(ServerGroupPage.class);
    private static final String TABLE_ID = "server-groupAppId";
    private final String SEARCH_INPUT_ID = "server-groupSearchAppId";
    private final String NAME_COLUMN_LABEL = "Name";
    private final String ADD_NEW_SERVER_GROUP_LABEL = "Add New Server Group";
    private final String EDIT_SERVER_GROUP_LABEL = "Edit Server Group";


    public ServerGroupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        serverGroupPopup = new ServerGroupPopupPage(driver, wait);
    }

    private final ServerGroupPopupPage serverGroupPopup;
    public ServerGroupPopupPage getServerGroupPopup() {
        return serverGroupPopup;
    }

    @Step("I Open Server Group View")
    public static ServerGroupPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        BaseDfePage.openDfePage(driver, basicURL, wait, "server-group");
        return new ServerGroupPage(driver, wait);
    }

    @Step("I click add new Server Group")
    public void clickAddNewServerGroup() {
        clickContextActionAdd();
    }

    public Boolean serverGroupExistIntoTable(String serverGroupName) {
        searchFeed(serverGroupName);
        DelayUtils.waitForPageToLoad(driver, wait);
        int numberOfRowsInTable = getNumberOfRowsInTable(NAME_COLUMN_LABEL);
        log.trace("Fount rows count: {}. Filtered by {}", numberOfRowsInTable, serverGroupName);
        return numberOfRowsInTable == 1;
    }

    @Step("I click edit Server Group")
    public void clickEditServerGroup() {
        clickContextActionEdit();
    }


    @Override
    public String getTableId() {
        return TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_NEW_SERVER_GROUP_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_SERVER_GROUP_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return null;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }
}
