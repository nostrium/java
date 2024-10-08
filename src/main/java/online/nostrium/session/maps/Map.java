/*
 * Define a map of files on the virtual file system
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.session.maps;

import java.io.File;
import java.util.TreeSet;
import online.nostrium.servers.web.App;

/**
 * @author Brito
 * @date: 2024-10-01
 * @location: Germany
 */
public abstract class Map implements Comparable<Map> {

    public final String name;
    public final MapType type;
    private Map parent = null;

    File relatedFolder = null;
    App relatedApp = null;

    public Map(MapType type, String name) {
        this.name = name;
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
        return this.toString().compareTo(other.toString());
    }

    public File getRelatedFolderOrFile() {
        return relatedFolder;
    }

    public void setRelatedFolderOrFile(File realFile) {
        this.relatedFolder = realFile;
    }

    public App getAppRelated() {
        return relatedApp;
    }

    public void setAppRelated(App appRelated) {
        this.relatedApp = appRelated;
    }

    public String getName() {
        return name;
    }

    public MapType getType() {
        return type;
    }

    // find all files, folders, apps and related items
    //public abstract void index();

    /**
     * Provides a path of this item inside the map
     * For example /user/forum/section1/
     * @return the canonical path
     */
    public String getPath(){
        String path = getName();
        if(this.getParent() == null){
            return "/";
        }
        if(this.getParent().getParent() == null){
            return "/" + getName();
        }
        
        Map map = this;
        while(map.getParent() != null){
            map = map.getParent();
            // build the path, don't include the root element
            if(map.getParent() != null){
                path = map.getName() + "/" + path;
            }else{
                path = "/" + path;
            }
        }
        
        return path;
    }
    
    /**
     * Finds a folder, app or file within a given path. This is roughly
     * equivalent to CD in linux
     *
     * @param path e.g. /user/forum
     * @return null when the path was not found
     */
    public Map findPath(String path) {
        // Sanity check for the input path
        if (path == null) {
            return null;
        }
        
        // are talking about this map?
        if(path.isEmpty() || path.equals(".")) {
            return this;
        }
        
        // is it the parent path?
        if(path.equals("../") || path.equals("..")){
            if(this.getParent()!= null){
                return this.getParent();
            }else{
                // just means that exist no parent, should be null
                return null;
            }
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
            return map.findPath(path);
        }
        
        // is it something at the parent path?
        if(path.startsWith("../")){
            path = path.substring(3);
            if(getParent()!= null){
                return getParent().findPath(path);
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
                return next.findPath(path);
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

    /**
     * Equivalent in Linux to the LS command
     * @param parameters
     * @return 
     */
    public TreeSet<Map> listFiles(String parameters) {
        TreeSet<Map> list = new TreeSet<>();
        // is this an app?
        if(this instanceof MapApp mapApp){
            // first list the apps
            for(MapApp map : mapApp.getApps()){
                list.add(map);
            }
            // then add the folders
            for(MapFolder map : mapApp.getFolders()){
                list.add(map);
            }
            // finally the files
            for(MapFile map : mapApp.getFiles()){
                list.add(map);
            }
        }
        // is this a folder?
        if(this instanceof MapFolder mapFolder){
            // first list the apps
            for(MapApp map : mapFolder.getApps()){
                list.add(map);
            }
            // then add the folders
            for(MapFolder map : mapFolder.getFolders()){
                list.add(map);
            }
            // finally the files
            for(MapFile map : mapFolder.getFiles()){
                list.add(map);
            }
        }
        return list;
    }


}
