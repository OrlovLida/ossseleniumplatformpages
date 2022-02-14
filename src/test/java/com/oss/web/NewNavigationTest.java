/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.toolsmanager.ApplicationWizard;
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
    private static final String ADD_APPLICATION = "addTileButton0";
    private static final String APPLICATION_NAME = "All Test Movies";
    private static final String APPLICATION_NAME_2 = "Test Movies";
    private static final String APPLICATION_TYPE = "Inventory View";
    private static final String EDIT_APPLICATION_BUTTON = "editButton0";
    private static final String APPLICATION_NAME_2_UPDATE = "Best Movies" ;
    private ToolsManagerWindow toolsManagerWindow;
    
    @BeforeClass
    public void getToolsManager() {

        toolsManagerWindow = ToolsManagerWindow.create(driver, webDriverWait);
    }
    private void goToHomePage(){
        driver.get(String.format("%s/#/", BASIC_URL));
    }
    
    @Test (priority = 1)
    public void createCategory() {
        toolsManagerWindow.clickCreateCategory();
        CategoryWizard categoryWizard = CategoryWizard.create(driver, webDriverWait);
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
        CategoryWizard subcategory = CategoryWizard.create(driver, webDriverWait);
        subcategory.setName(MOVIES_SUBCATEGORY);
        subcategory.clickSave();
        SystemMessageInterface messages = SystemMessageContainer.create(driver, webDriverWait);
        Optional<SystemMessageContainer.Message> message1 = messages.getFirstMessage();
        messages.close();
        toolsManagerWindow.callAction(CATEGORY_NAME, CREATE_SUBCATEGORY_ID);
        CategoryWizard subcategory2 = CategoryWizard.create(driver, webDriverWait);
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
        toolsManagerWindow.callActionSubcategory(MOVIES_SUBCATEGORY, CATEGORY_NAME, ADD_APPLICATION);
        ApplicationWizard applicationWizard = ApplicationWizard.create(driver, webDriverWait);
        applicationWizard.addApplication(APPLICATION_TYPE, APPLICATION_NAME);
        SystemMessageInterface messages = SystemMessageContainer.create(driver, webDriverWait);
        Optional<SystemMessageContainer.Message> message1 = messages.getFirstMessage();
        messages.close();
        applicationWizard.addApplication(APPLICATION_TYPE, APPLICATION_NAME);
        Assertions.assertThat(message1).isPresent();
        Assertions.assertThat(message1.get().getText()).isEqualTo("You've just created an application "+APPLICATION_NAME+" in "+MOVIES_SUBCATEGORY);
        DelayUtils.sleep(1000);
        Assertions.assertThat(toolsManagerWindow.getApplicationNames(CATEGORY_NAME)).contains(APPLICATION_NAME);
        Assertions.assertThat(toolsManagerWindow.getApplicationURL(APPLICATION_NAME)).contains("#/views/management/views/inventory-view");
    }
    
    @Test
    public void changeOrderCategory() {
        int categoryPosition = toolsManagerWindow.getCategoriesName().indexOf(CATEGORY_NAME);
        toolsManagerWindow.changeCategoryOrder(CATEGORY_NAME, categoryPosition-2);
        int newPosition = toolsManagerWindow.getCategoriesName().indexOf(CATEGORY_NAME);
        Assertions.assertThat(newPosition).isNotEqualTo(categoryPosition).isEqualTo(categoryPosition-2);
    }
    
    @Test
    public void changeOrderSubcategory() {
        int placeOfActors = toolsManagerWindow.getPlaceOfSubcategory(CATEGORY_NAME, ACTORS_SUBCATEGORY);
        toolsManagerWindow.changeSubcategoryOrder(CATEGORY_NAME,MOVIES_SUBCATEGORY, placeOfActors);
        int newPosition = toolsManagerWindow.getPlaceOfSubcategory(CATEGORY_NAME, MOVIES_SUBCATEGORY);
        Assertions.assertThat(newPosition).isEqualTo(placeOfActors);
    }
    
    @Test
    public void changeOrderApplication() {
        int appPosition = toolsManagerWindow.getApplicationNames(CATEGORY_NAME).indexOf(APPLICATION_NAME_2);
        toolsManagerWindow.changeApplicationOrder(CATEGORY_NAME,APPLICATION_NAME,appPosition);
        int newPosition = toolsManagerWindow.getApplicationNames(CATEGORY_NAME).indexOf(APPLICATION_NAME);
        Assertions.assertThat(newPosition).isEqualTo(appPosition);
    }
    @Test
    public void addPathParam() {
        toolsManagerWindow.callActionApplication(APPLICATION_NAME_2,CATEGORY_NAME, EDIT_APPLICATION_BUTTON);
        ApplicationWizard applicationWizard = ApplicationWizard.create(driver, webDriverWait);
        applicationWizard.addPathParam("TestMovie");
        applicationWizard.clickSave();
        toolsManagerWindow.openApplication(CATEGORY_NAME, APPLICATION_NAME_2);
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        Assertions.assertThat(driver.getCurrentUrl()).contains("TestMovie");
    }


    @Test
    public void addQueryParam() {
        toolsManagerWindow.callActionApplication(APPLICATION_NAME_2,CATEGORY_NAME, EDIT_APPLICATION_BUTTON);
        ApplicationWizard applicationWizard = ApplicationWizard.create(driver, webDriverWait);
        applicationWizard.addQueryParam("query", "rating=='10'");
        applicationWizard.clickSave();
        toolsManagerWindow.openApplication(CATEGORY_NAME, APPLICATION_NAME_2);
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        ArrayList<String> ratingValues = getValuesFromColumn("rating");
        ratingValues.forEach(value -> Assertions.assertThat(value).isEqualTo("10"));
    }

    @Test
    public void editApplication() {
        toolsManagerWindow.callActionApplication(APPLICATION_NAME_2, CATEGORY_NAME, EDIT_APPLICATION_BUTTON);
        ApplicationWizard applicationWizard = ApplicationWizard.create(driver, webDriverWait);
        applicationWizard.setApplicationName(APPLICATION_NAME_2_UPDATE);
        List<String> applicationNames = toolsManagerWindow.getApplicationNames(CATEGORY_NAME);
        Assertions.assertThat(applicationNames).contains(APPLICATION_NAME_2_UPDATE);
    }

    @Test
    public void addPolitics() {
    }

    private ArrayList<String> getValuesFromColumn(String columnId) {
        ArrayList<String> attributeValues = new ArrayList<>();
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        TableWidget tableWidget = newInventoryViewPage.getMainTable();

        int rowsNumber = tableWidget.getRowsNumber();
        for (int i = 0; i < rowsNumber; i++) {
            attributeValues.add(tableWidget.getCellValue(i, columnId));
        }

        return attributeValues;
    }

}
