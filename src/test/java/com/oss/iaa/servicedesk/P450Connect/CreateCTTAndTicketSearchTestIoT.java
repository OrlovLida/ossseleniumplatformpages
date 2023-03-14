package com.oss.iaa.servicedesk.P450Connect;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.comarch.oss.web.pages.NotificationWrapperPage;
import com.jayway.restassured.RestAssured;
import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.ClosedTicketsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.MyGroupTicketsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.MyTicketsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketOverviewTab;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketSearchPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;
import com.oss.pages.platform.LoginPage;
import com.oss.serviceClient.Environment;
import com.oss.serviceClient.EnvironmentRequestClient;

import io.qameta.allure.Description;

import static com.oss.configuration.Configuration.CONFIGURATION;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DOWNLOAD_FILE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.EXPORT_WIZARD_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.ID_ATTRIBUTE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;

public class CreateCTTAndTicketSearchTestIoT extends BaseTestCase {

    private static final String PRIORITY = "P1 - Critical Priority";
    private static final String SEVERITY = "Warning";
    private static final String SEVERITY_ID = "TT_WIZARD_INPUT_SEVERITY_LABEL";
    private static final String PRIORITY_ID = "TT_WIZARD_INPUT_PRIORITY_LABEL";
    private static final String FLOW_TYPE = "CTT";
    private static final String INCIDENT_DESCRIPTION = "Selenium test ticket";
    private static final String SEVERITY_ATTRIBUTE_NAME = "Severity";
    private static final String DOMAIN_ATTRIBUTE_NAME = "Domain";
    private static final String STATUS_INPROGRESS = "In Progress";
    private static final String STATUS_RESOLVED = "Resolved";
    private static final String STATUS_CLOSED = "Closed";
    private static final String TT_DOWNLOAD_FILE = "TroubleTicket*.xlsx";
    private static final String TT_WIZARD_ASSIGNEE = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private static final String ASSIGNEE = "sd_seleniumtest";
    private static final String DOMAIN = "Nokia";
    private static final String DOMAIN_ID = "COMMON_WIZARD_DOMAINS_FIELD_LABEL";
    private static final String NUMBER_SIM = "000";
    private static final String REASON_PRIORITY = "selenium_test";
    private static final String SERVICE_TYPE = "VoiceService";
    private static final String SERVICE_TYPE_ID = "GenericDictionaryField_Service_type_1";
    private static final String COMMUNICATION_CHANNEL = "Email";
    private static final String COMMUNICATION_CHANNEL_ID = "GenericDictionaryField_Communication_channel_1";
    private static final String NUMBER_OF_SIM_ID = "GenericWizardUserAttributeField_Number of SIMs_NumberOfSIMs";
    private static final String REASON_ID = "GenericWizardUserAttributeField_Reason of the ticket priority_TicketPriorityReason";

    private TicketDashboardPage ticketDashboardPage;
    private MyGroupTicketsPage myGroupTicketsPage;
    private MyTicketsPage myTicketsPage;
    private TicketSearchPage ticketSearchPage;
    private NotificationWrapperPage notificationWrapperPage;
    private IssueDetailsPage issueDetailsPage;
    private TicketOverviewTab ticketOverviewTab;
    private ClosedTicketsPage closedTicketsPage;
    private String ticketID;
    private SDWizardPage sdWizardPage;

    public WebDriverWait webDriverWait;

