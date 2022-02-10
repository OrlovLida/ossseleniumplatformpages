/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.web;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.toolsmanager.CategoryWizard;
import com.oss.untils.FakeGenerator;

/**
 * @author Gabriela Zaranek
 */
public class NewNavigationTest extends BaseTestCase {
    //private static final String CATEGORY_NAME = "Selenium Test Navigation " + FakeGenerator.getIdNumber();
    private static final String CATEGORY_NAME = "Selenium Test Navigation 123-27-7901";
    private static final String DESCRIPTION = FakeGenerator.getCharacter(FakeGenerator.FilmTitle.LORD_OF_THE_RING);
    private static final String ICON_ID = "AI_CONTROL_DESK";
    private static final String CREATE_SUBCATEGORY_ID = "createSubcategoryButton26";
    private static final String MOVIES_SUBCATEGORY = "Movies";
    private static final String ACTORS_SUBCATEGORY = "Actors";
    ToolsManagerWindow toolsManagerWindow;
    
    @BeforeClass
    public void getToolsManager() {
        toolsManagerWindow = ToolsManagerWindow.create(driver, webDriverWait);
    }
    
    @Test (priority = 1)
    public void createCategory() {
        toolsManagerWindow.clickCreateCategory();
        CategoryWizard categoryWizard = CategoryWizard.create(driver, webDriverWait, "popup_container");
        categoryWizard.setName(CATEGORY_NAME);
        categoryWizard.setDescription(DESCRIPTION);
        categoryWizard.selectIcon(ICON_ID);
        categoryWizard.clickSave();
        Optional<SystemMessageContainer.Message> message = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage();
        Assertions.assertThat(message).isPresent();
        Assertions.assertThat(message.get().getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(message.get().getText()).contains(CATEGORY_NAME);
        DelayUtils.sleep(1000);
        Assertions.assertThat(toolsManagerWindow.getCategoriesName()).contains(CATEGORY_NAME);
        Assertions.assertThat(toolsManagerWindow.getCategoryDescription(CATEGORY_NAME)).isEqualTo(DESCRIPTION);
    }
    
    @Test (priority = 2)
    public void createSubcategories() {
        toolsManagerWindow.callAction(CATEGORY_NAME,CREATE_SUBCATEGORY_ID);
        CategoryWizard subcategory = CategoryWizard.create(driver, webDriverWait, "popup_container");
        subcategory.setName(MOVIES_SUBCATEGORY);
        subcategory.clickSave();
        SystemMessageInterface messages = SystemMessageContainer.create(driver, webDriverWait);
        Optional<SystemMessageContainer.Message> message1 = messages.getFirstMessage();
        messages.close();
        toolsManagerWindow.callAction(CATEGORY_NAME, CREATE_SUBCATEGORY_ID);
        CategoryWizard subcategory2 = CategoryWizard.create(driver, webDriverWait, "popup_container");
        subcategory2.setName(ACTORS_SUBCATEGORY);
        subcategory2.clickSave();
        Optional<SystemMessageContainer.Message> message2 = SystemMessageContainer.create(driver, webDriverWait).getFirstMessage();
        messages.close();
        DelayUtils.sleep(1000);
        Assertions.assertThat(message1).isPresent();
        Assertions.assertThat(message1.get().getText()).contains(MOVIES_SUBCATEGORY);
        Assertions.assertThat(message2).isPresent();
        Assertions.assertThat(message2.get().getText()).contains(ACTORS_SUBCATEGORY);

        List<String> subcategoriesNames = toolsManagerWindow.getSubcategoriesNames(CATEGORY_NAME);
        Assertions.assertThat(subcategoriesNames).contains(MOVIES_SUBCATEGORY).contains(ACTORS_SUBCATEGORY);
    }
    
    @Test
    public void addApplications() {
    }
    
    @Test
    public void changeOrderCategory() {
    }
    
    @Test
    public void changeOrderSubcategory() {
    }
    
    @Test
    public void changeOrderApplication() {
    }
    
    @Test
    public void addPolitics() {
    }
    
    @Test
    public void addQueryParam() {
        
    }
    
    @Test
    public void addPathParam() {
    }
}
