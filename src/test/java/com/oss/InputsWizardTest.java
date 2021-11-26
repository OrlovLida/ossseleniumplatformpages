package com.oss;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.framework.components.inputs.Checkbox;
import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Coordinates;
import com.oss.framework.components.inputs.Date;
import com.oss.framework.components.inputs.DateTime;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.inputs.MultiCombobox;
import com.oss.framework.components.inputs.MultiSearchField;
import com.oss.framework.components.inputs.NumberField;
import com.oss.framework.components.inputs.PasswordField;
import com.oss.framework.components.inputs.PhoneField;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.components.inputs.TextArea;
import com.oss.framework.components.inputs.TextField;
import com.oss.framework.components.inputs.Time;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.InputsWizardPage;

public class InputsWizardTest extends BaseTestCase {
    private static final String INPUTS_WIZARD_URL = String.format("%s%s/wizards/inputs-wizard?perspective=LIVE" +
            "&withRemoved=false", BASIC_URL, MOCK_PATH);
    
    private InputsWizardPage inputsWizardPage;
    
    @BeforeClass
    public void openInputsWizard() {
        inputsWizardPage = homePage.goToInputsWizardPage(INPUTS_WIZARD_URL);
    }
    
    @Test
    public void testCheckbox() {
        Checkbox checkbox = (Checkbox) inputsWizardPage
                .getComponent(InputsWizardPage.CHECKBOX_ID, ComponentType.CHECKBOX);
        checkbox.setSingleStringValue("true");
        testComponent(InputsWizardPage.CHECKBOX_ID, checkbox);
    }
    
    @Test
    public void testCombobox() {
        inputsWizardPage = homePage.goToInputsWizardPage(INPUTS_WIZARD_URL);
        Combobox combobox = (Combobox) inputsWizardPage
                .getComponent(InputsWizardPage.COMBOBOX_ID, ComponentType.COMBOBOX);
        // Combobox specific test cases
        combobox.setSingleStringValue("TestData3");
        Assertions.assertThat(combobox.getStringValue()).isEqualTo("TestData3");
        combobox.setSingleStringValue("TestData1");
        Assertions.assertThat(combobox.getStringValue()).isEqualTo("TestData1");
        combobox.clear();
        Assertions.assertThat(combobox.getStringValue()).isEqualTo("");
        
        // Component tests
        testComponent(InputsWizardPage.COMBOBOX_ID, combobox);
    }
    
    @Test
    public void testCoordinates() {
        Coordinates coordinates = (Coordinates) inputsWizardPage
                .getComponent(InputsWizardPage.COORDINATES_ID, ComponentType.COORDINATES);
        List<String> coordinatesList = new ArrayList<>();
        coordinatesList.add("S");
        coordinatesList.add("120");
        coordinatesList.add("45");
        coordinatesList.add("30");
        
        // Coordinates specific test cases
        coordinates.setMultiStringValue(coordinatesList);
        Assertions.assertThat(coordinates.getStringValues()).isEqualTo(coordinatesList);
        
        coordinates.clear();
        coordinatesList.clear();
        
        // Component tests
        testComponent(InputsWizardPage.COORDINATES_ID, coordinates);
    }
    
    @Test
    // mask 1111-11-11
    public void testDate() {
        Date date = (Date) inputsWizardPage
                .getComponent(InputsWizardPage.DATE_ID, ComponentType.DATE);
        
        date.setValue(Data.createSingleData("2018-12-02"));
        Assertions.assertThat(date.getStringValue()).isEqualTo("2018-12-02");
        
        date.clear();
        Assertions.assertThat(date.getStringValue()).isEqualTo("");
        
        DelayUtils.sleep(1000);
        date.chooseDate("2020-12-02");
        DelayUtils.sleep(500);
        Assertions.assertThat(date.getStringValue()).isEqualTo("2020-12-02");
        
        DelayUtils.sleep(1000);
        date.chooseDate("2018-01-28");
        DelayUtils.sleep(500);
        Assertions.assertThat(date.getStringValue()).isEqualTo("2018-01-28");
        
        DelayUtils.sleep(1000);
        date.chooseDate("2019-06-21");
        DelayUtils.sleep(500);
        Assertions.assertThat(date.getStringValue()).isEqualTo("2019-06-21");
        
        DelayUtils.sleep(1000);
        date.chooseDate("2019-11-04");
        DelayUtils.sleep(500);
        Assertions.assertThat(date.getStringValue()).isEqualTo("2019-11-04");
        
        DelayUtils.sleep(1000);
        date.chooseDate("2022-02-06");
        DelayUtils.sleep(500);
        Assertions.assertThat(date.getStringValue()).isEqualTo("2022-02-06");
        
        // Component tests
        testComponent(InputsWizardPage.DATE_ID, date);
        
    }
    
