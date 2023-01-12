package com.oss.pages.platform;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.mainheader.LoginPanel;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.SWITCHER;

@Deprecated
public class LoginPanelPage extends BasePage {

    public LoginPanelPage(WebDriver driver) {
        super(driver);
    }

    private static final String ALPHA_MODE_SWITCHER_ID = "alpha-mode-switcher";

    public void changeLanguageForEnglish() {
        LoginPanel.create(driver, wait).chooseLanguage("English");
    }

    public LoginPanelPage changeSwitcherForAlphaMode() {
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep(200);
        getAlphaModeSwitcher().setSingleStringValue("true");
        return new LoginPanelPage(driver);
    }

    public LoginPanelPage changeSwitcherForNormalMode() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getAlphaModeSwitcher().setSingleStringValue("false");
        return new LoginPanelPage(driver);
    }

    private Input getAlphaModeSwitcher() {
        return ComponentFactory.create(ALPHA_MODE_SWITCHER_ID, SWITCHER, driver, wait);
    }


}
