package com.oss.pages.servicedesk;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;

import io.qameta.allure.Step;

public class ServiceDeskMenuPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(ServiceDeskMenuPage.class);

    public ServiceDeskMenuPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public ServiceDeskMenuPage openMainPage(String url) {
        openPage(driver, url);
        return new ServiceDeskMenuPage(driver, wait);
    }

    @Step("Go to {menuPage} from Trouble Tickets Left Side Menu")
    public void chooseFromTroubleTicketsMenu(String menuPage) {
        chooseFromLeftSideMenu(menuPage, "Incident Management");
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Going to {} from Left Side Menu", menuPage);
    }

    @Step("Go to {menuPage} from Problem Management Left Side Menu")
    public void chooseFromProblemManagementMenu(String menuPage) {
        chooseFromLeftSideMenu(menuPage, "Problem Management");
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Going to {} from Left Side Menu", menuPage);
    }
}
