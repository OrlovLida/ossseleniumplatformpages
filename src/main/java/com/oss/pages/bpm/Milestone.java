/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import java.util.Optional;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.listwidget.EditableList;
import com.oss.pages.BasePage;

/**
 * @author Gabriela Kasza
 */
public class Milestone {
    
    private final String name;
    private final String description;
    private final String dueDate;
    private final String relatedTask;
    private final String leadTime;
    private final String isActive;
    private final String isManualCompletion;
    
    public static Builder builder() {
        return new Builder();
    }
    
    private Milestone(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.dueDate = builder.dueDate;
        this.relatedTask = builder.relatedTask;
        this.leadTime = builder.leadTime;
        this.isActive = builder.isActive;
        this.isManualCompletion = builder.isManualCompletion;
    }
    
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }
    
    public Optional<String> getRelatedTask() {
        return Optional.ofNullable(relatedTask);
    }
    
    public Optional<String> getLeadTime() {
        return Optional.ofNullable(leadTime);
    }
    
    public Optional<String> getIsActive() {
        return Optional.ofNullable(isActive);
    }
    
    public Optional<String> getIsManualCompletion() {
        return Optional.ofNullable(isManualCompletion);
    }
    
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }
    
    public Optional<String> getDueDate() {
        return Optional.ofNullable(dueDate);
    }
    
    public static class Builder {
        private String name;
        private String description;
        private String dueDate;
        private String relatedTask;
        private String leadTime;
        private String isActive;
        private String isManualCompletion;
        
        public Builder setName(String value) {
            this.name = value;
            return this;
        }
        
        public Builder setDescription(String value) {
            this.description = value;
            return this;
        }
        
        public Builder setDueDate(String value) {
            this.dueDate = value;
            return this;
        }
        
        public Builder setRelatedTask(String relatedTask) {
            this.relatedTask = relatedTask;
            return this;
        }
        
        public Builder setLeadTime(String leadTime) {
            this.leadTime = leadTime;
            return this;
        }
        
        public Builder setIsActive(String isActive) {
            this.isActive = isActive;
            return this;
        }
        
        public Builder setIsManualCompletion(String isManualCompletion) {
            this.isManualCompletion = isManualCompletion;
            return this;
        }
        
        public Milestone build() {
            
            return new Milestone(this);
        }
        
    }
}
