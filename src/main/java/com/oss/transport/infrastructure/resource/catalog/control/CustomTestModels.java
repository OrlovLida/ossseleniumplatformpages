package com.oss.transport.infrastructure.resource.catalog.control;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.comarch.oss.services.infrastructure.objectmapper.JDK8ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss.transport.infrastructure.resource.catalog.control.json.CardModelDTO;
import com.oss.transport.infrastructure.resource.catalog.control.json.DeviceModelDTO;
import com.oss.transport.infrastructure.resource.catalog.control.json.PluggableModuleModelDTO;
import com.oss.transport.infrastructure.resource.catalog.control.json.PluggableModuleSlotModelDTO;

public class CustomTestModels {

    public static final String CANNOT_LOAD_MODEL_S_S_N = "Cannot load model %s: %s %n";
    private static final Map<String, DeviceModelDTO> deviceModels = new HashMap<>();
    private static final Map<String, CardModelDTO> cardModels = new HashMap<>();
    private static final Map<String, PluggableModuleSlotModelDTO> pluggableModuleSlotModels = new HashMap<>();
    private static final Map<String, PluggableModuleModelDTO> pluggableModuleModels = new HashMap<>();

    private static final ObjectMapper mapper = JDK8ObjectMapper.getMapper();

    static {
        loadModels("src/test/resources/model/device", CustomTestModels::loadDeviceModel);
        loadModels("src/test/resources/model/card", CustomTestModels::loadCardModel);
        loadModels("src/test/resources/model/pluggableModuleSlot", CustomTestModels::loadPluggableModuleSlotModel);
        loadModels("src/test/resources/model/pluggableModule", CustomTestModels::loadPluggableModuleModel);
    }

    private CustomTestModels() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, CardModelDTO> getCardModels() {
        return cardModels;
    }

    public static Map<String, PluggableModuleSlotModelDTO> getPluggableModuleSlotModels() {
        return pluggableModuleSlotModels;
    }

    public static Map<String, PluggableModuleModelDTO> getPluggableModuleModels() {
        return pluggableModuleModels;
    }

    public static Map<String, DeviceModelDTO> getDeviceModels() {
        return deviceModels;
    }

    private static void loadModels(String path, Consumer<File> modelLoader) {
        File modelsDirectory = new File(path);
        if (modelsDirectory.exists()) {
            Arrays.stream(modelsDirectory.listFiles())
                    .filter(File::isFile)
                    .forEach(modelLoader::accept);
        }
    }

    private static void loadDeviceModel(File modelFile) {
        try {
            DeviceModelDTO model = mapper.readValue(modelFile, DeviceModelDTO.class);
            String name = getFileName(modelFile);
            deviceModels.put(name, model);
        } catch (IOException e) {
            System.out.printf(CANNOT_LOAD_MODEL_S_S_N, modelFile.getName(), e.getMessage());
        }
    }

    private static void loadCardModel(File modelFile) {
        try {
            CardModelDTO model = mapper.readValue(modelFile, CardModelDTO.class);
            String name = getFileName(modelFile);
            cardModels.put(name, model);
        } catch (IOException e) {
            System.out.printf(CANNOT_LOAD_MODEL_S_S_N, modelFile.getName(), e.getMessage());
        }
    }

    private static void loadPluggableModuleSlotModel(File modelFile) {
        try {
            PluggableModuleSlotModelDTO model = mapper.readValue(modelFile, PluggableModuleSlotModelDTO.class);
            String name = getFileName(modelFile);
            pluggableModuleSlotModels.put(name, model);
        } catch (IOException e) {
            System.out.printf(CANNOT_LOAD_MODEL_S_S_N, modelFile.getName(), e.getMessage());
        }
    }

    private static void loadPluggableModuleModel(File modelFile) {
        try {
            PluggableModuleModelDTO model = mapper.readValue(modelFile, PluggableModuleModelDTO.class);
            String name = getFileName(modelFile);
            pluggableModuleModels.put(name, model);
        } catch (IOException e) {
            System.out.printf(CANNOT_LOAD_MODEL_S_S_N, modelFile.getName(), e.getMessage());
        }
    }

    private static String getFileName(File file) {
        String name = file.getName();
        int pos = name.lastIndexOf(".");
        if (pos > 0) {
            name = name.substring(0, pos);
        }

        return name;
    }
}
