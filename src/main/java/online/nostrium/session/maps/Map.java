/*
 * Define a map of files on the virtual file system
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.session.maps;

import java.io.File;
import online.nostrium.servers.terminal.TerminalApp;

/**
 * @author Brito
 * @date: 2024-10-01
 * @location: Germany
 */
public abstract class Map implements Comparable<Map>{
    
    public final String name;
    public final MapType type;
    
    File realFile = null;
    TerminalApp appRelated = null;

    public Map(MapType type, String virtualPath) {
        this.name = virtualPath;
        this.type = type;
    }

    @Override
    public String toString(){
        return this.name;
    }
    
    @Override
    public int compareTo(Map other) {
        // compare based on folder vs file
        if(type != other.type){
            return this.toString().compareTo(other.toString());
        }
        // compare based on file name
        return other.toString().compareTo(this.toString());
    }

    public File getRealFile() {
        return realFile;
    }

    public void setRealFile(File realFile) {
        this.realFile = realFile;
    }

    public TerminalApp getAppRelated() {
        return appRelated;
    }

    public void setAppRelated(TerminalApp appRelated) {
        this.appRelated = appRelated;
    }

    public String getName() {
        return name;
    }

    public MapType getType() {
        return type;
    }
    
    

    // find all files, folders, apps and related items
    public abstract void index();

}
