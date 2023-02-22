package com.oss.pages.dms;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.CommentTextField;
import com.oss.framework.components.layout.Card;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class GlobalAttachmentManagerPage extends BasePage {

    private static final String HOME_DIRECTORY_INPUT = "OLD_TEXT_FIELD_APP-globalAttachmentManager_textField";
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

    @Step("Check if object with given {name} is present")
    public boolean isNamePresent(String name) {
        return getList().isRowDisplayed(NAME_COLUMN, name);
    }

    @Step("Check if object with given {owner} is present")
    public boolean isOwnerPresent(String owner) {
        return getList().isRowDisplayed(OWNER_COLUMN, owner);
    }

    @Step("Check if object with given {tags} is present")
    public boolean areTagsPresent(String tags) {
        return getList().isRowDisplayed(TAGS_COLUMN, tags);
    }

    @Step("Get list")
    private CommonList getList() {
        return CommonList.create(driver, wait, ATTACHMENTS_LIST_ID);
    }

    @Step("Check if list is empty")
    public boolean isListEmpty() {
        return getList().hasNoData();
    }

    @Step("Select row")
    public void selectRow(String name) {
        getList().getRow(NAME_COLUMN, name).selectRow();
    }

    @Step("Open directory with name = {directoryName}")
    public void clickDirectoryLinkInPath(String directoryName) {
        getList().getRow(NAME_COLUMN, directoryName).clickLink(directoryName);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Click link on List")
    public void clickLinkInList(String link) {
        CommentTextField input = CommentTextField.create(driver, wait, HOME_DIRECTORY_INPUT);
        input.clickLink(link);
    }
}