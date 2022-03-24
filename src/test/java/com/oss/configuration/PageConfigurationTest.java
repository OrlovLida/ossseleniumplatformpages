package com.oss.configuration;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.pages.platform.configuration.SaveConfigurationWizard;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.ConnectionsViewPage;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

import static com.oss.pages.platform.configuration.SaveConfigurationWizard.Property.*;

@Listeners({TestListener.class})
public class PageConfigurationTest extends BaseTestCase {

    private NewInventoryViewPage newInventoryViewPage;

    private final static String GROUP_NAME = "SeleniumTests";
    private final static String CONFIGURATION_NAME_IV = "IV";
    private final static String CONFIGURATION_NAME_IV_USER = "IV_User";
    private final static String CONFIGURATION_NAME_IV_GROUP = "IV_Group";
    private final static String CONFIGURATION_NAME_HV = "HV_User";
    private final static String CONFIGURATION_NAME_CV = "CV_User";


    @BeforeClass
    public void goToInventoryView() {
        //given
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    @Description("Saving new configuration for Inventory View")
    public void saveConfigurationForIVPage() {
        //when
        newInventoryViewPage.setHorizontalLayout();
        newInventoryViewPage.savePageConfiguration(
                createField(NAME, CONFIGURATION_NAME_IV));

        //then
        assertSuccessMessage();
        // Assert.assertTrue(driver.getCurrentUrl().contains("Location"));
    }

    @Test(priority = 3)
    @Description("Saving new configuration for Inventory View page for user")
    public void saveConfigurationForIVPageForUser() {
        //when
        newInventoryViewPage
                .setVerticalLayout();
        newInventoryViewPage.saveNewPageConfiguration(
                        CONFIGURATION_NAME_IV_USER,
                        createField(DEFAULT_VIEW_FOR, "Me"));

        //then
        assertSuccessMessage();
        // Assert.assertTrue(driver.getCurrentUrl().contains("Location"));
    }

    @Test(priority = 5)
    @Description("Saving new configuration for for Inventory View page for group")
    public void saveConfigurationForIVPageForGroup() {
        //given
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Building");

        //when
        newInventoryViewPage
                .setHorizontalLayout();
        newInventoryViewPage
                .saveNewPageConfiguration(
                        CONFIGURATION_NAME_IV_GROUP,
                        createField(GROUPS, GROUP_NAME));

        //then
        assertSuccessMessage();
        //     Assert.assertTrue(driver.getCurrentUrl().contains("Building"));
    }

    @Test(priority = 6)
    @Description("Saving new configuration for Hierarchy View page for user")
    public void saveConfigurationForHVPage() {
        //given
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
        newInventoryViewPage
                .selectFirstRow()
                .goToHierarchyViewForSelectedObject();

        //when
        new HierarchyViewPage(driver)
                .saveNewPageConfiguration(CONFIGURATION_NAME_HV);

        //then
        assertSuccessMessage();
    }

    @Test(priority = 7)
    @Description("Saving new configuration for Connections View page for user")
    public void saveConfigurationForCVPage() {
        //given
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
        String idOfFirstObject = newInventoryViewPage.getIdOfMainTableObject(0);
        ConnectionsViewPage connectionsViewPage = ConnectionsViewPage.goToConnectionsViewPage(driver, BASIC_URL);

        //when
        //connectionsViewPage.saveNewPageConfiguration(CONFIGURATION_NAME_CV);

        //then
        assertSuccessMessage();
    }

    @Test(priority = 8)
    @Description("Open saved configuration")
    public void isConfigurationSavedProperly() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
        //when
        newInventoryViewPage
                .applyConfigurationForPage(CONFIGURATION_NAME_IV);

        //then
        Assert.assertEquals(newInventoryViewPage.isHorizontal(), 1);
        // Assert.assertTrue(driver.getCurrentUrl().contains("Location"));
    }

    @Test(priority = 9)
    @Description("Check configuration for second user")
    public void isConfigurationWorkingProperlyForSecondUser() {
        //given
        newInventoryViewPage.changeUser("webseleniumtests2", "webtests");

        //when
        newInventoryViewPage
                .applyConfigurationForPage(CONFIGURATION_NAME_IV_GROUP);

        //then
        Assert.assertEquals(newInventoryViewPage.isHorizontal(), 2);
        //     Assert.assertTrue(driver.getCurrentUrl().contains("Building"));
    }

    private SaveConfigurationWizard.Field createField(SaveConfigurationWizard.Property property, String... values) {
        return SaveConfigurationWizard.create(driver, webDriverWait).createField(property, values);
    }

    private void assertSuccessMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        systemMessage.close();
    }
}
