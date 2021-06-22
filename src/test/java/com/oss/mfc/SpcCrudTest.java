package com.oss.mfc;

import java.time.LocalDateTime;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.mfc.spc.SpcNewInventoryViewPage;
import com.oss.pages.mfc.spc.SpcPropertyPanel;
import com.oss.pages.mfc.spc.SpcWizardPage.Format;
import com.oss.pages.mfc.spc.SpcWizardPage.Length;
import com.oss.pages.mfc.spc.SpcWizardPage.NetworkIdentifier;
import com.oss.repositories.LogicalFunctionRepository;
import com.oss.untils.Environment;

import io.qameta.allure.Description;

import static org.assertj.core.api.Assertions.assertThat;

public class SpcCrudTest extends BaseTestCase {

    private static final String SPC_TEXT;
    private static final String UPDATED_SPC_TEXT;
    private static final boolean IS_ADDITIONAL = false;
    private static final Length LENGTH = Length.ANSI;
    private static final NetworkIdentifier NETWORK_IDENTIFIER = NetworkIdentifier.NAT_R;
    private static final Format FORMAT = Format.DECIMAL;
    private static final int SPC_VALUE = 164567;

    static {
        LocalDateTime now = LocalDateTime.now();
        SPC_TEXT = "SPC Selenium CRUD Test. Date " + now;
        UPDATED_SPC_TEXT = "SPC Selenium CRUD Test after update. Date " + now;
    }

    @BeforeClass
    void init() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
    }

    @Test(priority = 1)
    @Description("Open SPC new IV")
    void openSpcNiv() {
        homePage.setNewObjectType("Signalling Point Code");
    }

    @Test(priority = 2)
    @Description("Create SPC")
    void createSpc() {
        SpcNewInventoryViewPage.create(driver, webDriverWait)
            .openCreateSpcWizard()
            .setDescription(SPC_TEXT)
            .setName(SPC_TEXT)
            .setIsAdditional(IS_ADDITIONAL)
            .setLength(LENGTH)
            .setNetworkIdentifier(NETWORK_IDENTIFIER)
            .setFormat(FORMAT)
            .setSpcValue(SPC_VALUE)
            .submit();
    }

    @Test(priority = 3)
    @Description("Check created SPC")
    void checkCreatedSPC() {
        checkSpc(SPC_TEXT);
    }

    @Test(priority = 4)
    @Description("Update SPC")
    public void updateSpc() {
        SpcNewInventoryViewPage.create(driver, webDriverWait)
            .openModifySpcWizardForSelectedSpc()
            .setDescription(UPDATED_SPC_TEXT)
            .setName(UPDATED_SPC_TEXT)
            .submit();
    }

    @Test(priority = 5)
    @Description("Check updated SPC")
    void checkUpdatedSpc() {
        checkSpc(UPDATED_SPC_TEXT);
    }

    @Test(priority = 6)
    @Description("Delete SPC")
    void deleteSpc() {
        SpcNewInventoryViewPage.create(driver, webDriverWait)
            .searchAndSelect(UPDATED_SPC_TEXT)
            .openDeleteSpcWizardForSelectedSpc()
            .accept();
    }

    @Test(priority = 7)
    @Description("Check deleted SPC")
    void checkDeletedSpc() {
        TableWidget mainTable = SpcNewInventoryViewPage.create(driver, webDriverWait)
                                    .searchObject(UPDATED_SPC_TEXT)
                                    .getMainTable();
        assertThat(mainTable.hasNoData()).isTrue();
    }

    private void checkSpc(String name) {
        SpcPropertyPanel propertyPanel = SpcNewInventoryViewPage.create(driver, webDriverWait)
                                             .searchAndSelect(name)
                                             .getPropetiesPanelForSelectedSpc();
        assertThat(propertyPanel.length()).isEqualTo(LENGTH);
        assertThat(propertyPanel.format()).isEqualTo(Format.F_8_8_8);
        assertThat(propertyPanel.decimalValue()).isEqualTo(SPC_VALUE);
        assertThat(propertyPanel.isAdditional()).isEqualTo(IS_ADDITIONAL);
        assertThat(propertyPanel.networkIdentifier()).isEqualTo(NETWORK_IDENTIFIER);
        assertThat(propertyPanel.name()).isEqualTo(name);
    }

}
