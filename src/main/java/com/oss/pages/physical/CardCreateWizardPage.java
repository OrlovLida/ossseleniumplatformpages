package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.sun.xml.internal.bind.v2.TODO;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.sql.Driver;

public class CardCreateWizardPage extends BasePage {

    private Wizard wizard = Wizard.createByComponentId(driver, wait, "wizard");
    private static final String MODEL_SEARCH = "model";
    //private static final String SLOT = "model";
    //private static final String UPDATE_BUTTON = "change_model_common_buttons_app-1";

    public CardCreateWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Set Model")
    public void setModel(String model) {
        Input modelComponent = wizard.getComponent(MODEL_SEARCH, Input.ComponentType.SEARCH_FIELD);
        modelComponent.setSingleStringValue(model);
    }

    //TODO: change this method after adding data-attributename
    @Step("Set Slots")
    public void setSlots(String slots) {
        DelayUtils.waitByXPath(wait, "//span[text()='Slot']/../../../..");
        driver.findElement(By.xpath("//span[text()='Slot']/../../../..")).click();
        DelayUtils.waitByXPath(wait, "//*[@class='combo-box__dropdown__search']//input");
        WebElement search = driver.findElement(By.xpath("//*[@class='combo-box__dropdown__search']//input"));
        search.sendKeys(slots);
        search.sendKeys(Keys.ARROW_DOWN);
        search.sendKeys(Keys.ENTER);
        search.sendKeys(Keys.ESCAPE);
    }

    public static CardCreateWizardPage goToCardCreateWizardPageLive(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/physical-inventory/deviceoverview?perspective=LIVE&id=75530309", basicURL));
        return new CardCreateWizardPage(driver);
    }

    @Step("Click Accept button")
    public void clickAccept() {
        wizard.clickAccept();
    }

}
