package com.oss.pages.reconciliation;

import org.openqa.selenium.WebDriver;

import com.oss.pages.BasePage;

public class NetworkInconsistenciesViewPage extends BasePage {

    public static NetworkInconsistenciesViewPage goToNetworkInconsistenciesViewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/reco/network-repository-view/discrepancies" +
                "?perspective=NETWORK", basicURL));
        return new NetworkInconsistenciesViewPage(driver);
    }

    protected NetworkInconsistenciesViewPage(WebDriver driver) {
        super(driver);
    }
}
