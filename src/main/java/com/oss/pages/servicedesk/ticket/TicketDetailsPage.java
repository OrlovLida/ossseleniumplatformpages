package com.oss.pages.servicedesk.ticket;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.pages.BasePage;
import com.oss.pages.servicedesk.ticket.wizard.WizardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class TicketDetailsPage extends BasePage {

    public static final String DETAILS_PAGE_URL_PATTERN = "%s/#/view/service-desk/trouble-ticket/details/%s";
    public static final String EDIT_DETAILS_LABEL = "Edit details";

    public TicketDetailsPage(WebDriver driver) {
        super(driver);
    }

    @Step("I open edit ticket wizard")
    public WizardPage openEditTicketWizard(WebDriver driver) {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextAction(EDIT_DETAILS_LABEL);
        return new WizardPage(driver, wait);
    }

    private void clickContextAction(String contextActionLabel) {
        TabWindowWidget.create(driver, wait).callActionByLabel(contextActionLabel);
    }
}
