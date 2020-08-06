package com.oss.pages.platform;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;

public class FormAppPage extends BasePage {

    @FindBy(xpath = "//*[@class = 'radio']/label")
    private List<WebElement> radios;

    @FindBy(xpath = "//*[@class = 'form-element slider-input']/input")
    private List<WebElement> slides;

    @FindBy(xpath = "//input[@type = 'text' and @label='Mock Slider: Integer']")
    private List<WebElement> inputsForSlides;

    FormAppPage(WebDriver driver) {
        super(driver);
    }

    public FormAppPage checkRadio(int number) {
        DelayUtils.waitForVisibility(wait,radios);
        radios.get(number - 1)
                .click();
        return this;
    }

    public FormAppPage setSlideToMin(int slideNumber) {
        DelayUtils.waitForVisibility(wait,slides);
        WebElement slide = slides.get(slideNumber - 1);
        slide.sendKeys(Keys.ARROW_LEFT);
        return this;
    }

    public String getValue(int number) {
        DelayUtils.waitForVisibility(wait,inputsForSlides);
        int number1 = number - 1;
        return inputsForSlides.get(number1).getAttribute("value");
    }

    public WebElement getRadio(int number) {
        return radios.get(1);
    }
}
