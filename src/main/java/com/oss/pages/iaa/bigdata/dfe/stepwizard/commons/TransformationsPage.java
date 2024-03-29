package com.oss.pages.iaa.bigdata.dfe.stepwizard.commons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.iaa.widgets.dfe.transformationsmanager.TransformationsManagerWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class TransformationsPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(TransformationsPage.class);
    private final Wizard transformationWizard;

    public TransformationsPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait);
        transformationWizard = Wizard.createByComponentId(driver, wait, wizardId);
    }

    public void addNewTransformation(String transformationName) {
        TransformationsManagerWidget transformationsManager = TransformationsManagerWidget.create(driver, wait);
        transformationsManager.selectTransformation(transformationName);
        transformationsManager.clickAdd();
        log.debug("Adding new transformation: {}", transformationName);
    }

    @Step("I fill transformations Step Aggregate with {transformationName}")
    public void fillTransformationsStep(String transformationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        addNewTransformation(transformationName);
        log.info("Filled Transformations Step");
    }

    @Step("I click Next Step")
    public void clickNextStep() {
        DelayUtils.waitForPageToLoad(driver, wait);
        transformationWizard.clickNextStep();
        log.info("I click Next Step");
    }
}
