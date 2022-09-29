package com.oss.pages.bpm.processinstances;

import com.google.common.collect.Multimap;
import com.oss.pages.bpm.forecasts.Forecast;
import com.oss.pages.bpm.milestones.Milestone;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

/**
 * @author Paweł Rother
 */
@Getter
public class ProcessCreationWizardProperties {
    private final Optional<List<Milestone>> milestoneList;
    private final Optional<Integer> processAmount;
    private final Optional<List<String>> programNamesList;
    private final Optional<List<Forecast>> forecastsList;
    private final Optional<String> cronExpression;
    private final Optional<ScheduleProperties> scheduleProperties;
    private final Optional<String> processName;
    private final Optional<String> processType;
    private final Optional<Long> processPlusDays;

    private final Optional<String> programName;
    private final Optional<String> programType;
    private final Optional<Long> programPlusDays;

    private final Optional<String> processWithProgramName;
    private final Optional<String> processWithProgramType;
    private final Optional<Long> processWithProgramDays;
    private final Optional<Multimap<String, String>> processRolesList;

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
    }

    public static ProcessCreationWizardPropertiesBuilder builder() {
        return new ProcessCreationWizardPropertiesBuilder();
    }

    public static class ProcessCreationWizardPropertiesBuilder {

        private Optional<List<Milestone>> milestoneList;
        private Optional<Integer> processAmount;
        private Optional<List<String>> programNamesList;
        private Optional<List<Forecast>> forecastsList;
        private Optional<String> cronExpression;
        private Optional<ScheduleProperties> scheduleProperties;
        private Optional<String> processName;
        private Optional<String> processType;
        private Optional<Long> processPlusDays;
        private Optional<String> programName;
        private Optional<String> programType;
        private Optional<Long> programPlusDays;
        private Optional<String> processWithProgramName;
        private Optional<String> processWithProgramType;
        private Optional<Long> processWithProgramDays;
        private Optional<Multimap<String, String>> processRolesList;

        ProcessCreationWizardPropertiesBuilder() {
        }

        public ProcessCreationWizardPropertiesBuilder basicProcess(String processName, String processType, Long plusDays) {
            this.processName = Optional.ofNullable(processName);
            this.processType = Optional.ofNullable(processType);
            this.processPlusDays = Optional.ofNullable(plusDays);
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder basicProgram(String programName, String programType, Long plusDays) {
            this.programName = Optional.ofNullable(programName);
            this.programType = Optional.ofNullable(programType);
            this.programPlusDays = Optional.ofNullable(plusDays);
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withMilestones(List<Milestone> milestoneList) {
            this.milestoneList = Optional.ofNullable(milestoneList);
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withProgramsToLink(List<String> programNamesList) {
            this.programNamesList = Optional.ofNullable(programNamesList);
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withForecasts(List<Forecast> forecastsList) {
            this.forecastsList = Optional.ofNullable(forecastsList);
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withSchedule(String cronExpression) {
            this.cronExpression = Optional.ofNullable(cronExpression);
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withSchedule(ScheduleProperties scheduleProperties) {
            this.scheduleProperties = Optional.ofNullable(scheduleProperties);
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withMultipleProcesses(int processAmount) {
            this.processAmount = Optional.of(processAmount);
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withProcessCreation(String processName, String processType, Long plusDays) {
            this.processWithProgramName = Optional.ofNullable(processName);
            this.processWithProgramType = Optional.ofNullable(processType);
            this.processWithProgramDays = Optional.ofNullable(plusDays);
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withProcessRolesAssignment(Multimap<String, String> processRolesList) {
            this.processRolesList = Optional.ofNullable(processRolesList);
            return this;
        }

        public ProcessCreationWizardProperties build() {
            return new ProcessCreationWizardProperties(this);
        }
    }
}


