/*
 * Define a folder on the virtual file system
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.session.maps;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Brito
 * @date: 2024-10-01
 * @location: Germany
 */
public class MapFolder extends Map{

    Map parent = null;
    Set<MapFolder> folders = new TreeSet<>();
    Set<MapFile> files = new TreeSet<>();
    Set<MapApp> apps = new TreeSet<>();
    
    public MapFolder(String virtualPath) {
        super(MapType.FOLDER, virtualPath);
    }
    
    public MapFolder(File folder) {
        super(MapType.APP, folder.getName());
        realFile = folder;
    }
    
    public void index(){
        indexFolder();
    }

    public void indexFolder(){
        if(realFile == null || realFile.isFile()){
            return;
        }
        
        // list the files
        File[] filesFound = realFile.listFiles();
        if(filesFound == null || filesFound.length == 0){
            return;
        }
        // iterate all found files
        for(File item : filesFound){
            if(item.isFile()){
                MapFile mapFile = new MapFile(item.getName());
                mapFile.realFile = item;
                files.add(mapFile);
            }else{
                // crawl inside the next folders and apps
                MapFolder mapFolder = new MapFolder(item.getName());
                mapFolder.setRealFile(item);
                mapFolder.index();
                mapFolder.parent = this;
                folders.add(mapFolder);
            }
        }
    }
    
    public Map getParent() {
        return parent;
    }

    public void setParent(MapFolder parent) {
        this.parent = parent;
    }

    public void indexApps() {
        for(MapApp app : apps){
            app.index();
        }
    }

    public Set<MapFolder> getFolders() {
        return folders;
    }

    public Set<MapFile> getFiles() {
        return files;
    }

    public Set<MapApp> getApps() {
        return apps;
    }
    
}
