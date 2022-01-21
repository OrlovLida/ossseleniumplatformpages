package com.oss.pages.platform;

import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

public class PerspectiveChooserPage extends BasePage {

    public PerspectiveChooserPage(WebDriver driver){super(driver);}

    public BasePage setLivePerspective(){
        PerspectiveChooser.create(driver, wait).setLivePerspective();
        return this;
    }

    public BasePage setNetworkPerspective() {
        PerspectiveChooser.create(driver, wait).setNetworkPerspective();
        return this;
    }

    public BasePage setPlanPerspective(String processCodeOrName) {
        PerspectiveChooser.create(driver, wait).setPlanPerspective(processCodeOrName);
        return this;
    }

    public BasePage setPlanDatePerspective(String date) {
        PerspectiveChooser.create(driver, wait).setPlanDatePerspective(date);
        return this;
    }

    public BasePage setWithoutRemove() {
        PerspectiveChooser.create(driver, wait).setWithoutRemoved();
        return this;
    }

    public BasePage setWithRemove() {
        PerspectiveChooser.create(driver, wait).setWithRemove();
        return this;
    }

    public BasePage close(){
        ToolbarWidget.create(driver, wait).closeQueryContextContainer();
        return this;
    }
}
