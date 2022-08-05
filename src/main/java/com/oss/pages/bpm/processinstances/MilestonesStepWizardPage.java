package com.oss.pages.bpm.processinstances;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.milestones.MilestoneWizardPage;
import org.openqa.selenium.WebDriver;

public class MilestonesStepWizardPage extends ProcessWizardPage {
    private static final String PREDEFINED_MILESTONE_LIST = "editMilestonesComponentId";
    private static final String ADD_MILESTONE_LIST = "addMilestonesComponentId";

    public MilestonesStepWizardPage(WebDriver driver) {
        super(driver);
    }

    public MilestonesStepWizardPage defineProcessAndGoToMilestonesStep(String processName, Long plusDays, String processType) {
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        Wizard processWizard = definedBasicProcess(processName, processType, plusDays);
        processWizardPage.selectCheckbox(processWizard, MILESTONE_ENABLED_CHECKBOX_ID);
        DelayUtils.sleep();
        processWizard.clickButtonById(NEXT_BUTTON);
        return new MilestonesStepWizardPage(driver);
    }

    public EditableList getMilestonePredefinedList() {
        return EditableList.createById(driver, wait, PREDEFINED_MILESTONE_LIST);
    }

    public Milestone addMilestoneRow(Milestone milestone) {
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver, ADD_MILESTONE_LIST);
        return milestoneWizardPage.addMilestoneRow(milestone);
    }

    public Milestone editPredefinedMilestone(Milestone milestone, int row) {
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver, PREDEFINED_MILESTONE_LIST);
        return milestoneWizardPage.editMilestoneRow(milestone, row);
    }
}
