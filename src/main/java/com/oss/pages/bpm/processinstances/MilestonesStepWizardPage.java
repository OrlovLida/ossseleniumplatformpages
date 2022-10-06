package com.oss.pages.bpm.processinstances;

import com.oss.framework.widgets.list.EditableList;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.milestones.MilestoneWizardPage;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Paweł Rother
 */
public class MilestonesStepWizardPage extends ProcessWizardPage {
    private static final String PREDEFINED_MILESTONE_LIST = "editMilestonesComponentId";
    private static final String ADD_MILESTONE_LIST = "addMilestonesComponentId";

    public MilestonesStepWizardPage(WebDriver driver) {
        super(driver);
    }

    public EditableList getMilestonePredefinedList() {
        return EditableList.createById(driver, wait, PREDEFINED_MILESTONE_LIST);
    }

    public Milestone addMilestoneRow(Milestone milestone) {
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver, ADD_MILESTONE_LIST);
        return milestoneWizardPage.addMilestoneRow(milestone);
    }

    public List<Milestone> addMilestones(List<Milestone> milestones) {
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver, ADD_MILESTONE_LIST);
        return milestones.stream().map(milestoneWizardPage::addMilestoneRow).collect(Collectors.toList());
    }

    public Milestone editPredefinedMilestone(Milestone milestone, int row) {
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver, PREDEFINED_MILESTONE_LIST);
        return milestoneWizardPage.editMilestoneRow(milestone, row);
    }
}
