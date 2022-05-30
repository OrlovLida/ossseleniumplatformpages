package com.oss.pages.platform.OldInventoryView.helper;

public class OldInventoryViewConstants {
    public static final String PROPERTIES_TAB_LABEL = "Properties";
    public static final String BUILDING_PROPERTIES_TABLE_TEST_ID = "properties(Building)";
    public static final String CHP_PROPERTIES_TABLE_TEST_ID = "properties(CharacteristicPointLocation)";
    public static final String POP_PROPERTIES_TABLE_TEST_ID = "properties(PoP)";

    public static final String IV_BUILDING_ACTION_ID = "InventoryViewBuilding";
    public static final String IV_CHP_ACTION_ID = "InventoryViewCharacteristicPointLocation";
    public static final String IV_POP_ACTION_ID = "InventoryViewPoP";

    public static final String BUILDING_ALL_LOCATIONS_TAB = "Tab:DetailAllLocations(Building)";
    public static final String BUILDING_ALL_LOCATIONS_TABLE = "DetailAllLocations(Building)";
    public static final String CHP_DETAIL_LOCATIONS_TABLE = "DetailLocationsInLocation(CharacteristicPointLocation)";
    public static final String MORE_BUTTON_PATH = "//button[@class='btn btn-default dropdown-toggle' and .//div[contains(., 'More')]]";

    private OldInventoryViewConstants() {
        throw new IllegalStateException("Utility class");
    }
}
