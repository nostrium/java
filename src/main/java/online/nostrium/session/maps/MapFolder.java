/*
 * Define a folder on the virtual file system
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.session.maps;

import java.io.File;

/**
 * @author Brito
 * @date: 2024-10-01
 * @location: Germany
 */
public class MapFolder extends MapBox {

    public MapFolder(String virtualPath) {
        super(MapType.FOLDER, virtualPath);
    }

    public MapFolder(File folder) {
        super(MapType.FOLDER, folder.getName());
        relatedFolderOrFile = folder;
    }

}
