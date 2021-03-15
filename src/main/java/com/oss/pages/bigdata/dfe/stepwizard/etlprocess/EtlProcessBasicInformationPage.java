package com.oss.pages.bigdata.dfe.stepwizard.etlprocess;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.MultiSearchField;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.dfe.stepwizard.commons.BasicInformationPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EtlProcessBasicInformationPage extends BasicInformationPage {

    private static final String CATEGORY_INPUT_ID = "category";

    public EtlProcessBasicInformationPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    public void fillCategory(String category){
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        MultiSearchField categoryInput = (MultiSearchField) getWizard(driver, wait).getComponent(CATEGORY_INPUT_ID, Input.ComponentType.MULTI_SEARCH_FIELD);
        categoryInput.setValue(Data.createSingleData(category));
    }
}
