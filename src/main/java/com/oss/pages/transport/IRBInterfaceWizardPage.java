package com.oss.pages.transport;

import org.openqa.selenium.WebDriver;
import com.oss.pages.BasePage;

public class IRBInterfaceWizardPage extends BasePage {
    public static IRBInterfaceWizardPage goToIRBInterfaceWizardPage(WebDriver driver, String basicURL){
        driver.get(String.format("%s/#/view/transport/ip/ethernet/irb-interface?"+"perspective=LIVE", basicURL));
        return new IRBInterfaceWizardPage(driver);
    }

    public IRBInterfaceWizardPage(WebDriver driver) {
        super(driver);
    }
}
