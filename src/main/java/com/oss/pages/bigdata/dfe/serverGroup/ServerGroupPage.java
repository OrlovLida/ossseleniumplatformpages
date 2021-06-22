package com.oss.pages.bigdata.dfe.serverGroup;

import com.oss.pages.bigdata.dfe.BaseDfePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ServerGroupPage extends BaseDfePage {

    private static final String TABLE_ID = "server-groupAppId";
    private final String SEARCH_INPUT_ID = "server-groupSearchAppId";
    private final String ADD_NEW_SERVER_GROUP_LABEL = "Add New Server Group";
    private final String EDIT_SERVER_GROUP_LABEL = "Edit Server Group";
    private final String DELETE_SERVER_GROUP_LABEL = "Delete Server Group";
    private final String SERVER_GROUP_NAME_COLUMN_LABEL = "Name";
    private final static String SERVERS_TAB = "Servers";
    private final String CONFIRM_DELETE_LABEL = "Delete";


    public ServerGroupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
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

    @Step("I check if server group: {name} exists into the table")
    public Boolean serverGroupExistIntoTable(String name) {
        return feedExistIntoTable(name, SERVER_GROUP_NAME_COLUMN_LABEL);
    }

    @Step("I select found Server Group")
    public void selectFoundServerGroup() {
        getTable(driver, wait).selectRow(0);
    }

    @Step("I click edit Server Group")
    public void clickEditServerGroup() {
        clickContextActionEdit();
    }

    @Step("I click delete Server Group")
    public void clickDeleteServerGroup() {
        clickContextActionDelete();
    }

    @Step("I click Servers Tab")
    public void selectServersTab() {
        selectTab(SERVERS_TAB);
    }

    @Step("I confirm the removal")
    public void clickConfirmDelete() {
        confirmDelete(CONFIRM_DELETE_LABEL);
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
        return DELETE_SERVER_GROUP_LABEL;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }
}
