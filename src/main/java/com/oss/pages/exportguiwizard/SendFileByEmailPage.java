package com.oss.pages.exportguiwizard;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.oss.framework.utils.DelayUtils;

public class SendFileByEmailPage extends ExportGuiWizardPage{

    public SendFileByEmailPage(WebDriver driver){super(driver); getWizard();}

    private String RECEIPIENTS_ID = "exportgui-components-emailreceipientsmultisearchtag";
    private String ATTACH_EXPORTED_FILE_ID = "exportgui-components-emailattachexportedcheckbox";

    @Step("Choose email address to send")
    public SendFileByEmailPage chooseEmail(String email){
        setValueOnMultiSearch(RECEIPIENTS_ID,email);
        return this;
    }

    @Step("Check the checkbox to attach exported file")
    public SendFileByEmailPage chooseAttachExportedFile(){
        checkTheCheckbox(ATTACH_EXPORTED_FILE_ID);
        return this;
    }


    private void setValueOnMultiSearch(String COMPONENT_ID, String value) {
        clickOnMultiSearch(COMPONENT_ID);
        typeValueInMultiSearchInput(value);
        textWrapperWirhText(value).click();
    }

    private WebElement textWrapperWirhText(String text){
        String xpath = "//div[@class='text-wrapper' and contains(text(), '"+text+"')]";
        DelayUtils.waitForComponent(wait,xpath);
        return driver.findElement(By.xpath(xpath));
    }

    private void typeValueInMultiSearchInput(String value){
        String xpath = "//div[contains(@id,'stickyPortal')]//input";
        DelayUtils.waitForComponent(wait,xpath);
        driver.findElement(By.xpath(xpath)).sendKeys(value);
    }

    private void clickOnMultiSearch(String COMPONENT_ID){
        String componentPath = "//div[contains (@data-attributename,'" + COMPONENT_ID + "')]//div[contains(@class, 'input')]";
        DelayUtils.waitForComponent(wait,componentPath);
        driver.findElement(By.xpath(componentPath)).click();
    }
}
