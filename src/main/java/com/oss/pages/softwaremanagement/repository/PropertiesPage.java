package com.oss.pages.softwaremanagement.repository;

import org.openqa.selenium.WebDriver;

import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * Created by Bartłomiej Jędrzejczyk on 2022-04-19
 */
public class PropertiesPage extends BasePage {

    private static final String PROPERTIES_WINDOW_ID = "card-content_smComponent_SoftwareRepositoryViewIdPropertiesWindowId";
    private static final String FILE_NAME_PROPERTY = "File name";
    private static final String PATH_PROPERTY = "Path";
    private static final String SIZE_PROPERTY = "Size";

    private final OldPropertyPanel propertyPanel;

    public PropertiesPage(WebDriver driver) {
        super(driver);
        propertyPanel = OldPropertyPanel.createById(driver, wait, PROPERTIES_WINDOW_ID);
    }

    @Step("Get File name")
    public String getFileName() {
        return propertyPanel.getPropertyValue(FILE_NAME_PROPERTY);
    }

    @Step("Get path")
    public String getPath() {
        return propertyPanel.getPropertyValue(PATH_PROPERTY);
    }

    @Step("Get size")
    public String getSize() {
        return propertyPanel.getPropertyValue(SIZE_PROPERTY);
    }
}
