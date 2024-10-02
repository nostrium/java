/*
 * Define a map of files on the virtual file system
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.session.maps;

/**
 * @author Brito
 * @date: 2024-10-01
 * @location: Germany
 */
public class MapFile extends Map{

    public MapFile(String virtualPath) {
        super(MapType.FILE, virtualPath);
    }

    @Override
    public void index() {
        // not applicable to this case
    }

    
}
