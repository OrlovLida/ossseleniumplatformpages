package com.oss.pages.exportguiwizard;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SendFileByEmailPage extends ExportGuiWizardPage{

    public SendFileByEmailPage(WebDriver driver){super(driver); getWizard();}

    private String RECEIPIENTS_ID = "exportgui-components-emailreceipientsmultisearchtag";
    private String ATTACH_EXPORTED_FILE_ID = "exportgui-components-emailattachexportedcheckbox";

    public SendFileByEmailPage chooseEmail(String email){
        setValueOnMultiSearch(RECEIPIENTS_ID,email);
        return this;
    }

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
        waitForComponent(xpath);
        return driver.findElement(By.xpath(xpath));
    }
    private void typeValueInMultiSearchInput(String value){
        String xpath = "//div[contains(@id,'stickyPortal')]//input";
        waitForComponent(xpath);
        driver.findElement(By.xpath(xpath)).sendKeys(value);
    }

    private void clickOnMultiSearch(String COMPONENT_ID){
        String componentPath = "//div[contains (@data-attributename,'" + COMPONENT_ID + "')]//div[contains(@class, 'input')]";
        waitForComponent(componentPath);
        driver.findElement(By.xpath(componentPath)).click();
    }
}
