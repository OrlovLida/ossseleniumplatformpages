package com.oss.pages.filtermanager;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class EditFilterPage  extends BasePage {

    public EditFilterPage(WebDriver driver){
        super(driver);
    }

    private final String TO_DROP_XPATH = "//div[@data-rbd-droppable-id ='filterManager_wizard_rel_enabled']";
    private final String TO_TAKE_XPATH = "//div[@class='btn-drag']";
    private final String ACCEPT_BUTTON_ID = "wizard-submit-button-webFilter_wizard_filter_widget";
    private final String WIZARD_ID = "webFilter_wizard_filter_widget";

    private Wizard folderWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    public void dragAndDrop(String element1, String element2){
        DelayUtils.waitForPageToLoad(driver,wait);
        WebElement e2 = driver.findElement(By.xpath(element2));
        List<WebElement> e3 = driver.findElements(By.xpath(element1));
        WebElement e1 = e3.get(0);
        WebElement e4 = e3.get(1);

        DelayUtils.sleep(3000);
        DragAndDrop.dragAndDrop(e1,e2,driver);

    }

    @Step("Change Folder for filer using drag and drop")
    public EditFilterPage changeFolderForFilter(){
        DelayUtils.waitForPageToLoad(driver,wait);
        DragAndDrop.dragAndDrop(TO_TAKE_XPATH, TO_DROP_XPATH, driver);
        return this;
    }

    @Step("Click on Accept and close the wizard")
    public FilterManagerPage clickAccept(){
        folderWizard.clickActionById(ACCEPT_BUTTON_ID);
        return new FilterManagerPage(driver);
    }
}
