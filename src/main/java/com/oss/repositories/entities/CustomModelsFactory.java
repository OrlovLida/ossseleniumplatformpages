/* @(#) $Id$
 *
 * Copyright (c) 2000-2021 ComArch S.A. All Rights Reserved. Any usage, duplication or redistribution of this
 * software is allowed only according to separate agreement prepared in written between ComArch and authorized party.
 */
package com.oss.repositories.entities;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.comarch.oss.services.infrastructure.objectmapper.JDK8ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

public class CustomModelsFactory {
    
    private static final String CANNOT_LOAD_MODEL = "Cannot load model {0}";
    private static final String CANNOT_FIND_MODEL = "Cannot find model with name: {0}";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomModelsFactory.class);
    
    private static final String MODELS_DIRECTORY = "src/test/resources/models";
    private static final String DEVICE_MODELS_DIRECTORY = MODELS_DIRECTORY + "/device";
    
    private static final ObjectMapper MAPPER = JDK8ObjectMapper.getMapper();
    
    private static Map<String, DeviceModel> deviceModels = Maps.newHashMap();
    
    static {
        loadModels(DEVICE_MODELS_DIRECTORY, CustomModelsFactory::loadDeviceModel);
    }
    
    private static void loadModels(String path, Consumer<File> modelLoader) {
        File modelsDirectory = new File(path);
        if (modelsDirectory.exists()) {
            loadModels(modelsDirectory, modelLoader);
        }
    }
    
    private static void loadModels(File modelsDirectory, Consumer<File> modelLoader) {
        Arrays.stream(modelsDirectory.listFiles())
                .filter(File::isFile)
                .forEach(modelLoader::accept);
    }
    
    private static void loadDeviceModel(File modelFile) {
        try {
            DeviceModel model = MAPPER.readValue(modelFile, DeviceModel.class);
            String name = FilenameUtils.getBaseName(modelFile.getName());
            deviceModels.put(name, model);
        } catch (IOException exception) {
            LOGGER.error(MessageFormat.format(CANNOT_LOAD_MODEL, modelFile.getName()), exception);
        }
    }
    
    public static DeviceModel findDeviceModelByName(String name) {
        return Optional.ofNullable(deviceModels.get(name))
                .orElseThrow(() -> new IllegalStateException(MessageFormat.format(CANNOT_FIND_MODEL, name)));
    }
}
