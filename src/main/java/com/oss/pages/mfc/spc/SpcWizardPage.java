package com.oss.pages.mfc.spc;

import java.util.Arrays;
import java.util.Objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class SpcWizardPage<R extends BasePage> extends BasePage {

    private final Wizard wizard;
    private final R returnPage;

    protected SpcWizardPage(WebDriver driver, WebDriverWait wait, R returnPage) {
        super(driver);
        this.returnPage = returnPage;
        wizard = Wizard.createWizard(driver, wait);
    }

    public static <R extends BasePage> SpcWizardPage<R> create(WebDriver driver, WebDriverWait wait, R returnPage) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return new SpcWizardPage(driver, wait, returnPage);
    }

    @Step("Set description")
    public SpcWizardPage<R> setDescription(String description) {
        wizard.getComponent("description-text-area", ComponentType.TEXT_AREA)
            .setSingleStringValue(description);
        return this;
    }

    @Step("Set name")
    public SpcWizardPage<R> setName(String name) {
        wizard.getComponent("name", ComponentType.TEXT_FIELD)
            .setSingleStringValue(name);
        return this;
    }

    @Step("Set is additional")
    public SpcWizardPage<R> setIsAdditional(boolean isAdditional) {
        wizard.getComponent("is-additional", ComponentType.COMBOBOX)
            .setValue(Data.createFindFirst(Objects.toString(isAdditional)));
        return this;
    }

    @Step("Set length")
    public SpcWizardPage<R> setLength(Length length) {
        wizard.getComponent("point-code-length", ComponentType.COMBOBOX)
            .setValue(Data.createFindFirst(length.name()));
        return this;
    }

    @Step("Set network identifier")
    public SpcWizardPage<R> setNetworkIdentifier(NetworkIdentifier networkIdentifier) {
        wizard.getComponent("network-identifier", ComponentType.COMBOBOX)
            .setValue(Data.createFindFirst(networkIdentifier.getReadable()));
        return this;
    }

    @Step("Set predefined format")
    public SpcWizardPage<R> setFormat(Format format) {
        wizard.getComponent("point-code-format", ComponentType.COMBOBOX)
            .setValue(Data.createFindFirst(format.getReadable()));
        return this;
    }

    @Step("Change format to custom")
    public SpcWizardPage<R> setCustomFormatOn(boolean customOn) {
        wizard.getComponent("format-checkbox", ComponentType.CHECKBOX)
            .setSingleStringValue(Objects.toString(customOn));
        return this;
    }

    @Step("Set custom format")
    public SpcWizardPage<R> setCustomFormat(String format) {
        wizard.getComponent("custom-point-code-format", ComponentType.TEXT_FIELD)
            .setSingleStringValue(format);
        return this;
    }

    @Step("Set spc value")
    public SpcWizardPage<R> setSpcValue(String value) {
        wizard.getComponent("point-code-value", ComponentType.TEXT_FIELD)
            .setSingleStringValue(value);
        return this;
    }

    @Step("Set spc value")
    public SpcWizardPage<R> setSpcValue(int value) {
        return setSpcValue(Objects.toString(value));
    }

    @Step("Close SPC wizard without creating instance")
    public R cancel() {
        wizard.cancel();
        return returnPage;
    }

    @Step("Submit creation of SPC")
    public R submit() {
        wizard.clickAccept();
        DelayUtils.sleep(5000);
        return returnPage;
    }

    public enum Format {
        DECIMAL("Decimal"),
        HEXADECIMAL("Hexadecimal"),
        F_3_8_3("3-8-3"),
        F_3_4_3_4("3-4-3-4"),
        F_7_7("7-7"),
        F_8_8_8("8-8-8");

        private final String readable;

        Format(String readable) {
            this.readable = readable;
        }

        public static Format of(String value) {
            return Arrays.stream(values())
                       .filter(t -> t.getReadable().equals(value)).findFirst()
                       .orElseThrow(() -> new EnumConstantNotPresentException(Format.class, value));
        }

        public String getReadable() {
            return readable;
        }
    }

    public enum NetworkIdentifier {
        INT("INT"),
        INT_R("INT-R"),
        NAT("NAT"),
        NAT_R("NAT-R");

        private final String readable;

        NetworkIdentifier(String readable) {
            this.readable = readable;
        }

        public String getReadable() {
            return readable;
        }
    }

    public enum Length {
        ITU,
        ANSI
    }

}
