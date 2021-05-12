/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.bpm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.EditMilestoneWizardPage;
import com.oss.pages.bpm.Milestone;
import com.oss.pages.bpm.MilestoneViewPage;
import com.oss.utils.TestListener;

/**
 * @author Gabriela Kasza
 */
@Listeners({TestListener.class})
public class EditMilestoneTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(EditMilestoneTest.class);

    private String description = "Milestone Update " + (Math.random() * 1001);
    private String leadTime =  String.valueOf((int) (Math.random() * 101));
    @BeforeClass
    public void createMilestone(){

    }
    @Test
    public void editDescription(){
        //given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        //when
        milestoneViewPage.selectMilestone("Milestone 2.915", "DCP-2038");
        String modifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_description = Milestone.builder().setDescription(description).build();
        //then
        editWizard.editMilestone(updated_description);
        String newDescriptionValue = milestoneViewPage.getValuePropertyPanel("Description", 0);
        String newModifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        Assert.assertEquals(description,newDescriptionValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);
    }

    @Test
    public void editLeadTime(){
        //given
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        //when
        milestoneViewPage.selectMilestone("Milestone 2.915", "DCP-2038");
        String modifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        EditMilestoneWizardPage editWizard = new EditMilestoneWizardPage(driver);
        Milestone updated_leadTime = Milestone.builder().setLeadTime(leadTime).build();
        //then
        editWizard.editMilestone(updated_leadTime);
        String newLeadTimeValue = milestoneViewPage.getValuePropertyPanel("leadTime", 0);
        String newModifyDate = milestoneViewPage.getValuePropertyPanel("modifyDate", 0);
        Assert.assertEquals(leadTime,newLeadTimeValue);
        Assert.assertNotEquals(newModifyDate, modifyDate);
        log.info("test");

    }

    @Test
    public void editRelatedTask(){
        MilestoneViewPage milestoneViewPage = MilestoneViewPage.goToMilestoneViewPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        milestoneViewPage.selectMilestone("Milestone 2.915", "DCP-2038");
        String relatedTask = milestoneViewPage.getValuePropertyPanel("relatedTaskName", 0);
        Assert.assertEquals(relatedTask,"—");
    }

    @Test
    public void editDueDateEarlierDate(){

    }

    @Test void editDueDateLaterDate(){

    }

    @Test void checkIfNameIsEditable(){

    }

    @Test
    public void checkIfModifyUserIsUpdated(){

    }
}
