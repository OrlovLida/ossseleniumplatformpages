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
import com.oss.framework.navigation.sidemenu.SideMenu;
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
    private static final String CATEGORY_NAME = "Selenium Test Navigation " + FakeGenerator.getIdNumber();
    //private static final String CATEGORY_NAME = "Selenium Test Navigation 804-45-3515";
    private static final String CATEGORY_NAME_UPDATE = CATEGORY_NAME + " Update";
    private static final String DESCRIPTION = FakeGenerator.getCharacter(FakeGenerator.FilmTitle.LORD_OF_THE_RING);
    private static final String ICON_ID = "AI_CONTROL_DESK";
    private static final String CREATE_SUBCATEGORY_ID = "createSubcategoryButton";
    private static final String MOVIES_SUBCATEGORY = "Movies";
    private static final String ACTORS_SUBCATEGORY = "Actors";
    private static final String ACTOR_SUBCATEGORY_UPDATE = ACTORS_SUBCATEGORY + " Update";
    private static final String ADD_APPLICATION = "addTileButton";
    private static final String APPLICATION_NAME = "All Test Movies";
    private static final String APPLICATION_NAME_2 = "Test Movies";
    private static final String APPLICATION_NAME_3 = "Actors";
    private static final String APPLICATION_TYPE = "Inventory View";
    private static final String EDIT_BUTTON = "editButton";
    private static final String APPLICATION_NAME_2_UPDATE = "Best Movies";
    private static final String EDIT_CATEGORY_BUTTON = "editCategoryButton";
    private static final String DELETE_APPLICATION_ID = "deleteCategoryButton";
    private static final String USER_1 = "webseleniumtests";
    private static final String PASSWORD_1 = "Webtests123!";
    private static final String USER_2 = "webseleniumtests2";
    private static final String PASSWORD_2 = "oss";
    private static final String DELETE_CATEGORY_BUTTON_ID = "deleteCategoryButton";
    private static final String PHYSICAL_CONNECTION_TYPE = "Physical Connection";
    private static final String MODIFY_ROLE = "MODIFY";
    private static final String TEST_MOVIE_TYPE = "TestMovie";
    private static final String INVENTORY_VIEW_LINK = "#/views/management/views/inventory-view";
    private static final String LINK_IS_NOT_AVAILABLE_EXCEPTION = "Link is not available";
    private static final String DELETE_BUTTON = "Delete";
    private static final String FAVOURITES_MENU_CATEGORY = "Favourites";
    private static final String TOOLS_MENU_CATEGORY = "Tools";

    private ToolsManagerWindow toolsManagerWindow;
    
    @BeforeClass
    public void getToolsManager() {
        ToolsManagerPage toolsManagerPage = new ToolsManagerPage(driver);
        toolsManagerWindow = toolsManagerPage.getToolsManager();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
    
    @Test(priority = 1)
    public void createCategory() {
        toolsManagerWindow.clickCreateCategory();
        CategoryWizard categoryWizard = CategoryWizard.create(driver, webDriverWait);
        categoryWizard.setName(CATEGORY_NAME);
        categoryWizard.setDescription(DESCRIPTION);
        categoryWizard.selectIcon(ICON_ID);
        categoryWizard.clickSave();
        SystemMessageInterface messages = SystemMessageContainer.create(driver, webDriverWait);
        Optional<SystemMessageContainer.Message> message = messages.getFirstMessage();
        Assertions.assertThat(message).isPresent();
        Assertions.assertThat(message.get().getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(message.get().getText()).contains(CATEGORY_NAME);
        messages.close();
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
        toolsManagerWindow.getCategoryByName(CATEGORY_NAME).expandCategory();
        toolsManagerWindow.waitForNavigationElement(MOVIES_SUBCATEGORY);
        toolsManagerWindow.waitForNavigationElement(ACTORS_SUBCATEGORY);
        
        List<String> subcategoriesNames = toolsManagerWindow.getSubcategoriesNames(CATEGORY_NAME);
        Assertions.assertThat(subcategoriesNames).contains(MOVIES_SUBCATEGORY).contains(ACTORS_SUBCATEGORY);
    }
    
    @Test(priority = 3)
    public void addApplications() {
        toolsManagerWindow.callActionSubcategory(MOVIES_SUBCATEGORY, CATEGORY_NAME, ADD_APPLICATION);
        addApplication(APPLICATION_NAME);
        SystemMessageInterface messages = SystemMessageContainer.create(driver, webDriverWait);
        Optional<SystemMessageContainer.Message> message1 = messages.getFirstMessage();
        messages.close();
        toolsManagerWindow.waitForNavigationElement(APPLICATION_NAME);
        toolsManagerWindow.callActionSubcategory(MOVIES_SUBCATEGORY, CATEGORY_NAME, ADD_APPLICATION);
        addApplication(APPLICATION_NAME_2);
        Assertions.assertThat(message1).isPresent();
        Assertions.assertThat(message1.get().getText())
                .isEqualTo("You've just created an application " + APPLICATION_NAME + " in " + MOVIES_SUBCATEGORY);
        toolsManagerWindow.waitForNavigationElement(APPLICATION_NAME_2);
        
        Assertions.assertThat(toolsManagerWindow.getApplicationNames(CATEGORY_NAME)).contains(APPLICATION_NAME);
        Assertions.assertThat(toolsManagerWindow.getApplicationURL(APPLICATION_NAME).orElse(LINK_IS_NOT_AVAILABLE_EXCEPTION))
                .contains(INVENTORY_VIEW_LINK);
        
    }
    
    @Test(priority = 4)
    public void changeOrderCategory() {
        toolsManagerWindow.getCategoryByName(CATEGORY_NAME).collapseCategory();
        int categoryPosition = toolsManagerWindow.getCategoriesName().indexOf(CATEGORY_NAME);
        toolsManagerWindow.changeCategoryOrder(CATEGORY_NAME, categoryPosition - 1);
        int newPosition = toolsManagerWindow.getCategoriesName().indexOf(CATEGORY_NAME);
        Assertions.assertThat(newPosition).isNotEqualTo(categoryPosition).isEqualTo(categoryPosition - 1);
    }
    
    @Test(priority = 5)
    public void changeOrderSubcategory() {
        int placeOfActors = toolsManagerWindow.getPlaceOfSubcategory(CATEGORY_NAME, ACTORS_SUBCATEGORY);
        toolsManagerWindow.changeSubcategoryOrder(CATEGORY_NAME, MOVIES_SUBCATEGORY, placeOfActors);
        int newPosition = toolsManagerWindow.getPlaceOfSubcategory(CATEGORY_NAME, MOVIES_SUBCATEGORY);
        Assertions.assertThat(newPosition).isEqualTo(placeOfActors);
    }
    
    @Test(priority = 6)
    public void changeOrderApplication() {
        int appPosition = toolsManagerWindow.getApplicationNames(CATEGORY_NAME).indexOf(APPLICATION_NAME_2);
        toolsManagerWindow.changeApplicationOrder(CATEGORY_NAME, APPLICATION_NAME, appPosition);
        int newPosition = toolsManagerWindow.getApplicationNames(CATEGORY_NAME).indexOf(APPLICATION_NAME);
        Assertions.assertThat(newPosition).isEqualTo(appPosition);
    }
    
    @Test(priority = 7)
    public void addPathParam() {
        ApplicationWizard applicationWizard = openApplicationWizard();
        applicationWizard.addPathParam(TEST_MOVIE_TYPE);
        applicationWizard.clickSave();
        DelayUtils.sleep(10000);
        toolsManagerWindow.openApplication(CATEGORY_NAME, APPLICATION_NAME_2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assertions.assertThat(driver.getCurrentUrl()).contains(TEST_MOVIE_TYPE);
    }
    
    @Test(priority = 8)
    public void addQueryParam() {
        goToHomePage();
        ApplicationWizard applicationWizard = openApplicationWizard();
        applicationWizard.addQueryParam("query", "rating=='10'");
        applicationWizard.clickSave();
        DelayUtils.sleep(10000);
        toolsManagerWindow.openApplication(CATEGORY_NAME, APPLICATION_NAME_2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ArrayList<String> ratingValues = getValuesFromColumn("rating");
        ratingValues.forEach(value -> Assertions.assertThat(value).isEqualTo("10"));
    }
    
    @Test(priority = 9)
    @Description("Set Favorites Application and check show ony favourites")
    public void setFavoriteApplication() {
        goToHomePage();
        Application application = toolsManagerWindow.getApplication(APPLICATION_NAME, CATEGORY_NAME);
        application.markFavorite();
        Assertions.assertThat(application.isFavorite()).isTrue();
        toolsManagerWindow.setShowOnlyFavourites();
        String applicationURL = toolsManagerWindow.getApplicationURL(APPLICATION_NAME).orElse(LINK_IS_NOT_AVAILABLE_EXCEPTION);
        Assertions.assertThat(toolsManagerWindow.getApplicationNames()).contains(APPLICATION_NAME);
        openViewBySideMenu(APPLICATION_NAME);
        Assertions.assertThat(applicationURL).isEqualTo(driver.getCurrentUrl());
    }
    
    @Test(priority = 10)
    @Description("Set Favorites Subcategory and check show ony favourites")
    public void setFavoriteSubcategory() {
        goToHomePage();
        Subcategory subcategoryMovie = toolsManagerWindow.getSubcategoryByName(MOVIES_SUBCATEGORY, CATEGORY_NAME);
        subcategoryMovie.markFavorite();
        Assertions.assertThat(subcategoryMovie.isFavorite()).isTrue();
        Assertions.assertThat(subcategoryMovie.getBadge()).isEqualTo("2/2");
        List<Application> applications = subcategoryMovie.getApplications();
        applications.forEach(application -> Assertions.assertThat(application.isFavorite()).isTrue());
        toolsManagerWindow.setShowOnlyFavourites();
        List<String> applicationNames = applications.stream().map(Application::getApplicationName).collect(Collectors.toList());
        Assertions.assertThat(toolsManagerWindow.getApplicationNames()).containsAll(applicationNames);
        String applicationURL = toolsManagerWindow.getApplicationURL(APPLICATION_NAME_2).orElse(LINK_IS_NOT_AVAILABLE_EXCEPTION);
        openViewBySideMenu(APPLICATION_NAME_2);
        Assertions.assertThat(applicationURL).isEqualTo(driver.getCurrentUrl());
    }
    
    @Test(priority = 11)
    public void addPolitics() {
        goToHomePage();
        ApplicationWizard applicationWizard = openApplicationWizard();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        applicationWizard.setPolicies(PHYSICAL_CONNECTION_TYPE, MODIFY_ROLE);
        applicationWizard.clickSave();
        DelayUtils.sleep(10000);
        Assertions.assertThat(toolsManagerWindow.getApplicationURL(APPLICATION_NAME_2)).isEmpty();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeUser(USER_2, PASSWORD_2);
        List<String> applicationNames = toolsManagerWindow.getApplicationNames(CATEGORY_NAME);
        Assertions.assertThat(applicationNames).doesNotContain(APPLICATION_NAME_2).contains(APPLICATION_NAME);
    }
    
    @Test(priority = 12)
    @Description("Check if Subcategory is not visible for user without configuration role ")
    public void checkPoliticsNotVisibleSubcategory() {
        // goToHomePage();
        changeUser(USER_1, PASSWORD_1);
        toolsManagerWindow.callActionSubcategory(ACTORS_SUBCATEGORY, CATEGORY_NAME, ADD_APPLICATION);
        ApplicationWizard applicationWizard = ApplicationWizard.create(driver, webDriverWait);
        applicationWizard.setApplication(APPLICATION_TYPE);
        applicationWizard.setApplicationName(APPLICATION_NAME_3);
        applicationWizard.setPolicies(PHYSICAL_CONNECTION_TYPE, MODIFY_ROLE);
        applicationWizard.clickSave();
        changeUser(USER_2, PASSWORD_2);
        List<String> subcategoriesNames = toolsManagerWindow.getSubcategoriesNames(CATEGORY_NAME);
        Assertions.assertThat(subcategoriesNames).doesNotContain(ACTORS_SUBCATEGORY).contains(MOVIES_SUBCATEGORY);
    }
    
    @Test(priority = 13)
    public void editApplication() {
        changeUser(USER_1, PASSWORD_1);
        ApplicationWizard applicationWizard = openApplicationWizard();
        applicationWizard.setApplicationName(APPLICATION_NAME_2_UPDATE);
        applicationWizard.clickSave();
        DelayUtils.sleep(10000);
        List<String> applicationNames = toolsManagerWindow.getApplicationNames(CATEGORY_NAME);
        Assertions.assertThat(applicationNames).contains(APPLICATION_NAME_2_UPDATE);
    }
    
    @Test(priority = 14)
    public void editSubcategoryName() {
        toolsManagerWindow.callActionSubcategory(ACTORS_SUBCATEGORY,CATEGORY_NAME, EDIT_BUTTON);
        CategoryWizard categoryWizard = CategoryWizard.create(driver, webDriverWait);
        categoryWizard.setName(ACTOR_SUBCATEGORY_UPDATE);
        categoryWizard.clickSave();
        DelayUtils.sleep(10000);
        List<String> subcategoriesNames = toolsManagerWindow.getSubcategoriesNames(CATEGORY_NAME);
        Assertions.assertThat(subcategoriesNames).doesNotContain(ACTORS_SUBCATEGORY).contains(ACTOR_SUBCATEGORY_UPDATE);
    }
    
    @Test(priority = 15)
    public void editCategoryName() {
        toolsManagerWindow.callAction(CATEGORY_NAME, EDIT_CATEGORY_BUTTON);
        CategoryWizard categoryWizard = CategoryWizard.create(driver, webDriverWait);
        categoryWizard.setName(CATEGORY_NAME_UPDATE);
        categoryWizard.clickSave();
        DelayUtils.sleep(10000);
        List<String> categoriesName = toolsManagerWindow.getCategoriesName();
        Assertions.assertThat(categoriesName).contains(CATEGORY_NAME_UPDATE).doesNotContain(CATEGORY_NAME);
    }
    
    @Test(priority = 16)
    @Description(value = "Delete Application" + APPLICATION_NAME)
    public void deleteApplication() {
        toolsManagerWindow.callActionApplication(APPLICATION_NAME, CATEGORY_NAME_UPDATE, DELETE_APPLICATION_ID);
        confirmDelete();
        DelayUtils.sleep(10000);
        List<String> applicationNames = toolsManagerWindow.getApplicationNames(CATEGORY_NAME_UPDATE);
        Assertions.assertThat(applicationNames).doesNotContain(APPLICATION_NAME).isNotEmpty();
    }
    
    @Test(priority = 17)
    @Description(value = "Delete Category" + MOVIES_SUBCATEGORY)
    public void deleteSubcategory() {
        toolsManagerWindow.callActionSubcategory(MOVIES_SUBCATEGORY, CATEGORY_NAME_UPDATE, DELETE_CATEGORY_BUTTON_ID);
        confirmDelete();
        DelayUtils.sleep(10000);
        List<String> subcategoriesNames = toolsManagerWindow.getSubcategoriesNames(CATEGORY_NAME_UPDATE);
        Assertions.assertThat(subcategoriesNames).contains(ACTOR_SUBCATEGORY_UPDATE).doesNotContain(MOVIES_SUBCATEGORY);
    }
    
    @Test(priority = 18)
    @Description("Check if Category is not visible for user without configuration role ")
    public void checkPoliticsNotVisibleCategory() {
        changeUser(USER_2, PASSWORD_2);
        List<String> categoriesName = toolsManagerWindow.getCategoriesName();
        Assertions.assertThat(categoriesName).doesNotContain(CATEGORY_NAME_UPDATE).isNotEmpty();
    }
    
    @Test(priority = 19)
    public void deleteCategory() {
        changeUser(USER_1, PASSWORD_1);
        toolsManagerWindow.callAction(CATEGORY_NAME_UPDATE, DELETE_CATEGORY_BUTTON_ID);
        Popup.create(driver,webDriverWait).clickButtonByLabel("Delete");
        DelayUtils.sleep(8000);
        List<String> categoriesName = toolsManagerWindow.getCategoriesName();
        Assertions.assertThat(categoriesName).doesNotContain(CATEGORY_NAME_UPDATE).isNotEmpty();
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
    
    private void addApplication(String name) {
        ApplicationWizard applicationWizard = ApplicationWizard.create(driver, webDriverWait);
        applicationWizard.addApplication(APPLICATION_TYPE, name);
    }
    
    private void changeUser(String userName, String password) {
        ToolsManagerPage toolsManagerPage = new ToolsManagerPage(driver);
        toolsManagerPage.changeUser(userName, password);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        toolsManagerWindow = toolsManagerPage.getToolsManager();
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
    }
    
    private void confirmDelete() {
        Popup.create(driver, webDriverWait).clickButtonByLabel(DELETE_BUTTON);
    }
    
    private void goToHomePage() {
        ToolsManagerPage toolsManagerPage = ToolsManagerPage.goToHomePage(driver, BASIC_URL);
        toolsManagerWindow = toolsManagerPage.getToolsManager();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
    
    private void openViewBySideMenu(String applicationName) {
        SideMenu.create(driver, webDriverWait).callActionByLabel(applicationName, FAVOURITES_MENU_CATEGORY, TOOLS_MENU_CATEGORY);
    }

    
}
