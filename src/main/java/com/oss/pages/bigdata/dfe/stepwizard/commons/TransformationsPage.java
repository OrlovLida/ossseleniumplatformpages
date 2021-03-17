package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.dfe.transformationsmanager.TransformationsManagerWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TransformationsPage extends BasePage {

    public TransformationsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void addNewTransformation(String transformationName){
        TransformationsManagerWidget transformationsManager = TransformationsManagerWidget.create(driver, wait);
        transformationsManager.selectTransformation(transformationName);
        transformationsManager.clickAdd();
    }

    @Step("I fill transformations Step Aggregate with {transformationName}")
    public void fillTransformationsStep(String transformationName){
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        addNewTransformation(transformationName);
    }
}
