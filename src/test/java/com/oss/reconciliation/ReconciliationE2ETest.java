package com.oss.reconciliation;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;

public class ReconciliationE2ETest extends BaseTestCase {

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private String cmDomainName = "SeleniumTestDomain";

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
    }

    @Test(priority = 1)
    public void createCmDomain() {
        networkDiscoveryControlViewPage.createCmDomain(cmDomainName, "CISCO IOS 12/15", "IP");
    }

    @Test(priority = 2)
    public void runReconciliation() {
        DelayUtils.sleep(100);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        networkDiscoveryControlViewPage.runReconciliation();
        networkDiscoveryControlViewPage.waitForEndOfReco();
    }

    @Test(priority = 3)
    public void applyInconsistencies(){
//        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expantTree();
        networkInconsistenciesViewPage.assignLocation();
        networkInconsistenciesViewPage.applyInconsistencies();
    }

    @Test(priority = 4)
    public void deleteCmDomain() {
        NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        networkDiscoveryControlViewPage.deleteCmDomain(cmDomainName);
    }

}
