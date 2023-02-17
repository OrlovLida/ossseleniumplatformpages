package com.oss.pages.dms;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.layout.Card;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class GlobalAttachmentManagerPage extends BasePage {

    private static final String GLOBAL_ATTACHMENT_MANAGER_VIEW_ID = "globalAttachmentManager_templateWindow";
    public static final String CREATE_DIRECTORY_ACTION_ID = "dms_createDirectoryAct";
    public static final String EDIT_DIRECTORY_ACTION_ID = "editFolderAct";
    public static final String ATTACH_FILE_ACTION_ID = "dms_addAttachmentAct";
    public static final String DELETE_DIRECTORY_ACTION_ID = "deleteFolderAct";
    public static final String DELETE_FILE_ACTION_ID = "deleteAttachmentAct";
    private static final String ATTACHMENTS_LIST_ID = "globalAttachmentManager_commonList";
    private static final String NAME_COLUMN = "Name";
    private static final String OWNER_COLUMN = "Owner";
    private static final String TAGS_COLUMN = "Tags";

    public GlobalAttachmentManagerPage(WebDriver driver) {
        super(driver);
    }

    @Step("Use context action")
    public void callAction(String groupId, String actionId) {
        Card card = Card.createCard(driver, wait, GLOBAL_ATTACHMENT_MANAGER_VIEW_ID);
        card.callActionById(groupId, actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Get name")
    public String getName(String directoryName) {
        return getList().getRow(NAME_COLUMN, directoryName).getValue(NAME_COLUMN);
    }

    @Step("Get owner")
    public String getOwner(String owner) {
        return getList().getRow(OWNER_COLUMN, owner).getValue(OWNER_COLUMN);
    }

    @Step("Get tags")
    public String getTags(String tags) {
        return getList().getRow(TAGS_COLUMN, tags).getValue(TAGS_COLUMN);
    }

    @Step("Get list")
    private CommonList getList() {
        return CommonList.create(driver, wait, ATTACHMENTS_LIST_ID);
    }

    @Step("Check if list is empty")
    public boolean isListEmpty() {
        return getList().hasNoData();
    }

    @Step("Select current row")
    public void selectRow(String name) {
        getList().getRow(NAME_COLUMN, name).selectRow();
    }

//    TODO: Odkomentowac po rozwiazaniu: OSSSELEN-659
//    @Step("Open directory with name = {directoryName}")
//    public void openDirectory(String directoryName) {
//        getList().getRow(NAME_COLUMN, directoryName).getCell(NAME_COLUMN).click();
//    }
}
