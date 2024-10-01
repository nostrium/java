/*
 * Define a map of files on the virtual file system
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.session.maps;

import java.io.File;
import java.util.ArrayList;
import online.nostrium.utils.FileFunctions;

/**
 * @author Brito
 * @date: 2024-10-01
 * @location: Germany
 */
public class Map {

    public void index(File folder, Map map){
        ArrayList<File> files = FileFunctions.searchFiles(folder);
        String startPath = "";
//        for(File file : files){
//            MapKey key = new MapKey();
//        }
    }
    
}
