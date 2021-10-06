package com.oss.pages.servicedesk.ticket;


import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.BasePage;
import com.oss.pages.servicedesk.ticket.wizard.WizardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketDashboardPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(TicketDashboardPage.class);

    private static final String CREATE_TICKET_BUTTON_ID = "TT_WIZARD_INPUT_CREATE_TICKET";
    public static final String ID_ATTRIBUTE = "id";
    private static final String TABLE_ID = "_TroubleTickets";

    public TicketDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open ticket dashboard View")
    public static TicketDashboardPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 150);

        String pageUrl = String.format("%s/#/dashboard/predefined/id/_TroubleTickets", basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep(5000);
        log.info("Opened page: {}", pageUrl);

        return new TicketDashboardPage(driver, wait);
    }

    @Step("I open create ticket wizard for flow {flowType}")
    public WizardPage openCreateTicketWizard(WebDriver driver, String flowType) {
        DelayUtils.sleep(5000);
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createBySelectorAndId(driver, "button", CREATE_TICKET_BUTTON_ID).click();
        DropdownList.create(driver, wait).selectOptionWithId(flowType);
        log.info("Create ticket wizard for {} is opened", flowType);
        return new WizardPage(driver);
    }

    @Step("I check severity table")
    public TableWidget getSeverityTable(WebDriver driver, String tableId, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Create Table widget");
        return TableWidget.createById(driver, tableId, wait);
    }

    public String getIdForNthTicketInTable(int n) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getAttributeFromTable(n, ID_ATTRIBUTE);
    }

    private String getAttributeFromTable(int index, String attributeName) {
        String attributeValue = getSeverityTable(driver, TABLE_ID, wait).getCellValue(index, attributeName);
        log.info("Got value for {} attribute: {}", attributeName, attributeValue);
        return attributeValue;
    }
}