    @BeforeClass
    public void openBrowser() {
        RestAssured.config = prepareRestAssureConfig();
        Environment environment = Environment.createEnvironmentFromConfiguration();
        environmentRequestClient = new EnvironmentRequestClient(environment);
        if (CONFIGURATION.getDriver().equals("chrome")) {
            startChromeDriver();
        } else {
            startFirefoxDriver();
        }
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(50));
        driver.navigate().to("https://vendor.test.iot-450c.swan.comarch");
        driver.findElement(By.cssSelector("[id='username']")).sendKeys("kinga.balcar-mazur@comarch.com");
        driver.findElement(By.cssSelector("[id='password']")).sendKeys("Dziczyzna_2424");
        DelayUtils.sleep(1000);
        driver.findElement(By.cssSelector("[class='mdc-button__ripple']")).click();
        DelayUtils.sleep(2000);
        driver.findElement(By.cssSelector("[name='accept']")).click();
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
        addCookies(driver);
    }

    @BeforeMethod
    public void goToTicketDashboardPage() {
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Parameters({"MOIdentifier", "Domain", "Assignee"})
    @Test(priority = 1, testName = "Create CTT Ticket", description = "Create CTT Ticket")
    @Description("Create CTT Ticket")
    public void createIncidentTicket(
            @Optional("testBuilding-MSAN-1/ChassisID/TEST_LM_SLOT_20220207/Horizontal Card MDF") String MOIdentifier,
            @Optional("Nokia") String Domain,
            @Optional("Kinga Balcar-Mazur") String Assignee
    ) {
        sdWizardPage = ticketDashboardPage.openCreateTicketWizard(FLOW_TYPE);
        sdWizardPage.getMoStep().enterTextIntoMOIdentifierField(MOIdentifier);
        sdWizardPage.getMoStep().selectObjectInMOTable(MOIdentifier);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.insertValueToComponent(SEVERITY, SEVERITY_ID);
        sdWizardPage.insertValueToComponent(PRIORITY, PRIORITY_ID);
        sdWizardPage.insertValueToComponent(NUMBER_SIM, NUMBER_OF_SIM_ID);
        sdWizardPage.insertValueToComponent(REASON_PRIORITY, REASON_ID);
        sdWizardPage.insertValueToComponent(SERVICE_TYPE, SERVICE_TYPE_ID);
        sdWizardPage.insertValueToComponent(COMMUNICATION_CHANNEL, COMMUNICATION_CHANNEL_ID);
        sdWizardPage.enterDescription(INCIDENT_DESCRIPTION);
        sdWizardPage.insertValueToComponent(Assignee, TT_WIZARD_ASSIGNEE);
//        sdWizardPage.insertValueToComponent(DOMAIN, DOMAIN_ID); //TODO: usunąć zakomentowanie gdy domeny bedą już gotowe w tym projekcie
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();
        ticketID = ticketDashboardPage.getIdFromMessage();
        Assert.assertTrue(ticketDashboardPage.getAttributeFromTable(0, SEVERITY_ATTRIBUTE_NAME).contains(SEVERITY));
        Assert.assertEquals(ticketDashboardPage.getRowForIssueWithID(ticketID), 0);
    }

    @Test(priority = 2, testName = "Export from Ticket Dashboard", description = "Export from Ticket Dashboard")
    @Description("Export from Ticket Dashboard")
    public void exportFromTicketDashboard() {
        ticketDashboardPage.exportFromDashboard(TT_DOWNLOAD_FILE);
        notificationWrapperPage = ticketDashboardPage.openNotificationPanel();

        Assert.assertTrue(ticketDashboardPage.checkIfFileIsNotEmpty(TT_DOWNLOAD_FILE));
    }

    @Test(priority = 3, testName = "Check My Tickets", description = "Check My Tickets")
    @Description("Check My Tickets")
    public void checkMyTickets() {
        myTicketsPage = new MyTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        myTicketsPage.filterBy(ID_ATTRIBUTE, ticketID);
        Assert.assertFalse(myTicketsPage.isIssueTableEmpty());
        Assert.assertEquals(myTicketsPage.getIdForNthTicketInTable(0), ticketID);
    }

    @Test(priority = 4, testName = "Check Ticket Search", description = "Check Ticket Search")
    @Description("Check Ticket Search")
    public void checkTicketSearch() {
        ticketSearchPage = new TicketSearchPage(driver, webDriverWait).openView(driver, BASIC_URL);
        ticketSearchPage.filterBy(ID_ATTRIBUTE, ticketID);
        Assert.assertFalse(ticketSearchPage.isIssueTableEmpty());
        Assert.assertEquals(ticketSearchPage.getIdForNthTicketInTable(0), ticketID);
    }

    @Test(priority = 5, testName = "Check My Group Tickets", description = "Check My Group Tickets")
    @Description("Check My Group Tickets")
    public void checkMyGroupTickets() {
        myGroupTicketsPage = new MyGroupTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        myGroupTicketsPage.filterBy(ID_ATTRIBUTE, ticketID);
        Assert.assertFalse(myGroupTicketsPage.isIssueTableEmpty());
        Assert.assertEquals(myGroupTicketsPage.getIdForNthTicketInTable(0), ticketID);
    }

    @Test(priority = 6, testName = "Export from My Group Tickets", description = "Export from My Group Tickets")
    @Description("Export from My Group Tickets")
    public void exportFromMyGroupTickets() {
        myGroupTicketsPage = new MyGroupTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        try {
            myGroupTicketsPage.exportFromSearchViewTable(EXPORT_WIZARD_ID);
        } catch (RuntimeException e) {
            Assert.fail(e.getMessage());
        }
        notificationWrapperPage = new NotificationWrapperPage(driver);

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
        Assert.assertTrue(myGroupTicketsPage.checkIfFileIsNotEmpty(DOWNLOAD_FILE));
    }

    @Test(priority = 7, testName = "Close Ticket", description = "Open ticket and go through all necessary steps to close it")
    @Description("Open ticket and go through all necessary steps to close it")
    public void closeTicket(
    ) {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.allowEditingTicket();
        issueDetailsPage.skipAllActionsOnCheckList();
        ticketOverviewTab.changeIssueStatus(STATUS_INPROGRESS);
        issueDetailsPage.skipAllActionsOnCheckList();
        ticketOverviewTab.changeIssueStatus(STATUS_RESOLVED);
        ticketOverviewTab.clickRemoveRemainder();
        ticketOverviewTab.changeIssueStatus(STATUS_CLOSED);

        Assert.assertEquals(ticketOverviewTab.checkTicketStatus(), STATUS_CLOSED);
    }

    @Test(priority = 8, testName = "Check Closed Tickets View", description = "Refresh, search and check if ticket is shown in the closed tickets table")
    @Description("Refresh, search and check if ticket is shown in the closed tickets table")
    public void checkClosedTicketView() {
        closedTicketsPage = new ClosedTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
        DelayUtils.sleep(1000); //TODO: remove this when environment will be stable
        closedTicketsPage.clickRefresh();
        DelayUtils.sleep(1000); //TODO: remove this when environment will be stable
        closedTicketsPage.filterBy(ID_ATTRIBUTE, ticketID);
        Assert.assertFalse(closedTicketsPage.isIssueTableEmpty());
        Assert.assertEquals(closedTicketsPage.getIdForNthTicketInTable(0), ticketID);
    }
}