package com.oss.web;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.layout.Card;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.NewInventoryViewPage;

public class TabsWidgetTest extends BaseTestCase {
    private static final String PROPERTIES = "Properties";
    private static final String TEST_PERSON = "TestPerson";
    private static final String INTERESTS = "Interests";
    private static final String MOVIES = "Movies";
    private static final String TABLE_TYPE = " Table";
    private static final String DETAIL_TABS_CARD = "DetailTabsCard";
    private NewInventoryViewPage newInventoryViewPage;

    @BeforeClass
    public void goToInventoryView() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    public void displayTabs() {
        newInventoryViewPage.selectFirstRow();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(newInventoryViewPage.isTabVisible(PROPERTIES));
    }

    @Test(priority = 2)
    public void addTabs() {
        newInventoryViewPage.selectFirstRow();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.enableWidget(TABLE_TYPE, INTERESTS);
        newInventoryViewPage.enableWidget(TABLE_TYPE, MOVIES);
        Assert.assertTrue(newInventoryViewPage.isTabVisible(MOVIES));
        Assert.assertTrue(newInventoryViewPage.isTabVisible(INTERESTS));
    }

    @Test(priority = 3)
    public void changeTab() {
        newInventoryViewPage.selectObjectByRowId(0);

        String activeTabLabel = newInventoryViewPage.getActiveTabLabel();
        String secondTabLabel = newInventoryViewPage.getTabLabel(1);
        Assertions.assertThat(activeTabLabel).isNotEqualTo(secondTabLabel);

        newInventoryViewPage.selectTabByLabel(secondTabLabel);
        String newActiveLabel = newInventoryViewPage.getActiveTabLabel();
        Assertions.assertThat(newActiveLabel).isEqualTo(secondTabLabel);
    }

    @Test(priority = 4)
    public void changeTabsOrder() {
        String firstTab = newInventoryViewPage.getTabLabel(0);
        newInventoryViewPage.changeTabsOrder(firstTab, 2);
        String thirdTabAfterChange = newInventoryViewPage.getTabLabel(2);
        Assert.assertEquals(firstTab, thirdTabAfterChange);
    }

    @Test(priority = 5)
    public void removeTab() {
        newInventoryViewPage.disableWidget(INTERESTS);
        Assert.assertFalse(newInventoryViewPage.isTabVisible(INTERESTS));
    }


    @Test(priority = 6, enabled = false)
    public void displayTabsForMultiselection() {
        newInventoryViewPage.enableWidget(TABLE_TYPE, MOVIES);
        newInventoryViewPage.selectObjectByRowId(2);
        Assert.assertFalse(newInventoryViewPage.isTabVisible(MOVIES));
    }

    @Test(priority = 7)
    public void maximizeAndMinimizeTabsWidget() {
        Card tabsCard = Card.createCard(driver, webDriverWait, DETAIL_TABS_CARD);
        tabsCard.maximizeCard();
        Assertions.assertThat(tabsCard.isCardMaximized()).isTrue();
        tabsCard.minimizeCard();
        Assertions.assertThat(tabsCard.isCardMaximized()).isFalse();
    }

}
