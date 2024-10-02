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
public abstract class Map implements Comparable<Map> {

    public final String name;
    public final MapType type;
    private Map parent = null;

    File realFile = null;
    TerminalApp appRelated = null;

    public Map(MapType type, String virtualPath) {
        this.name = virtualPath;
        this.type = type;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(Map other) {
        // compare based on folder vs file
        if (type != other.type) {
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

    /**
     * Finds a folder, app or file within a given path. This is roughly
     * equivalent to CD in linux
     *
     * @param path e.g. /user/forum
     * @return null when the path was not found
     */
    public Map getPath(String path) {
        // Sanity check for the input path
        if (path == null) {
            return null;
        }
        
        // are talking about this map?
        if(path.isEmpty() || path.equals(".")) {
            return this;
        }

        // ending with a slash for some reason?
        if(path.length() > 1 && path.endsWith("/") 
                && path.equals("../") == false){
            path = path.substring(0, path.length()-1);
        }
        // still with a slash? Invalid path
        if(path.endsWith("/")
                && path.length() > 1
                && path.equals("../") == false){
            return null;
        }
        
        // a folder path starting from the root?
        if(path.equals("/")){
            Map map = this;
            // now find the root component
            while(map.getParent() != null){
                map = map.getParent();
            }
            return map;
        }
        if(path.startsWith("/")){
            // so it seems, then remove the slash first
            path = path.substring(1);
            Map map = this;
            // now find the root component
            while(map.getParent() != null){
                map = map.getParent();
            }
            // proceed from the root, but without the slash
            return map.getPath(path);
        }
        
        // is it the parent path?
        if(path.equals("../")){
            if(this.getParent()!= null){
                return this.getParent();
            }else{
                // just means that exist no parent, should be null
                return null;
            }
        }
        
        // is it something at the parent path?
        if(path.startsWith("../")){
            path = path.substring(3);
            if(this.getParent()!= null){
                return this.getParent().getPath(path);
            }else{
                // just means that exist no parent, should be null
                return null;
            }
        }

        // clean up the path
        if(path.startsWith("./")){
            path = path.substring(2);
        }
        if(path.startsWith(".")){
            path = path.substring(1);
        }
        
        // is someone playing with us? this is not a valid path now
        if(path.startsWith("/")){
            return null;
        }
        
        // are we now looking for an item on this path?
        if(path.contains("/") == false){
            return getBasedOnName(path);
        }else{
            // remove the name before the next slash
            int i = path.indexOf("/");
            String nextName = path.substring(0, i);
            Map next = getBasedOnName(nextName);
            if(next == null){
                return null;
            }else{
                path = path.substring(i+1);
                return next.getPath(path);
            }
        }
    }

    /**
     * Tries to first map matching a name
     * @param name
     * @return null when nothing was found
     */
    private Map getBasedOnName(String name) {
        
        // is the name related to this map?
        if(this.getName().equals(name)){
            return this;
        }
        
        // then let's start with apps
        if(this instanceof MapApp mapApp){
            for(MapApp app : mapApp.getApps()){
                if(app.getName().equals(name)){
                    return app;
                }
            }
            // maybe it is a folder?
            for(MapFolder folder : mapApp.getFolders()){
                if(folder.getName().equals(name)){
                    return folder;
                }
            }
            // can it be a file?
            for(MapFile file : mapApp.getFiles()){
                if(file.getName().equals(name)){
                    return file;
                }
            }
            // found nothing for this app
            return null;
        }
        // was not an app, is it a folder?
        if(this instanceof MapFolder mapFolder){
            // can it be an app?
            for(MapApp app : mapFolder.getApps()){
                if(app.getName().equals(name)){
                    return app;
                }
            }
            // is this a folder
            for(MapFolder folder : mapFolder.getFolders()){
                if(folder.getName().equals(name)){
                    return folder;
                }
            }
            // can it be a file?
            for(MapFile file : mapFolder.getFiles()){
                if(file.getName().equals(name)){
                    return file;
                }
            }
            // found nothing for this app
            return null;
        }
        
        // found nothing
        return null;
    }
    
     public Map getParent() {
        return parent;
    }

    public void setParent(Map parent) {
        this.parent = parent;
    }
    
    
//    // Split the path into segments
//    String[] items = path.split("/");
//
//    // Start from the current map (this)
//    Map current = this;
//
//    // Iterate through each segment in the path
//    for (String item : items) {
//        if (item.isEmpty() || item.equals(".")) {
//            continue;  // Skip empty or current directory symbol (.)
//        }
//
//        boolean found = false;
//
//        // If current is a folder, check for folders or files
//        if (current instanceof MapFolder) {
//            // Check in folders
//            for (MapFolder folder : ((MapFolder) current).getFolders()) {
//                if (folder.getName().equals(item)) {
//                    current = folder;
//                    found = true;
//                    break;
//                }
//            }
//
//            // If not found in folders, check in files
//            if (!found) {
//                for (MapFile file : ((MapFolder) current).getFiles()) {
//                    if (file.getName().equals(item)) {
//                        current = file;
//                        found = true;
//                        break;
//                    }
//                }
//            }
//        }
//
//        // If current is an app, check for apps and commands
//        if (!found && current instanceof MapApp) {
//            // Check in apps
//            for (MapApp app : ((MapApp) current).getApps()) {
//                if (app.getName().equals(item)) {
//                    current = app;
//                    found = true;
//                    break;
//                }
//            }
//
//           
//        }
//
//        // If nothing was found for this segment, the path does not exist
//        if (!found) {
//            return null;
//        }
//    }
//
//    // Return the final resolved map object (folder, app, file, or command)
//    return current;
//}

}
