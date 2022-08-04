package com.oss.pages.bpm.processinstances;

import com.oss.pages.bpm.Forecast;
import com.oss.pages.bpm.milestones.Milestone;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final Optional<List<List<String>>> processRolesList;

    private ProcessCreationWizardProperties(ProcessCreationBuilder builder) {
        this.milestoneList = builder.milestoneList;
        this.processAmount = builder.processAmount;
        this.programNamesList = builder.programNamesList;
        this.forecastsList = builder.forecastsList;
        this.cronExpression = builder.cronExpression;
        this.processRolesList = builder.processRolesList;

        this.processName = builder.processName;
        this.processType = builder.processType;
        this.processPlusDays = builder.processPlusDays;

        this.programName = builder.programName;
        this.programType = builder.programType;
        this.programPlusDays = builder.programPlusDays;

        this.processWithProgramName = builder.processWithProgramName;
        this.processWithProgramType = builder.processWithProgramType;
        this.processWithProgramDays = builder.processWithProgramDays;
    }

    public static ProcessCreationBuilder builder() {
        return new ProcessCreationBuilder();
    }

    public Optional<List<Milestone>> getMilestoneList() {
        return milestoneList;
    }

    public Optional<AtomicInteger> getProcessAmount() {
        return processAmount;
    }

    public Optional<List<String>> getProgramNamesList() {
        return programNamesList;
    }

    public Optional<List<Forecast>> getForecastsList() {
        return forecastsList;
    }

    public Optional<String> getCronExpression() {
        return cronExpression;
    }

    public Optional<String> getProcessName() {
        return processName;
    }

    public Optional<String> getProcessType() {
        return processType;
    }

    public Optional<Long> getProcessPlusDays() {
        return processPlusDays;
    }

    public Optional<String> getProgramName() {
        return programName;
    }

    public Optional<String> getProgramType() {
        return programType;
    }

    public Optional<Long> getProgramPlusDays() {
        return programPlusDays;
    }

    public Optional<String> getProcessWithProgramName() {
        return processWithProgramName;
    }

    public Optional<String> getProcessWithProgramType() {
        return processWithProgramType;
    }

    public Optional<Long> getProcessWithProgramDays() {
        return processWithProgramDays;
    }

    public Optional<List<List<String>>> getProcessRolesList() {
        return processRolesList;
    }

    public static class ProcessCreationBuilder {

        private Optional<List<Milestone>> milestoneList = Optional.empty();
        private Optional<AtomicInteger> processAmount = Optional.empty();
        private Optional<List<String>> programNamesList = Optional.empty();
        private Optional<List<Forecast>> forecastsList = Optional.empty();
        private Optional<String> cronExpression = Optional.empty();
        private Optional<String> processName = Optional.empty();
        private Optional<String> processType = Optional.empty();
        private Optional<Long> processPlusDays = Optional.empty();

        private Optional<String> programName = Optional.empty();
        private Optional<String> programType = Optional.empty();
        private Optional<Long> programPlusDays = Optional.empty();

        private Optional<String> processWithProgramName = Optional.empty();
        private Optional<String> processWithProgramType = Optional.empty();
        private Optional<Long> processWithProgramDays = Optional.empty();
        private Optional<List<List<String>>> processRolesList = Optional.empty();

        public ProcessCreationBuilder createProcess(String processName, String processType, Long plusDays) {
            this.processName = Optional.ofNullable(processName);
            this.processType = Optional.ofNullable(processType);
            this.processPlusDays = Optional.ofNullable(plusDays);
            return this;
        }

        public ProcessCreationBuilder createProgram(String programName, String programType, Long plusDays) {
            this.programName = Optional.ofNullable(programName);
            this.programType = Optional.ofNullable(programType);
            this.programPlusDays = Optional.ofNullable(plusDays);
            return this;
        }

        public ProcessCreationBuilder addMilestones(List<Milestone> milestoneList) {
            this.milestoneList = Optional.ofNullable(milestoneList);
            return this;
        }

        public ProcessCreationBuilder addProgramsToLink(List<String> programNamesList) {
            this.programNamesList = Optional.ofNullable(programNamesList);
            return this;
        }

        public ProcessCreationBuilder addForecasts(List<Forecast> forecastsList) {
            this.forecastsList = Optional.ofNullable(forecastsList);
            return this;
        }

        public ProcessCreationBuilder addSchedule(String cronExpression) {
            this.cronExpression = Optional.ofNullable(cronExpression);
            return this;
        }

        public ProcessCreationBuilder createMultipleProcesses(int processAmount) {
            this.processAmount = Optional.of(new AtomicInteger(processAmount));
            return this;
        }

        public ProcessCreationBuilder addProcessCreation(String processName, String processType, Long plusDays) {
            this.processWithProgramName = Optional.ofNullable(processName);
            this.processWithProgramType = Optional.ofNullable(processType);
            this.processWithProgramDays = Optional.ofNullable(plusDays);
            return this;
        }

        public ProcessCreationBuilder addProcessRolesAssignment(List<List<String>> processRolesList) {
            this.processRolesList = Optional.ofNullable(processRolesList);
            return this;
        }

        public ProcessCreationWizardProperties build() {
            return new ProcessCreationWizardProperties(this);
        }
    }
}


