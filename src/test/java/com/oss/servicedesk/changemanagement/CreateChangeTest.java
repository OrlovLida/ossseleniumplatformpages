package com.oss.servicedesk.changemanagement;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.changemanagement.ChangeDashboardPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

public class CreateChangeTest extends BaseTestCase {

    private ChangeDashboardPage changeDashboardPage;
    private SDWizardPage sdWizardPage;
    private String changeID;

    private static final String RISK_ASSESSMENT_ID = "TT_WIZARD_INPUT_RISK_ASSESSMENT_LABEL";
    private static final String RISK = "LOW";
    private static final String REQUESTER_ID = "TT_WIZARD_INPUT_REQUESTER_LABEL";
    private static final String ASSIGNEE_ID = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private static final String INCIDENT_DESCRIPTIOPN_ID = "TT_WIZARD_INPUT_INCIDENT_DESCRIPTION_LABEL";
    private static final String INCIDENT_DESCRIPTION_TXT = "Selenium Incident Description";

    @BeforeMethod
    public void goToTicketDashboardPage() {
        changeDashboardPage = new ChangeDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Parameters({"userName"})
    @Test(priority = 1, testName = "Create Change", description = " Create Change")
    @Description("Create Change")
    public void createChange(
            @Optional("sd_seleniumtest") String userName
    ) {
        sdWizardPage = changeDashboardPage.openCreateChangeWizard();
        sdWizardPage.insertValueToTextComponent(RISK, RISK_ASSESSMENT_ID);
        sdWizardPage.insertValueToSearchComponent(userName, REQUESTER_ID);
        sdWizardPage.insertValueToSearchComponent(userName, ASSIGNEE_ID);
        sdWizardPage.insertValueToTextAreaComponent(INCIDENT_DESCRIPTION_TXT, INCIDENT_DESCRIPTIOPN_ID);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();
        changeID = changeDashboardPage.getIdFromMessage();
        Assert.assertEquals(changeDashboardPage.getRequesterFromNthRow(0), userName);
        Assert.assertEquals(changeDashboardPage.getRowForIssueWithID(changeID), 0);
    }
}
