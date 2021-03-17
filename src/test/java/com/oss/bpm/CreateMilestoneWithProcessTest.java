/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.bpm;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.Milestone;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.utils.TestListener;

/**
 * @author Gabriela Kasza
 */
@Listeners({TestListener.class})
public class CreateMilestoneWithProcessTest extends BaseTestCase {
    
    private String processName = "Selenium Test.Milestone-" + (int) (Math.random() * 1001);
    
    @BeforeClass
    public void openProcessInstancesPage() {
        ProcessInstancesPage processInstancesPage = ProcessInstancesPage.goToProcessInstancesPage(driver, BASIC_URL);
        DelayUtils.sleep(3000);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        processInstancesPage.changeUser("bpm_webselenium", "bpmweb");
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        
    }
    
    @Test(priority = 1)
    public void createProcessWithDefinedMilestones() {
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processWizardPage.definedMilestoneInProcess(processName, 10L, "Data Correction Process");
        Milestone milestone1 = Milestone.builder()
                .setDescription("abc")
                .setDueDate("2021-03-15")
                .setIsActive("true")
                .setName("My milestone").build();

        processWizardPage.addMilestoneRow(milestone1, 1);

        System.out.println("test");
        
    }
}
