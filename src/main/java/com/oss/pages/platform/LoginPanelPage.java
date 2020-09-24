package com.oss.pages.platform;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.mainheader.ToolbarWidget;
import com.oss.framework.mainheader.UserSettings;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.SWITCHER;

public class LoginPanelPage extends BasePage {

    public LoginPanelPage(WebDriver driver){super(driver);}

    private String ALPHA_MODE_SWITCHER_ID = "alpha-mode-switcher";

    public void changeLanguageForEnglish(){
        UserSettings.create(driver, wait).chooseLanguage("English");
    }

    public LoginPanelPage changeSwitcherForAlphaMode(){
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep(3000);
        getAlphaModeSwitcher().setSingleStringValue("true");
        return new LoginPanelPage(driver);
    }

    public LoginPanelPage changeSwitcherForNormalMode(){
        DelayUtils.waitForPageToLoad(driver, wait);
        getAlphaModeSwitcher().setSingleStringValue("false");
        return new LoginPanelPage(driver);
    }

    public BasePage closeLoginPanel() {
        DelayUtils.waitForPageToLoad(driver, wait);
        ToolbarWidget.create(driver, wait).closeLoginPanel();
       return new BasePage(driver);
    }

    private Input getAlphaModeSwitcher(){
        return ComponentFactory.create(ALPHA_MODE_SWITCHER_ID, SWITCHER, driver, wait);
    }


}
