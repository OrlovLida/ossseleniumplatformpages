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
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.prompts.Popup;
import com.oss.framework.navigation.toolsmanager.Application;
import com.oss.framework.navigation.toolsmanager.Subcategory;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.toolsmanager.ApplicationWizard;
import com.oss.pages.platform.toolsmanager.CategoryWizard;
import com.oss.pages.platform.toolsmanager.ToolsManagerPage;
import com.oss.untils.FakeGenerator;

import io.qameta.allure.Description;

/**
 * @author Gabriela Zaranek
 */
public class NewNavigationTest extends BaseTestCase {
    // private static final String CATEGORY_NAME = "Selenium Test Navigation " + FakeGenerator.getIdNumber();
    private static final String CATEGORY_NAME = "Selenium Test Navigation 123-27-7901";
    private static final String CATEGORY_NAME_UPDATE = CATEGORY_NAME + "Update";
    private static final String DESCRIPTION = FakeGenerator.getCharacter(FakeGenerator.FilmTitle.LORD_OF_THE_RING);
    private static final String ICON_ID = "AI_CONTROL_DESK";
    private static final String CREATE_SUBCATEGORY_ID = "createSubcategoryButton24";
    private static final String MOVIES_SUBCATEGORY = "Movies";
    private static final String ACTORS_SUBCATEGORY = "Actors";
    private static final String ACTOR_SUBCATEGORY_UPDATE = ACTORS_SUBCATEGORY + " Update";
    private static final String ADD_APPLICATION = "addTileButton1";
    private static final String APPLICATION_NAME = "All Test Movies";
    private static final String APPLICATION_NAME_2 = "Test Movies";
    private static final String APPLICATION_TYPE = "Inventory View";
    private static final String EDIT_BUTTON = "editButton0";
    private static final String APPLICATION_NAME_2_UPDATE = "Best Movies";
    private static final String EDIT_CATEGORY_BUTTON = "editCategoryButton1";
    private static final String DELETE_APPLICATION_ID = "deleteCategoryButton0";
    private ToolsManagerWindow toolsManagerWindow;
    
    @BeforeClass
    public void getToolsManager() {
        ToolsManagerPage toolsManagerPage = new ToolsManagerPage(driver);
        toolsManagerWindow = toolsManagerPage.getToolsManager();
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
    }
    
