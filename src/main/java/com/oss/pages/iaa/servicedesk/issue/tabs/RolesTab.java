package com.oss.pages.iaa.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.servicedesk.BaseSDPage;

import io.qameta.allure.Step;

public class RolesTab extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(RolesTab.class);

    private static final String MANAGER_SEARCH_BOX_ID = "rolesManager";
    private static final String APPROVER_SEARCH_BOX_ID = "approver";
    private static final String IMPLEMENTER_SEARCH_BOX_ID = "implementer";
    private static final String COORDINATOR_SEARCH_BOX_ID = "coordinator";

    public RolesTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Fill {user} in search box with id {componentId}")
    public void fillUser(String componentId, String user) {
        ComponentFactory.create(componentId, driver, wait)
                .setSingleStringValue(user);
        DelayUtils.waitForPageToLoad(driver, wait);

        log.info("Filling search box with id: {} with user: {}", componentId, user);
    }

    @Step("Get value from search box with id: {componentId}")
    public String getValueFromSearchBox(String componentId) {
        return ComponentFactory.create(componentId, driver, wait).getStringValue();
    }

    public String getManagerSearchBoxId() {
        return MANAGER_SEARCH_BOX_ID;
    }

    public String getApproverSearchBoxId() {
        return APPROVER_SEARCH_BOX_ID;
    }

    public String getImplementerSearchBoxId() {
        return IMPLEMENTER_SEARCH_BOX_ID;
    }

    public String getCoordinatorSearchBoxId() {
        return COORDINATOR_SEARCH_BOX_ID;
    }
}
