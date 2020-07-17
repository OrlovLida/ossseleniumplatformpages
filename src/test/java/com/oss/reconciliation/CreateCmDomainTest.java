package com.oss.reconciliation;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;

public class CreateCmDomainTest extends BaseTestCase {

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private String cmDomainName = "SeleniumTestDomain";

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkInconsistenciesViewPage(driver, BASIC_URL);
    }

    @Test
    public void createCmDomain() {
        networkDiscoveryControlViewPage.useContextAction("CREATE", "Create CM Domain");
        new CmDomainWizardPage(driver).createCmDomain(cmDomainName, "ALCATEL", "IP");
    }

    @Test
    public void deleteCmDomain() {
        networkDiscoveryControlViewPage
                .getTreeView()
                .performSearchWithEnter(cmDomainName)
                .selectTreeRowByText(cmDomainName);
        networkDiscoveryControlViewPage.useContextAction("EDIT", "Delete CM Domain");
        new CmDomainWizardPage(driver).deleteCmDomain();
    }

}
