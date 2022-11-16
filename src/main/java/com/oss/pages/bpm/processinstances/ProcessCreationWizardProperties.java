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
    private static final String INVALID_PROPERTIES_SETTING = "Invalid ProcessCreationWizardProperties settings \n";
    private static final String OPTIONS_WITHOUT_PROCESS_EXCEPTION = INVALID_PROPERTIES_SETTING +
            "\"withMultipleProcesses\", \"withProcessMilestones\", \"withProcessForecasts\" and \"withProcessRoles\" " +
            "options cannot be set without \"basicProcess\" or  \"withProcessCreation\"option";
    private static final String OPTIONS_WITHOUT_BASIC_PROGRAM_EXCEPTION = INVALID_PROPERTIES_SETTING +
            "\"withProgramMilestones\", \"withProgramForecasts\" and \"withProgramRoles\" options cannot be set without \"basicProgram\" option";
    private static final String PROGRAM_TO_LINK_EXCEPTION = INVALID_PROPERTIES_SETTING +
            "\"withProgramsToLink\" is available only with Basic Process creation";
    private static final String BASIC_PROGRAM_AND_PROCESS_EXCEPTION = INVALID_PROPERTIES_SETTING +
            "Both of \"basicProcess\" and \"basicProgram\" options set in properties. \n" +
            "If you want to create process with program use \"withProcessCreation\" method together with \"basicProgram\" in Properties builder.";
    private static final String PROGRAM_WITH_SCHEDULE_EXCEPTION = INVALID_PROPERTIES_SETTING +
            "Creating basic Program with a schedule is not available.";
    private static final String NO_BASIC_PROCESS_PROGRAM_EXCEPTION = INVALID_PROPERTIES_SETTING +
            "There is no set basic process or basic program creation.";
    private static final String CREATE_PROGRAM_WITH_PROCESS_EXCEPTION = INVALID_PROPERTIES_SETTING +
            "\"withProcessCreation\" option available only for Basic Program creation.";
    private static final String SCHEDULE_WITH_OTHER_OPTION_EXCEPTION = INVALID_PROPERTIES_SETTING +
            "Schedule creation is not available together with milestones, forecasts and multiple processes creation.";


    private final String cronExpression;
    private final ScheduleProperties scheduleProperties;

    private final String processName;
    private final String processType;
    private final Long processPlusDays;
    private final Integer processAmount;
    private final List<String> programNamesList;
    private final List<Milestone> processMilestoneList;
    private final List<Forecast> processForecastsList;
    private final Forecast processMainForecast;
    private final Multimap<String, String> processRolesList;

    private final String programName;
    private final String programType;
    private final Long programPlusDays;
    private final List<Milestone> programMilestoneList;
    private final List<Forecast> programForecastsList;
    private final Forecast programMainForecast;
    private final Multimap<String, String> programRolesList;

    private final boolean isProcessCreation;
    private final boolean isProgramCreation;
    private final boolean isProcessMilestonesCreation;
    private final boolean isProcessForecastsCreation;
    private final boolean isProcessRolesAssignment;
    private final boolean isProgramMilestonesCreation;
    private final boolean isProgramForecastsCreation;
    private final boolean isProgramRolesAssignment;
    private final boolean isScheduleCreation;
    private final boolean isMultipleProcessesCreation;
    private final boolean isProgramsToLink;
    private final boolean isProgramWithProcessCreation;


    ProcessCreationWizardProperties(ProcessCreationWizardPropertiesBuilder builder) {
        this.processMilestoneList = builder.processMilestoneList;
        this.programMilestoneList = builder.programMilestoneList;
        this.processAmount = builder.processAmount;
        this.programNamesList = builder.programNamesList;
        this.processForecastsList = builder.processForecastsList;
        this.processMainForecast = builder.processMainForecast;
        this.programForecastsList = builder.programForecastsList;
        this.programMainForecast = builder.programMainForecast;
        this.cronExpression = builder.cronExpression;
        this.scheduleProperties = builder.scheduleProperties;
        this.processName = builder.processName;
        this.processType = builder.processType;
        this.processPlusDays = builder.processPlusDays;
        this.programName = builder.programName;
        this.programType = builder.programType;
        this.programPlusDays = builder.programPlusDays;
        this.processRolesList = builder.processRolesList;
        this.programRolesList = builder.programRolesList;

        this.isProcessCreation = builder.isProcessCreation;
        this.isProgramCreation = builder.isProgramCreation;
        this.isProcessMilestonesCreation = builder.isProcessMilestonesCreation;
        this.isProcessForecastsCreation = builder.isProcessForecastsCreation;
        this.isProcessRolesAssignment = builder.isProcessRolesAssignment;
        this.isProgramMilestonesCreation = builder.isProgramMilestonesCreation;
        this.isProgramForecastsCreation = builder.isProgramForecastsCreation;
        this.isProgramRolesAssignment = builder.isProgramRolesAssignment;
        this.isScheduleCreation = builder.isScheduleCreation;
        this.isMultipleProcessesCreation = builder.isMultipleProcessesCreation;
        this.isProgramsToLink = builder.isProgramsToLink;
        this.isProgramWithProcessCreation = builder.isProgramWithProcessCreation;
    }

    public static ProcessCreationWizardPropertiesBuilder builder() {
        return new ProcessCreationWizardPropertiesBuilder();
    }

    public static class ProcessCreationWizardPropertiesBuilder {
        private String cronExpression;
        private ScheduleProperties scheduleProperties;

        private String processName;
        private String processType;
        private Long processPlusDays;
        private Integer processAmount;
        private List<String> programNamesList;

        private List<Milestone> processMilestoneList;
        private List<Forecast> processForecastsList;
        private Forecast processMainForecast;
        private Multimap<String, String> processRolesList;

        private String programName;
        private String programType;
        private Long programPlusDays;

        private List<Milestone> programMilestoneList;
        private List<Forecast> programForecastsList;
        private Forecast programMainForecast;
        private Multimap<String, String> programRolesList;

        private boolean isProcessCreation = false;
        private boolean isProgramCreation = false;
        private boolean isProcessMilestonesCreation = false;
        private boolean isProcessForecastsCreation = false;
        private boolean isProcessRolesAssignment = false;

        private boolean isProgramMilestonesCreation = false;
        private boolean isProgramForecastsCreation = false;
        private boolean isProgramRolesAssignment = false;
        private boolean isScheduleCreation = false;
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

        public ProcessCreationWizardPropertiesBuilder withProgramsToLink(List<String> programNamesList) {
            this.isProgramsToLink = true;
            this.programNamesList = programNamesList;
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withProcessMilestones(List<Milestone> milestoneList) {
            this.isProcessMilestonesCreation = true;
            this.processMilestoneList = milestoneList;
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withProcessForecasts(Forecast mainForecast, List<Forecast> forecastsList) {
            this.isProcessForecastsCreation = true;
            this.processForecastsList = forecastsList;
            this.processMainForecast = mainForecast;
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withProcessForecast(Forecast mainForecast) {
            this.isProcessForecastsCreation = true;
            this.processMainForecast = mainForecast;
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withProgramMilestones(List<Milestone> milestoneList) {
            this.isProgramMilestonesCreation = true;
            this.programMilestoneList = milestoneList;
            return this;
        }


        public ProcessCreationWizardPropertiesBuilder withProgramForecasts(Forecast mainForecast, List<Forecast> forecastsList) {
            this.isProgramForecastsCreation = true;
            this.programForecastsList = forecastsList;
            this.programMainForecast = mainForecast;
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withProgramForecast(Forecast mainForecast) {
            this.isProgramForecastsCreation = true;
            this.programMainForecast = mainForecast;
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
            this.processName = processName;
            this.processType = processType;
            this.processPlusDays = plusDays;
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withProcessRolesAssignment(Multimap<String, String> processRolesList) {
            this.isProcessRolesAssignment = true;
            this.processRolesList = processRolesList;
            return this;
        }

        public ProcessCreationWizardPropertiesBuilder withProgramRolesAssignment(Multimap<String, String> programRolesList) {
            this.isProgramRolesAssignment = true;
            this.programRolesList = programRolesList;
            return this;
        }

        public ProcessCreationWizardProperties build() {
            if (isProcessCreation && isProgramCreation)
                throw new IllegalArgumentException(BASIC_PROGRAM_AND_PROCESS_EXCEPTION);
            if (isProcessCreation && isProgramWithProcessCreation)
                throw new IllegalArgumentException(CREATE_PROGRAM_WITH_PROCESS_EXCEPTION);
            if (isScheduleCreation && isProgramCreation)
                throw new IllegalArgumentException(PROGRAM_WITH_SCHEDULE_EXCEPTION);
            if (isScheduleCreation && (isProcessForecastsCreation || isProcessMilestonesCreation || isMultipleProcessesCreation))
                throw new IllegalArgumentException(SCHEDULE_WITH_OTHER_OPTION_EXCEPTION);
            if (isProgramCreation && isProgramsToLink)
                throw new IllegalArgumentException(PROGRAM_TO_LINK_EXCEPTION);
            if (!isProcessCreation && !isProgramCreation)
                throw new IllegalArgumentException(NO_BASIC_PROCESS_PROGRAM_EXCEPTION);
            if ((isProcessMilestonesCreation || isProcessForecastsCreation || isProcessRolesAssignment || isMultipleProcessesCreation)
                    && !(isProcessCreation || isProgramWithProcessCreation))
                throw new IllegalArgumentException(OPTIONS_WITHOUT_PROCESS_EXCEPTION);
            if ((isProgramMilestonesCreation || isProgramForecastsCreation || isProgramRolesAssignment) && !isProgramCreation)
                throw new IllegalArgumentException(OPTIONS_WITHOUT_BASIC_PROGRAM_EXCEPTION);

            return new ProcessCreationWizardProperties(this);
        }
    }
}


