package com.oss.planning;

public class ObjectIdentifier {

    private static final String IP_DEVICE_TYPE = "IPDevice";
    private static final String BUILDING = "Building";
    private static final String CARD = "Card";
    private static final String CHASSIS = "Chassis";

    private final Long id;
    private final String type;


    public ObjectIdentifier(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    public static ObjectIdentifier ipDevice(Long id) {
        return new ObjectIdentifier(id, IP_DEVICE_TYPE);
    }

    public static ObjectIdentifier card(Long id) {
        return new ObjectIdentifier(id, CARD);
    }

    public static ObjectIdentifier building(Long id) {
        return new ObjectIdentifier(id, BUILDING);
    }

    public static ObjectIdentifier chassis(Long id) {
        return new ObjectIdentifier(id, CHASSIS);
    }


    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
