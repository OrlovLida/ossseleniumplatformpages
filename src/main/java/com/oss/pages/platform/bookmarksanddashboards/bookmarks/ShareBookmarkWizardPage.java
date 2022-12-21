package com.oss.pages.platform.bookmarksanddashboards.bookmarks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

public class ShareBookmarkWizardPage extends BasePage {

    private static final String VIEWER_RECIPIENT_INPUT_ID = "viewer-recipient-type-combobox-component-id";
    private static final String VIEWER_NAME_INPUT_ID = "viewer-name-object-search-field-component-id";
    private static final String SHARE_WIZARD_ID = "share-wizard-view-id_prompt-card";
    private static final String ACCEPT_BUTTON_ID = "wizard-submit-button-share-widget-id";
    private static final String ADD_VIEWER_BUTTON_ID = "viewer-add-button-component-id";

    public ShareBookmarkWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void setRecipientType(String recipient) {
        getWizard().setComponentValue(VIEWER_RECIPIENT_INPUT_ID, recipient);
    }

    public void setName(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().setComponentValue(VIEWER_NAME_INPUT_ID, name);
    }

    public void clickAddViewer() {
        getWizard().clickButtonById(ADD_VIEWER_BUTTON_ID);
    }

    public void clickAccept() {
        getWizard().clickButtonById(ACCEPT_BUTTON_ID);

    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, SHARE_WIZARD_ID);
    }

}