    @Test()
    public void testDateTime() {
        DateTime dateTime = (DateTime) inputsWizardPage
                .getComponent(InputsWizardPage.DATE_TIME_ID, ComponentType.DATE_TIME);
        dateTime.chooseDate("2021-12-12");
        
        // Date Time specific test cases
        dateTime.setSingleStringValue("19951111121212");
        Assertions.assertThat(dateTime.getStringValue()).isEqualTo("1995-11-11 12:12:12");
        dateTime.clear();
        
        dateTime.setSingleStringValue("www");
        Assertions.assertThat(dateTime.getStringValue()).isEqualTo("");
        dateTime.clear();
        
        dateTime.setSingleStringValue("123");
        dateTime.hover();
        List<String> validationMessage = dateTime.getMessages();
        Assertions.assertThat(validationMessage).contains("Wrong date.");
        dateTime.clear();
        
        //
        dateTime.chooseTime("06:12:35");
        Assertions.assertThat(dateTime.getStringValue()).contains("06:12:35");
        dateTime.chooseTime("05:10:30");
        Assertions.assertThat(dateTime.getStringValue()).contains("05:10:30");
        dateTime.clear();
        
        // Component tests
        testComponent(InputsWizardPage.DATE_TIME_ID, dateTime);
    }
    
    @Test(enabled = false)
    public void testDateRange() {
        
    }
    
    @Test(enabled = false)
    public void testFileChooser() {
        
        // TODO: refactor fileChooser - fileChooser nie dziala, nie wrzuca wybranego pliku
        // FileChooser fileChooser = FileChooser.create(driver, webDriverWait, "file");
    }
    
    @Test(enabled = true)
    public void testMultiCombobox() {
        MultiCombobox multiCombobox = (MultiCombobox) inputsWizardPage
                .getComponent(InputsWizardPage.MULTI_COMBOBOX_ID, ComponentType.MULTI_COMBOBOX);
        
        // Multi Combobox specific tests
        multiCombobox.setSingleStringValue("TestData1");
        Assertions.assertThat(multiCombobox.getStringValue()).isEqualTo("TestData1");
        multiCombobox.clear();
        Assertions.assertThat(multiCombobox.getStringValues()).isEmpty();
        
        // Component tests
        testComponent(InputsWizardPage.MULTI_COMBOBOX_ID, multiCombobox);
    }
    
    @Test(enabled = true)
    public void testMultiSearchField() {
        MultiSearchField multiSearchField = (MultiSearchField) inputsWizardPage
                .getComponent(InputsWizardPage.MULTI_SEARCH_FIELD_ID, ComponentType.MULTI_SEARCH_FIELD);
        
        // Multi Searchfield specific tests
        multiSearchField.setSingleStringValue("TestData1");
        Assertions.assertThat(multiSearchField.getStringValue()).isEqualTo("TestData1");
        multiSearchField.clear();
        Assertions.assertThat(multiSearchField.getStringValues()).isEmpty();
        
        // Component tests
        testComponent(InputsWizardPage.MULTI_SEARCH_FIELD_ID, multiSearchField);
    }
    
    @Test
    public void testNumberField() {
        NumberField numberField = (NumberField) inputsWizardPage
                .getComponent(InputsWizardPage.NUMBER_FIELD_ID, ComponentType.NUMBER_FIELD);
        
        // Number field specific tests
        numberField.setSingleStringValue("20191111");
        Assertions.assertThat(numberField.getStringValue()).isEqualTo("20 191 111");
        numberField.clear();
        Assertions.assertThat(numberField.getStringValue()).isEqualTo("");
        numberField.setSingleStringValue("alphabet");
        Assertions.assertThat(numberField.getStringValue()).isEqualTo("");
        
        // Component tests
        testComponent(InputsWizardPage.NUMBER_FIELD_ID, numberField);
    }
    
    @Test
    public void testPasswordField() {
        PasswordField passwordField = (PasswordField) inputsWizardPage
                .getComponent(InputsWizardPage.PASSWORD_FIELD_ID, ComponentType.PASSWORD_FIELD);
        // Password Field specific test cases
        
        passwordField.setSingleStringValue("Maliny^&");
        Assertions.assertThat(passwordField.getStringValue()).isEqualTo("Maliny^&");
        passwordField.clear();
        Assertions.assertThat(passwordField.getStringValue()).isEqualTo("");
        
        // Component tests
        testComponent(InputsWizardPage.PASSWORD_FIELD_ID, passwordField);
    }
    
    @Test
    public void testPhoneField() {
        PhoneField phoneField = (PhoneField) inputsWizardPage
                .getComponent(InputsWizardPage.PHONE_FIELD_ID, ComponentType.PHONE_FIELD);
        ;
        
        // Phone Field specific test cases
        
        phoneField.setSingleStringValue("+49 2056");
        Assertions.assertThat(phoneField.getStringValue()).isEqualTo("+49 2056");
        phoneField.clear();
        Assertions.assertThat(phoneField.getStringValue()).isEqualTo("");
        
        // Component tests
        testComponent(InputsWizardPage.PHONE_FIELD_ID, phoneField);
    }
    
