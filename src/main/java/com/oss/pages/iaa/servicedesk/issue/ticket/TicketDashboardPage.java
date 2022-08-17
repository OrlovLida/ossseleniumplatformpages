package com.oss.pages.iaa.servicedesk.issue.ticket;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.pages.iaa.servicedesk.issue.BaseDashboardPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.COMMON_WIZARD_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TICKET_DASHBOARD;

public class TicketDashboardPage extends BaseDashboardPage {

    private static final String CREATE_TICKET_BUTTON_ID = "TT_WIZARD_INPUT_CREATE_TICKET";
    private static final String TROUBLE_TICKETS_TABLE_ID = "_CommonTable_Dashboard_TroubleTickets";
    private static final String ASSIGNEE_ATTRIBUTE = "Assignee";

    public TicketDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open ticket dashboard View")
    public TicketDashboardPage goToPage(WebDriver driver, String basicURL) {
        openPage(driver, getDashboardURL(basicURL, TICKET_DASHBOARD));

        return new TicketDashboardPage(driver, wait);
    }

    @Step("I open create ticket wizard for flow {flowType}")
    public SDWizardPage openCreateTicketWizard(String flowType) {
        return SDWizardPage.openCreateWizard(driver, wait, flowType, CREATE_TICKET_BUTTON_ID, COMMON_WIZARD_ID);
    }

    public String getAssigneeForNthTicketInTable(int n) {
        return getAttributeFromTable(n, ASSIGNEE_ATTRIBUTE);
    }

    @Override
    protected String getTableID() {
        return TROUBLE_TICKETS_TABLE_ID;
    }
}

