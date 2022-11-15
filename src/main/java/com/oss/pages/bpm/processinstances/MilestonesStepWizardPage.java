package com.oss.pages.bpm.processinstances;

import com.oss.framework.widgets.list.EditableList;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.milestones.MilestoneWizardPage;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Paweł Rother
 */
public class MilestonesStepWizardPage extends ProcessWizardPage {
    private static final String PREDEFINED_MILESTONE_LIST = "editMilestonesComponentId";
    private static final String ADD_MILESTONE_LIST = "addMilestonesComponentId";
    private static final String ADD_MILESTONE_INFO = "Adding Milestone";
    private static final String ADD_MILESTONES_INFO = "Adding Milestones";
    private static final String EDIT_PREDEFINED_MILESTONE = "Editing Predefined Milestone";
    private static final Logger LOGGER = LoggerFactory.getLogger(MilestonesStepWizardPage.class);

    public MilestonesStepWizardPage(WebDriver driver) {
        super(driver);
    }

    public EditableList getMilestonePredefinedList() {
        return EditableList.createById(driver, wait, PREDEFINED_MILESTONE_LIST);
    }

    public Milestone addMilestoneRow(Milestone milestone) {
        LOGGER.info(ADD_MILESTONE_INFO);
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver, ADD_MILESTONE_LIST);
        return milestoneWizardPage.addMilestoneRow(milestone);
    }

    public List<Milestone> addMilestones(List<Milestone> milestones) {
        LOGGER.info(ADD_MILESTONES_INFO);
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver, ADD_MILESTONE_LIST);
        return milestones.stream().map(milestoneWizardPage::addMilestoneRow).collect(Collectors.toList());
    }

    public Milestone editPredefinedMilestone(Milestone milestone, int row) {
        LOGGER.info(EDIT_PREDEFINED_MILESTONE);
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver, PREDEFINED_MILESTONE_LIST);
        return milestoneWizardPage.editMilestoneRow(milestone, row);
    }
}
