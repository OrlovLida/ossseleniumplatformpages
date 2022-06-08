package com.oss.pages.platform;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.pages.BasePage;

public class SearchObjectTypePage extends BasePage {

    private static final String TYPE_INPUT_ID = "type-chooser";
    private static final String OPEN_BUTTON_ID = "open-button";

    public SearchObjectTypePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void searchType(String type) {
        getSearchTypeInput().setSingleStringValue(type);
        clickOpen();
    }

    public void searchTypeContains(String type) {
        getSearchTypeInput().setSingleStringValueContains(type);
        clickOpen();
    }

    private void clickOpen() {
        Button.createById(driver, OPEN_BUTTON_ID).click();
    }

    private Input getSearchTypeInput() {
        return ComponentFactory.create(TYPE_INPUT_ID, driver, wait);
    }
}
