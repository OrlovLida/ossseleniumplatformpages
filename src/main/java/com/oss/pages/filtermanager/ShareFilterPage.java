package com.oss.pages.filtermanager;

import org.openqa.selenium.WebDriver;

import com.google.common.collect.Lists;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.Modal;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.commonhierarchy.CommonHierarchyApp;

import io.qameta.allure.Step;

public class ShareFilterPage extends FilterManagerPage {

    private static final String COMMON_HIERARCHY_APP_USERS_LIST = "CommonHierarchyApp-usersList";
    private static final String SEARCH_ID = "extendedSearchComponent_userSearch";
    private static final String ALL_USERS = "all users";

    public ShareFilterPage(WebDriver driver) {
        super(driver);
    }

    @Step("Type user name in search")
    public ShareFilterPage typeUserNameInSearch(String userName) {
        Input searchField = ComponentFactory.create(SEARCH_ID, driver, wait);
        searchField.setSingleStringValue(userName);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Close Share View")
    public void closeShareView() {
        Modal.create(driver, wait).clickClose();
    }

    @Step("Share for user")
    public ShareFilterPage shareForUser(String userName, String permission) {
        CommonHierarchyApp shareTree = CommonHierarchyApp.create(driver, wait, COMMON_HIERARCHY_APP_USERS_LIST);
        shareTree.callAction(Lists.newArrayList(userName), permission, ALL_USERS);
        return this;
    }

}
