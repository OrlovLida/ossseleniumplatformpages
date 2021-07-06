package com.oss.pages.servicedesk.ticket;


import static com.oss.pages.servicedesk.URLConstants.PREDEFINED_DASHBOARD_URL_PATTERN;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.ticket.wizard.WizardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TicketDashboardPage extends BaseSDPage {

    private static final String TICKET_DASHBOARD_ID = "_TroubleTickets";
    private static final String CREATE_TICKET_BUTTON_ID = "TT_WIZARD_INPUT_CREATE_TICKET";

    public TicketDashboardPage(WebDriver driver) {
        super(driver);
    }

    @Step("I open ticket dashboard page")
    public static TicketDashboardPage openTicketDashboard(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        openDashboardPage(driver, basicURL, wait, TICKET_DASHBOARD_ID);
        return new TicketDashboardPage(driver);
    }

    @Step("I open create ticket wizard for flow {flowType}")
    public WizardPage openCreateTicketWizard(WebDriver driver, String flowType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createBySelectorAndId(driver, "button", CREATE_TICKET_BUTTON_ID).click();
        DropdownList.create(driver, wait).selectOptionWithId(flowType);
        return new WizardPage(driver);
    }
}

