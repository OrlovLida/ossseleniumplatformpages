package com.oss.planning;

import com.oss.repositories.PlanningRepository;
import com.oss.untils.Environment;

public class ObjectIdentifier {

    public static final String IP_DEVICE_TYPE = "IPDevice";
    public static final String BUILDING_TYPE = "Building";
    public static final String CARD_TYPE = "Card";
    public static final String CHASSIS_TYPE = "Chassis";
    public static final String PLA_TEST_RESOURCE_TYPE = "PlaTestResource";
    public static final String PLA_TEST_COMPONENT_TYPE = "PlaTestComponent";

    private final Long id;
    private final String typeName;
    private final String typeLabel;


    public ObjectIdentifier(Long id, String typeName) {
        this.id = id;
        this.typeName = typeName;
        this.typeLabel = new PlanningRepository(Environment.getInstance()).getTypeLabel(typeName);
    }

    public static ObjectIdentifier ipDevice(Long id) {
        return new ObjectIdentifier(id, IP_DEVICE_TYPE);
    }

    public static ObjectIdentifier card(Long id) {
        return new ObjectIdentifier(id, CARD_TYPE);
    }

    public static ObjectIdentifier building(Long id) {
        return new ObjectIdentifier(id, BUILDING_TYPE);
    }

    public static ObjectIdentifier chassis(Long id) {
        return new ObjectIdentifier(id, CHASSIS_TYPE);
    }

    public static ObjectIdentifier plaTestResource(Long id) {
        return new ObjectIdentifier(id, PLA_TEST_RESOURCE_TYPE);
    }

    public static ObjectIdentifier plaTestComponent(Long id) {
        return new ObjectIdentifier(id, PLA_TEST_COMPONENT_TYPE);
    }


    public Long getId() {
        return id;
    }

    public String getType() {
        return typeName;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", typeName, id);
    }

    public String toStringWithLabel() {
        return String.format("%s (%d)", typeLabel, id);
    }
}
