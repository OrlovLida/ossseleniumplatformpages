/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.bpm;

import java.io.Console;
import java.time.LocalDate;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.Milestone;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

/**
 * @author Gabriela Kasza
 */
@Listeners({TestListener.class})
public class CreateMilestoneWithProcessTest extends BaseTestCase {
    
    private String processName = "Selenium Test.Milestone-" + (int) (Math.random() * 1001);
    private String MilestoneName1 ="Milestone 1."+(int) (Math.random() * 1001);
    private String MilestoneName2 ="Milestone 2."+(int) (Math.random() * 1001);
    private ProcessInstancesPage processInstancesPage;
    
    @BeforeClass
    public void openProcessInstancesPage() {
        processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.sleep(3000);
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        processInstancesPage.changeUser("bpm_webselenium", "bpmweb");
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        
    }
    @Description("For Basic  Milestone User ")
    @Test(priority = 1)
    public void createProcessWithMilestones() {
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processWizardPage.definedMilestoneInProcess(processName, 10L, "GK Milestone");
        Milestone milestone1 = Milestone.builder()
                .setLeadTime("10")
                .setDescription("Milestone 1 - Selenium Test")
                .setIsManualCompletion("true")
                .setIsActive("true")
                .setName(MilestoneName1).build();

        Milestone milestone2 = Milestone.builder()
                .setDueDate(LocalDate.now().plusDays(5).toString())
                .setName(MilestoneName2)
                .setRelatedTask("Task 1")
                .build();

        processWizardPage.addMilestoneRow(milestone1);
        processWizardPage.addMilestoneRow(milestone2);


        processWizardPage.clickAcceptButton();
        System.out.println(milestone1.getName().get() + milestone2.getName().get());



    }
    @Test
    public void updatePredefinedMilestone(){
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processWizardPage.definedMilestoneInProcess(processName, 10L, "GK Milestone");
        Milestone milestone1 = Milestone.builder()
                .setDueDate(LocalDate.now().toString())
                .setLeadTime("10")
                .setDescription("Update 1")
                .setIsManualCompletion("false")
                .build();
        processWizardPage.editPredefinedMilestone(milestone1,1);
        processWizardPage.clickAcceptButton();

    }

    @Test
    public void addMilestoneForDCP(){
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processWizardPage.definedMilestoneInProcess(processName, 10L, "Data Correction Process");
        Milestone milestone1 = Milestone.builder()
                .setLeadTime("10")
                .setDescription("Milestone 1 - Selenium Test")
                .setIsManualCompletion("true")
                .setIsActive("true")
                .setName(MilestoneName1).build();

        Milestone milestone2 = Milestone.builder()
                .setDueDate(LocalDate.now().plusDays(5).toString())
                .setName(MilestoneName2)
                .setRelatedTask("Correct data")
                .build();

        processWizardPage.addMilestoneRow(milestone1);
        processWizardPage.addMilestoneRow(milestone2);
        String code = processWizardPage.clickAcceptButton();

        processInstancesPage.selectMilestoneTab(code);
        String statusMilestone1 = processInstancesPage.getMilestoneValue( milestone1.getName().get(), "Status");
        String statusMilestone2 = processInstancesPage.getMilestoneValue( milestone2.getName().get(), "Status");
        Assert.assertEquals(statusMilestone1, "New");
        Assert.assertEquals(statusMilestone2, "Not Needed");


    }

    @Test
    public void checkIfNameIsNotEditableForPredefinedMilestone(){
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processWizardPage.definedMilestoneInProcess("Milestone Process",0L,"GK Milestone");
        processWizardPage.getPredefinedList().getVisibleRows().get(0).isEditableAttribute("name");
        Assert.assertFalse(processWizardPage.getPredefinedList().getVisibleRows().get(0).isEditableAttribute("name"));
        processWizardPage.clickCancelButton();

    }


}
