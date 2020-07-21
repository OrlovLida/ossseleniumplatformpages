package com.oss.reconciliation;

import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;

public class CreateCmDomainTest extends BaseTestCase {

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private String cmDomainName = "SeleniumTestDomain";

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkInconsistenciesViewPage(driver, BASIC_URL);
    }

    @Test(priority = 1)
    public void createCmDomain() {
        networkDiscoveryControlViewPage.createCmDomain(cmDomainName, "ALCATEL", "IP");
    }

    @Test(priority = 2)
    public void runReconciliation() {
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        Assertions.assertThat(networkDiscoveryControlViewPage.runReconciliation()).isTrue();
    }

    @Test(priority = 3)
    public void deleteCmDomain() {
//        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        networkDiscoveryControlViewPage.deleteCmDomain(cmDomainName);
    }

}
