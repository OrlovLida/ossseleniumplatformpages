package com.oss.pages.templatecm.templatemanager;

import org.openqa.selenium.WebDriver;

import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * Created by Bartłomiej Jędrzejczyk on 2022-04-15
 */
public class TemplateDetailsPage extends BasePage {

    private static final String DETAILS_WIDGET_ID = "card-content_TemplateDetailsWindow";
    private static final String NAME_PROPERTY = "Name";
    private static final String DESCRIPTION_PROPERTY = "Description";

    private final OldPropertyPanel propertyPanel;

    public TemplateDetailsPage(WebDriver driver) {
        super(driver);
        propertyPanel = OldPropertyPanel.createById(driver, wait, DETAILS_WIDGET_ID);
    }

    @Step("Get name")
    public String getName() {
        return propertyPanel.getPropertyValue(NAME_PROPERTY);
    }

    @Step("Get description")
    public String getDescription() {
        return propertyPanel.getPropertyValue(DESCRIPTION_PROPERTY);
    }
}
