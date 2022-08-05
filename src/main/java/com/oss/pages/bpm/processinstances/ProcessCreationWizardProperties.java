package com.oss.pages.bpm.processinstances;

import com.google.common.collect.Multimap;
import com.oss.pages.bpm.forecasts.Forecast;
import com.oss.pages.bpm.milestones.Milestone;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Builder
@Getter
public class ProcessCreationWizardProperties {
    private final Optional<List<Milestone>> milestoneList;
    private final Optional<AtomicInteger> processAmount;
    private final Optional<List<String>> programNamesList;
    private final Optional<List<Forecast>> forecastsList;
    private final Optional<String> cronExpression;
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

    public static class ProcessCreationWizardPropertiesBuilder {

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

        public ProcessCreationWizardPropertiesBuilder withMultipleProcesses(int processAmount) {
            this.processAmount = Optional.of(new AtomicInteger(processAmount));
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

    }
}


