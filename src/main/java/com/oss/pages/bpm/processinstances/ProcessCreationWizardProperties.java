package com.oss.pages.bpm.processinstances;

import com.google.common.collect.Multimap;
import com.oss.pages.bpm.forecasts.Forecast;
import com.oss.pages.bpm.milestones.Milestone;
import lombok.Getter;

import java.util.List;

/**
 * @author Paweł Rother
 */
@Getter
public class ProcessCreationWizardProperties {
    private final List<Milestone> milestoneList;
    private final Integer processAmount;
    private final List<String> programNamesList;
    private final List<Forecast> forecastsList;
    private final String cronExpression;
    private final ScheduleProperties scheduleProperties;
    private final String processName;
    private final String processType;
    private final Long processPlusDays;

    private final String programName;
    private final String programType;
    private final Long programPlusDays;

    private final String processWithProgramName;
    private final String processWithProgramType;
    private final Long processWithProgramDays;
    private final Multimap<String, String> processRolesList;

    private final boolean isProcessCreation;
    private final boolean isProgramCreation;
    private final boolean isMilestonesCreation;
    private final boolean isForecastsCreation;
    private final boolean isScheduleCreation;
    private final boolean isProcessRolesAssignment;
    private final boolean isMultipleProcessesCreation;
    private final boolean isProgramsToLink;
    private final boolean isProgramWithProcessCreation;

    ProcessCreationWizardProperties(ProcessCreationWizardPropertiesBuilder builder) {
        this.milestoneList = builder.milestoneList;
        this.processAmount = builder.processAmount;
        this.programNamesList = builder.programNamesList;
        this.forecastsList = builder.forecastsList;
        this.cronExpression = builder.cronExpression;
        this.scheduleProperties = builder.scheduleProperties;
        this.processName = builder.processName;
        this.processType = builder.processType;
        this.processPlusDays = builder.processPlusDays;
        this.programName = builder.programName;
        this.programType = builder.programType;
        this.programPlusDays = builder.programPlusDays;
        this.processWithProgramName = builder.processWithProgramName;
        this.processWithProgramType = builder.processWithProgramType;
        this.processWithProgramDays = builder.processWithProgramDays;
        this.processRolesList = builder.processRolesList;

        this.isProcessCreation = builder.isProcessCreation;
        this.isProgramCreation = builder.isProgramCreation;
        this.isMilestonesCreation = builder.isMilestonesCreation;
        this.isForecastsCreation = builder.isForecastsCreation;
        this.isScheduleCreation = builder.isScheduleCreation;
        this.isProcessRolesAssignment = builder.isProcessRolesAssignment;
        this.isMultipleProcessesCreation = builder.isMultipleProcessesCreation;
        this.isProgramsToLink = builder.isProgramsToLink;
        this.isProgramWithProcessCreation = builder.isProgramWithProcessCreation;
    }

    public static ProcessCreationWizardPropertiesBuilder builder() {
        return new ProcessCreationWizardPropertiesBuilder();
    }

    public static class ProcessCreationWizardPropertiesBuilder {

        private List<Milestone> milestoneList;
        private Integer processAmount;
        private List<String> programNamesList;
        private List<Forecast> forecastsList;
        private String cronExpression;
        private ScheduleProperties scheduleProperties;
        private String processName;
        private String processType;
        private Long processPlusDays;
        private String programName;
        private String programType;
        private Long programPlusDays;
        private String processWithProgramName;
        private String processWithProgramType;
        private Long processWithProgramDays;
        private Multimap<String, String> processRolesList;

        private boolean isProcessCreation = false;
        private boolean isProgramCreation = false;
        private boolean isMilestonesCreation = false;
        private boolean isForecastsCreation = false;
        private boolean isScheduleCreation = false;
        private boolean isProcessRolesAssignment = false;
        private boolean isMultipleProcessesCreation = false;
        private boolean isProgramsToLink = false;
        private boolean isProgramWithProcessCreation = false;

        ProcessCreationWizardPropertiesBuilder() {
        }

        public ProcessCreationWizardPropertiesBuilder basicProcess(String processName, String processType, Long plusDays) {
            this.isProcessCreation = true;
            this.processName = processName;
            this.processType = processType;
            this.processPlusDays = plusDays;
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder basicProgram(String programName, String programType, Long plusDays) {
            this.isProgramCreation = true;
            this.programName = programName;
            this.programType = programType;
            this.programPlusDays = plusDays;
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withMilestones(List<Milestone> milestoneList) {
            this.isMilestonesCreation = true;
            this.milestoneList = milestoneList;
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withProgramsToLink(List<String> programNamesList) {
            this.isProgramsToLink = true;
            this.programNamesList = programNamesList;
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withForecasts(List<Forecast> forecastsList) {
            this.isForecastsCreation = true;
            this.forecastsList = forecastsList;
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withSchedule(String cronExpression) {
            this.isScheduleCreation = true;
            this.cronExpression = cronExpression;
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withSchedule(ScheduleProperties scheduleProperties) {
            this.isScheduleCreation = true;
            this.scheduleProperties = scheduleProperties;
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withMultipleProcesses(int processAmount) {
            this.isMultipleProcessesCreation = true;
            this.processAmount = processAmount;
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withProcessCreation(String processName, String processType, Long plusDays) {
            this.isProgramWithProcessCreation = true;
            this.processWithProgramName = processName;
            this.processWithProgramType = processType;
            this.processWithProgramDays = plusDays;
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withProcessRolesAssignment(Multimap<String, String> processRolesList) {
            this.isProcessRolesAssignment = true;
            this.processRolesList = processRolesList;
            return this;
        }

        public ProcessCreationWizardProperties build() {
            return new ProcessCreationWizardProperties(this);
        }
    }
}


