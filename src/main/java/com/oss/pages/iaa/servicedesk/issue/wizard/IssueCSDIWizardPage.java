package com.oss.pages.iaa.servicedesk.issue.wizard;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.qameta.allure.Step;

public class IssueCSDIWizardPage extends SDWizardPage {

    private static final Logger log = LoggerFactory.getLogger(IssueCSDIWizardPage.class);

    private static final String ISSUE_TITLE_ID = "ISSUE_TITLE";
    private static final String SEVERITY_ID = "TT_WIZARD_INPUT_SEVERITY_LABEL";
    private static final String STEPS_ID = "StepsToReproduce";
    private static final String EXPECTED_BEHAVIOR_ID = "ExpectedBehavior";
    private static final String CONTACT_PERSON_ID = "ContactPerson";
    private static final String IMPACT_ID = "Impact";
    private static final String IMPACT_EXPLANATION_ID = "ExplanationOfImpact";
    private static final String URGENCY_ID = "Urgency";
    private static final String URGENCY_EXPLANATION_ID = "ExplanationOfUrgency";
    private static final String ENVIRONMENT_ID = "EnvironmentType";
    private static final String COMPONENT_ID = "Component";


    public IssueCSDIWizardPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    @Step("Set issue title")
    public void setIssueTitle(String issueTitle) {
        insertValueToComponent(issueTitle, ISSUE_TITLE_ID);
        log.info("Set issue title");
    }

    @Step("Set severity")
    public void setSeverity(String severity) {
        insertValueToComponent(severity, SEVERITY_ID);
        log.info("Set severity");
    }

    @Step("Set steps to reproduce")
    public void setStepsToReproduce(String steps) {
        insertValueToComponent(steps, STEPS_ID);
        log.info("Set steps to reproduce");
    }

    @Step("Set expected behavior")
    public void setExpectedBehavior(String expectedBehavior) {
        insertValueToComponent(expectedBehavior, EXPECTED_BEHAVIOR_ID);
        log.info("Set expected behavior");
    }

    @Step("Set contact person")
    public void setContactPerson(String contactPerson) {
        insertValueToComponent(contactPerson, CONTACT_PERSON_ID);
        log.info("Set contact person");
    }

    @Step("Set impact")
    public void setImpact(String impact) {
        insertValueToComponent(impact, IMPACT_ID);
        log.info("Set impact");
    }

    @Step("Set impact explanation")
    public void setImpactExplanation(String impactExplanation) {
        insertValueToComponent(impactExplanation, IMPACT_EXPLANATION_ID);
        log.info("Set impact explanation");
    }

    @Step("Set urgency")
    public void setUrgency(String urgency) {
        insertValueToComponent(urgency, URGENCY_ID);
        log.info("Set urgency");
    }

    @Step("Set urgency explanation")
    public void setUrgencyExplanation(String urgencyExplanation) {
        insertValueToComponent(urgencyExplanation, URGENCY_EXPLANATION_ID);
        log.info("Set urgency explanation");
    }

    @Step("Set environment type")
    public void setEnvironmentType(String environmentType) {
        insertValueToComponent(environmentType, ENVIRONMENT_ID);
        log.info("Set environment type");
    }

    @Step("Set component")
    public void setComponent(String component) {
        insertValueToComponent(component, COMPONENT_ID);
        log.info("Set component");
    }
}