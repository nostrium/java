/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.IOException;

/**
 * @author Brito
 * @date: 2024-08-07
 * @location: Germany
 */
public class FileTypeAdapter extends TypeAdapter<File> {
    @Override
    public void write(JsonWriter out, File value) throws IOException {
        out.value(value.getPath());
    }

    @Override
    public File read(JsonReader in) throws IOException {
        return new File(in.nextString());
    }
}