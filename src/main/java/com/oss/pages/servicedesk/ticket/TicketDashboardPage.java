package com.oss.pages.servicedesk.ticket;


import static com.oss.pages.servicedesk.URLConstants.PREDEFINED_DASHBOARD_URL_PATTERN;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.ticket.wizard.WizardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TicketDashboardPage extends BaseSDPage {

    private static final String TICKET_TABLE_ID = "_CommonTable_Dashboard_TroubleTickets";
    private static final String TICKET_DASHBOARD_ID = "_TroubleTickets";
    private static final String CREATE_TICKET_BUTTON_ID = "TT_WIZARD_INPUT_CREATE_TICKET";

    public TicketDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open ticket dashboard page")
    public static TicketDashboardPage openTicketDashboard(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        BaseSDPage.openPage(driver, String.format(PREDEFINED_DASHBOARD_URL_PATTERN, basicURL, TICKET_DASHBOARD_ID), wait);
        return new TicketDashboardPage(driver, wait);
    }

    @Step("I open create ticket wizard for flow {flowType}")
    public WizardPage openCreateTicketWizard(WebDriver driver, String flowType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTicketTable(driver);
        Button.createBySelectorAndId(driver, "button", CREATE_TICKET_BUTTON_ID).click();
        DropdownList.create(driver, wait).selectOptionWithId(flowType);
        return new WizardPage(driver, wait);
    }

    private TableWidget getTicketTable(WebDriver driver) {
        return TableWidget.createById(driver, TICKET_TABLE_ID, wait);
    }
}

