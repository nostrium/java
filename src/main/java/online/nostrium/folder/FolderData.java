/*
 * Write data how a specific folder should be used
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.folder;

import com.google.gson.annotations.Expose;

/**
 * @author Brito
 * @date: 2024-09-27
 * @location: Germany
 */
public class FolderData {

    public static final String filename = FolderUtils.nameFolderData; // data.json

    
    @Expose
    String title;   // human readable title

    @Expose
    String description;  // one line description

    @Expose
    FolderType type = FolderType.NONE;
}
