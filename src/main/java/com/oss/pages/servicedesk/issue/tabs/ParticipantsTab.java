package com.oss.pages.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.issue.wizard.ParticipantsPromptPage;

import io.qameta.allure.Step;

public class ParticipantsTab extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(ParticipantsTab.class);

    private static final String ADD_PARTICIPANT_ID = "_createParticipant";
    private static final String PARTICIPANTS_TABLE_ID = "_participantsTableApp";
    private static final String FIRST_NAME_ATTRIBUTE_ID = "First Name";
    private static final String SURNAME_ATTRIBUTE_ID = "Surname";
    private static final String ROLE_ATTRIBUTE_ID = "Role";

    public ParticipantsTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Click add participant")
    public ParticipantsPromptPage clickAddParticipant() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextActionFromButtonContainer(ADD_PARTICIPANT_ID);
        log.info("Add Participant prompt is opened");
        return new ParticipantsPromptPage(driver, wait);
    }

    public boolean isAddParticipantButtonActive() {
        return isActionPresent(ADD_PARTICIPANT_ID);
    }

    @Step("Check participant first name")
    public String checkParticipantFirstName(int participantIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check participant first name");
        return getParticipantTable().getCellValue(participantIndex, FIRST_NAME_ATTRIBUTE_ID);
    }

    @Step("Check participant surname")
    public String checkParticipantSurname(int participantIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check participant surname");
        return getParticipantTable().getCellValue(participantIndex, SURNAME_ATTRIBUTE_ID);
    }

    @Step("Check participant role")
    public String checkParticipantRole(int participantIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check participant role");
        return getParticipantTable().getCellValue(participantIndex, ROLE_ATTRIBUTE_ID);
    }

    private OldTable getParticipantTable() {
        return OldTable.createById(driver, wait, PARTICIPANTS_TABLE_ID);
    }
}