    @Test
    public void testSearchField() {
        SearchField searchField = (SearchField) inputsWizardPage
                .getComponent(InputsWizardPage.SEARCH_FIELD_ID, ComponentType.SEARCH_FIELD);
        
        // Search Field specific test case
        testComponent(InputsWizardPage.SEARCH_FIELD_ID, searchField);
    }
    
//    @Test(enabled = false)
//    public void testSwitcher() throws InterruptedException {
//        Switcher switcher = inputsWizardPage.getSwitcher();
//        //Switcher specific test cases
//        switcher.set();
//        //Component tests
//        testComponent(InputsWizardPage.SWITCHER_ID, switcher);
//    }
//
    @Test
    public void testTextArea() {
        TextArea textArea = (TextArea) inputsWizardPage
                .getComponent(InputsWizardPage.TEXT_AREA_ID, ComponentType.TEXT_AREA);
        // Text Area specific test cases
        textArea.setSingleStringValue("testOSS123!@#");
        Assertions.assertThat(textArea.getStringValue()).isEqualTo("testOSS123!@#");
        textArea.clear();
        Assertions.assertThat(textArea.getStringValue()).isEqualTo("");
        // Component tests
        testComponent(InputsWizardPage.TEXT_AREA_ID, textArea);
    }
    
    @Test
    public void testTextField() {
        TextField textField = (TextField) inputsWizardPage
                .getComponent(InputsWizardPage.TEXT_FIELD_ID, ComponentType.TEXT_FIELD);
        // Text Field specific test cases
        
        textField.setSingleStringValue("duap");
        Assertions.assertThat(textField.getStringValue()).isEqualTo("duap");
        textField.clear();
        Assertions.assertThat(textField.getStringValue()).isEqualTo("");
        
        // Component tests
        testComponent(InputsWizardPage.TEXT_FIELD_ID, textField);
    }
    
    @Test
    public void testTime() {
        Time time = (Time) inputsWizardPage
                .getComponent(InputsWizardPage.TIME_ID, ComponentType.TIME);
        
        time.setSingleStringValue("05:23");
        Assertions.assertThat(time.getStringValue()).isEqualTo("05:23");
        time.clear();
        Assertions.assertThat(time.getStringValue()).isEqualTo("");
        
        time.chooseTime("6:25");
        time.chooseTime("21:03");
        time.chooseTime("03:44");
        time.chooseTime("00:00");
        
        testComponent(InputsWizardPage.TIME_ID, time);
    }
    
    private void testComponent(String componentId, Input input) {
        // clean up
        // inputsWizardPage.clearAllControllers();
        
        // Label Test
        String label = input.getLabel();
        Assertions.assertThat(componentId).isEqualTo(label);
        
        // Messages danger test
        inputsWizardPage.setControllerValue(InputsWizardPage.DANGER_MESSAGE_CONTROLLER_ID, componentId);
        input.hover();
        List<String> messages = input.getMessages();
        Assertions.assertThat(messages).contains("DANGER");
        inputsWizardPage.clearController(InputsWizardPage.DANGER_MESSAGE_CONTROLLER_ID);
        
        // Messages warning test
        
        // Read only test //
        inputsWizardPage.setControllerValue(InputsWizardPage.READ_ONLY_CONTROLLER_ID, componentId);
        Assertions.assertThat(input.cursor()).isEqualTo(Input.MouseCursor.NOT_ALLOWED);
        inputsWizardPage.clearController(InputsWizardPage.READ_ONLY_CONTROLLER_ID);
        
        // Tooltip message test
        Assertions.assertThat(input.getHint().get(0)).contains(componentId);
        
        // Mandatory label test
        inputsWizardPage.setControllerValue(InputsWizardPage.MANDATORY_CONTROLLER_ID, componentId);
        String mandatoryLabel = input.getLabel();
        Assertions.assertThat(mandatoryLabel).contains("*");
        
        // Mandatory validation test //
        // inputsWizardPage.submit();
        input.hover();
        List<String> mandatoryMessages = input.getMessages();
        Assertions.assertThat(mandatoryMessages).contains("This field is mandatory.");
        inputsWizardPage.clearController(InputsWizardPage.MANDATORY_CONTROLLER_ID);
        
        // Hidden test, must be always last
        inputsWizardPage.setControllerValue(InputsWizardPage.HIDDEN_CONTROLLER_ID, componentId);
        Assertions.assertThatThrownBy(input::hover).isInstanceOf(StaleElementReferenceException.class);
        inputsWizardPage.clearController(InputsWizardPage.HIDDEN_CONTROLLER_ID);
    }
}
