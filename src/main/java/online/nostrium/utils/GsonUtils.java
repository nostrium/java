/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;

/**
 * @author Brito
 * @date: 2024-08-07
 * @location: Germany
 */
public class GsonUtils {
    public static Gson createGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .registerTypeAdapter(File.class, new FileTypeAdapter())
                .create();
    }
}