package com.oss.pages.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.servicedesk.BaseSDPage;

public class DescriptionTab extends BaseSDPage {

    private static final String DESCRIPTION_COMPONENT_ID = "_descriptionWidget";

    public DescriptionTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void addDescription(String descriptionNote) {
        getDescriptionComponent().setSingleStringValue(descriptionNote);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Adding description: {}", descriptionNote);
    }

    public String getDescriptionMessage() {
        return getDescriptionComponent().getStringValue();
    }

    private Input getDescriptionComponent() {
        return ComponentFactory.create(DESCRIPTION_COMPONENT_ID, Input.ComponentType.COMMENT_TEXT_FIELD, driver, wait);
    }
}
