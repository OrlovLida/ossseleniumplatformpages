package com.oss;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.tree.TreeComponent.Node;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HierarchyViewPage;

public class HierarchyViewTest extends BaseTestCase {
    private HierarchyViewPage hierarchyViewPage;

    @BeforeClass
    public void goToHierarchyViewPage() {
        hierarchyViewPage = HierarchyViewPage.openHierarchyViewPage(driver, BASIC_URL, "PhysicalDevice");
    }

    @Test (priority = 1)
    public void selectFirstNode() {
        hierarchyViewPage.selectFirstNode();

        Assertions.assertThat(hierarchyViewPage
                .getFirstNode().isToggled()).isTrue();
    }

    @Test (priority = 2)
    public void expandNode() {
        hierarchyViewPage.getFirstNode().expandNode();
        Assertions.assertThat(hierarchyViewPage.getFirstNode().isExpanded()).isTrue();
    }

    @Test (priority = 3)
    public void searchWithNotExistingData() {
        hierarchyViewPage.searchObject("hjakserzxaseer");
        List<Node> nodes = hierarchyViewPage.getMainTree().getVisibleNodes();

        Assertions.assertThat(nodes).hasSize(0);

        hierarchyViewPage.clearFiltersOnMainTree();
    }

    @Test (priority = 4)
    public void searchWithExistingData() {
        Node node = hierarchyViewPage.getFirstNode();
        String label = node.getLabel();

        hierarchyViewPage.searchObject(label);
        List<Node> nodes = hierarchyViewPage.getMainTree().getVisibleNodes();

        Assertions.assertThat(nodes).hasSize(1);
        Assertions.assertThat(nodes.get(0).getLabel()).isEqualTo(label);

        hierarchyViewPage.clearFiltersOnMainTree();

    }

    @Test (priority = 5)
    public void showOnHierarchyView() {
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        hierarchyViewPage.getFirstNode().callAction(ActionsContainer.SHOW_ON_GROUP_ID, HierarchyViewPage.OPEN_HIERARCHY_VIEW_CONTEXT_ACTION_ID);

        List<Node> nodes = hierarchyViewPage.getMainTree().getVisibleNodes();
        Assertions.assertThat(nodes).hasSize(1);
    }
}