    @Test(priority = 1)
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
        toolsManagerWindow.waitForNavigationElement(CATEGORY_NAME);
        Assertions.assertThat(toolsManagerWindow.getCategoriesName()).contains(CATEGORY_NAME);
        Assertions.assertThat(toolsManagerWindow.getCategoryDescription(CATEGORY_NAME)).isEqualTo(DESCRIPTION);
    }
    
    @Test(priority = 2)
    public void createSubcategories() {
        toolsManagerWindow.callAction(CATEGORY_NAME, CREATE_SUBCATEGORY_ID);
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
        Assertions.assertThat(message1).isPresent();
        Assertions.assertThat(message1.get().getText()).contains(MOVIES_SUBCATEGORY);
        Assertions.assertThat(message2).isPresent();
        Assertions.assertThat(message2.get().getText()).contains(ACTORS_SUBCATEGORY);
        toolsManagerWindow.waitForNavigationElement(MOVIES_SUBCATEGORY);
        toolsManagerWindow.waitForNavigationElement(ACTORS_SUBCATEGORY);
        
        List<String> subcategoriesNames = toolsManagerWindow.getSubcategoriesNames(CATEGORY_NAME);
        Assertions.assertThat(subcategoriesNames).contains(MOVIES_SUBCATEGORY).contains(ACTORS_SUBCATEGORY);
    }
    
    @Test
    public void addApplications() {
        toolsManagerWindow.callActionSubcategory(MOVIES_SUBCATEGORY, CATEGORY_NAME, ADD_APPLICATION);
        addApplication(APPLICATION_NAME);
        SystemMessageInterface messages = SystemMessageContainer.create(driver, webDriverWait);
        Optional<SystemMessageContainer.Message> message1 = messages.getFirstMessage();
        messages.close();
        toolsManagerWindow.callActionSubcategory(MOVIES_SUBCATEGORY, CATEGORY_NAME, ADD_APPLICATION);
        addApplication(APPLICATION_NAME_2);
        Assertions.assertThat(message1).isPresent();
        Assertions.assertThat(message1.get().getText())
                .isEqualTo("You've just created an application " + APPLICATION_NAME + " in " + MOVIES_SUBCATEGORY);
        toolsManagerWindow.waitForNavigationElement(APPLICATION_NAME_2);
        Assertions.assertThat(toolsManagerWindow.getApplicationNames(CATEGORY_NAME)).contains(APPLICATION_NAME);
        Assertions.assertThat(toolsManagerWindow.getApplicationURL(APPLICATION_NAME)).contains("#/views/management/views/inventory-view");
    }
    
    @Test
    public void changeOrderCategory() {
        int categoryPosition = toolsManagerWindow.getCategoriesName().indexOf(CATEGORY_NAME);
        toolsManagerWindow.changeCategoryOrder(CATEGORY_NAME, categoryPosition - 2);
        int newPosition = toolsManagerWindow.getCategoriesName().indexOf(CATEGORY_NAME);
        Assertions.assertThat(newPosition).isNotEqualTo(categoryPosition).isEqualTo(categoryPosition - 2);
    }
    
    @Test
    public void changeOrderSubcategory() {
        int placeOfActors = toolsManagerWindow.getPlaceOfSubcategory(CATEGORY_NAME, ACTORS_SUBCATEGORY);
        toolsManagerWindow.changeSubcategoryOrder(CATEGORY_NAME, MOVIES_SUBCATEGORY, placeOfActors);
        int newPosition = toolsManagerWindow.getPlaceOfSubcategory(CATEGORY_NAME, MOVIES_SUBCATEGORY);
        Assertions.assertThat(newPosition).isEqualTo(placeOfActors);
    }
    
    @Test
    public void changeOrderApplication() {
        int appPosition = toolsManagerWindow.getApplicationNames(CATEGORY_NAME).indexOf(APPLICATION_NAME_2);
        toolsManagerWindow.changeApplicationOrder(CATEGORY_NAME, APPLICATION_NAME, appPosition);
        int newPosition = toolsManagerWindow.getApplicationNames(CATEGORY_NAME).indexOf(APPLICATION_NAME);
        Assertions.assertThat(newPosition).isEqualTo(appPosition);
    }
    
    @Test
    public void addPathParam() {
        ApplicationWizard applicationWizard = openApplicationWizard();
        applicationWizard.addPathParam("TestMovie");
        applicationWizard.clickSave();
        toolsManagerWindow.openApplication(CATEGORY_NAME, APPLICATION_NAME_2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assertions.assertThat(driver.getCurrentUrl()).contains("TestMovie");
    }
    
    @Test
    public void addQueryParam() {
        ApplicationWizard applicationWizard = openApplicationWizard();
        applicationWizard.addQueryParam("query", "rating=='10'");
        applicationWizard.clickSave();
        toolsManagerWindow.openApplication(CATEGORY_NAME, APPLICATION_NAME_2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ArrayList<String> ratingValues = getValuesFromColumn("rating");
        ratingValues.forEach(value -> Assertions.assertThat(value).isEqualTo("10"));
    }
    
    @Test
    public void editApplication() {
        ApplicationWizard applicationWizard = openApplicationWizard();
        applicationWizard.setApplicationName(APPLICATION_NAME_2_UPDATE);
        List<String> applicationNames = toolsManagerWindow.getApplicationNames(CATEGORY_NAME);
        Assertions.assertThat(applicationNames).contains(APPLICATION_NAME_2_UPDATE);
    }
    
    @Test
    public void addPolitics() {
        ApplicationWizard applicationWizard = openApplicationWizard();
        applicationWizard.togglePolicies("true");
        Assertions.assertThat(applicationWizard.isPoliciesSet()).isTrue();
        applicationWizard.setPolicies("PhysicalDevice", "CREATE");
        applicationWizard.clickSave();
        ToolsManagerPage toolsManagerPage = new ToolsManagerPage(driver);
        toolsManagerPage.changeUser("webseleniumtests2", "oss");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        toolsManagerWindow = toolsManagerPage.getToolsManager();
        List<String> applicationNames = toolsManagerWindow.getApplicationNames(CATEGORY_NAME);
        Assertions.assertThat(applicationNames).doesNotContain(APPLICATION_NAME_2_UPDATE).contains(APPLICATION_NAME);
    }
    
    @Test
    public void editSubcategoryName() {
        ToolsManagerPage toolsManagerPage = new ToolsManagerPage(driver);
        toolsManagerPage.changeUser("webseleniumtests", "Webtests123!");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        toolsManagerWindow = toolsManagerPage.getToolsManager();
        toolsManagerWindow.callActionSubcategory(CATEGORY_NAME, ACTORS_SUBCATEGORY, EDIT_BUTTON);
        CategoryWizard categoryWizard = CategoryWizard.create(driver, webDriverWait);
        categoryWizard.setName(ACTOR_SUBCATEGORY_UPDATE);
        categoryWizard.clickSave();
        DelayUtils.sleep(1000);
        List<String> subcategoriesNames = toolsManagerWindow.getSubcategoriesNames(CATEGORY_NAME);
        Assertions.assertThat(subcategoriesNames).doesNotContain(ACTORS_SUBCATEGORY).contains(ACTOR_SUBCATEGORY_UPDATE);
    }
    
    @Test
    public void editCategoryName() {
        toolsManagerWindow.callAction(CATEGORY_NAME, EDIT_CATEGORY_BUTTON);
        CategoryWizard categoryWizard = CategoryWizard.create(driver, webDriverWait);
        categoryWizard.setName(CATEGORY_NAME_UPDATE);
        categoryWizard.clickSave();
        DelayUtils.sleep(1000);
        List<String> categoriesName = toolsManagerWindow.getCategoriesName();
        Assertions.assertThat(categoriesName).contains(CATEGORY_NAME_UPDATE).doesNotContain(CATEGORY_NAME);
    }

    @Test
    @Description ("Set Favorites Application and check show ony favourites")
    public void setFavoriteApplication() {
        Application application = toolsManagerWindow.getApplication(APPLICATION_NAME_2, CATEGORY_NAME);
        application.markAsFavorite();
        Assertions.assertThat(application.isFavorite()).isTrue();
        toolsManagerWindow.setShowOnlyFavourites();
        Assertions.assertThat(toolsManagerWindow.getApplicationNames()).contains(APPLICATION_NAME_2);
    }

    @Test
    @Description ("Set Favorites Subcategory and check show ony favourites")
    public void setFavoriteSubcategory() {
        Subcategory subcategoryMovie = toolsManagerWindow.getSubcategoryByName(MOVIES_SUBCATEGORY, CATEGORY_NAME);
        subcategoryMovie.markAsFavorite();
        Assertions.assertThat(subcategoryMovie.isFavorite()).isTrue();
        Assertions.assertThat(subcategoryMovie.getBadge()).isEqualTo("2/2");
        List<Application> applications = subcategoryMovie.getApplications();
        applications.forEach(application -> Assertions.assertThat(application.isFavorite()).isTrue());
        toolsManagerWindow.setShowOnlyFavourites();
        List<String> applicationNames = applications.stream().map(Application::getApplicationName).collect(Collectors.toList());
        Assertions.assertThat(toolsManagerWindow.getApplicationNames()).containsAll(applicationNames);
    }

    @Test
    @Description(value = "Delete Application" + APPLICATION_NAME_2)
    public void deleteApplication(){
        toolsManagerWindow.callActionApplication(APPLICATION_NAME_2, CATEGORY_NAME, DELETE_APPLICATION_ID);
        Popup.create(driver,webDriverWait).clickButtonByLabel("Delete");
        List<String> applicationNames = toolsManagerWindow.getApplicationNames(CATEGORY_NAME_UPDATE);
        Assertions.assertThat(applicationNames).doesNotContain(APPLICATION_NAME_2).isNotEmpty();
    }

    @Test
    @Description ("Check if Subcategory is not visible for user without configuration role ")
    public void checkPoliticsNotVisibleSubcategory() {
    }

    @Test
    @Description ("Check if Category is not visible for user without configuration role ")
    public void checkPoliticsNotVisibleCategory() {
    }

    private ApplicationWizard openApplicationWizard() {
        toolsManagerWindow.callActionApplication(APPLICATION_NAME_2, CATEGORY_NAME, EDIT_BUTTON);
        return ApplicationWizard.create(driver, webDriverWait);
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
    private void addApplication(String name){
        ApplicationWizard applicationWizard = ApplicationWizard.create(driver, webDriverWait);
        applicationWizard.addApplication(APPLICATION_TYPE, name);
    }
    
}
