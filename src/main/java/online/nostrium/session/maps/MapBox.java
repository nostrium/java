/*
 * A root container for MapApp and MapFolder
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.session.maps;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.user.User;

/**
 * @author Brito
 * @date: 2024-10-03
 * @location: Germany
 */
public class MapBox extends Map {

    Set<MapFolder> folders = new TreeSet<>();
    Set<MapApp> apps = new TreeSet<>();
    Set<MapCommand> commands = new TreeSet<>();
    Set<MapFile> files = new TreeSet<>();
    Set<MapLink> links = new TreeSet<>();

    public MapBox(MapType type, String name) {
        super(type, name);
    }

    public void index() {
        if (relatedApp != null) {
            // add the apps
            indexApps();
        }
        if(relatedFile != null 
                && relatedFile.exists() 
                && relatedFile.isDirectory()){
            indexFolder();
        }
    }

    private void indexApps() {
        // needs to have an associated app
        if (relatedApp == null) {
            return;
        }
        // add app apps
        for (TerminalApp app : relatedApp.appChildren) {
            
            // only include if you have permission
            User user = app.session.getUser();
            if(app.permissions.isPermitted(user)==false){
                continue;
            }
            
            // create a new map app
            MapApp mapApp = new MapApp(app);
            mapApp.setAppRelated(app);
            mapApp.setParent(this);
            mapApp.index();
            apps.add(mapApp);
            // add the commands too
            for (TerminalCommand command : app.commands.values()) {
                MapCommand mapCmd = new MapCommand(command);
                mapCmd.setParent(mapApp);
                mapApp.addCommand(mapCmd);
            }
        }
    }

    public void indexFolder() {
        if (relatedFile == null || relatedFile.isFile()) {
            return;
        }

        // list the files
        File[] filesFound = relatedFile.listFiles();
        if (filesFound == null || filesFound.length == 0) {
            return;
        }
        // iterate all found files
        for (File item : filesFound) {
            if (item.isFile()) {
                MapFile mapFile = new MapFile(item.getName());
                mapFile.relatedFile = item;
                mapFile.setParent(this);
                files.add(mapFile);
            } else {
                // crawl inside the next folders and apps
                MapFolder mapFolder = new MapFolder(item.getName());
                mapFolder.setRealFile(item);
                mapFolder.index();
                mapFolder.setParent(this);
                folders.add(mapFolder);
            }
        }
    }

    public Set<MapApp> getApps() {
        return this.apps;
    }

    public Set<MapFile> getFiles() {
        return this.files;
    }

    public Set<MapFolder> getFolders() {
        return this.folders;
    }

    public Set<MapCommand> getCommands() {
        return commands;
    }

    public Set<MapLink> getLinks() {
        return links;
    }

    public void addCommand(MapCommand mapCmd) {
        commands.add(mapCmd);
    }

    public void addLink(MapLink mapLink) {
        links.add(mapLink);
    }

    public void addFolder(MapFolder mapFolder) {
        folders.add(mapFolder);
    }

    
    public String getTree() {
        StringBuilder treeBuilder = new StringBuilder();
        buildTree(treeBuilder, "", true, true);
        return treeBuilder.toString();
    }

    public void buildTree(StringBuilder builder, String prefix, boolean isTail, boolean isRoot) {
        if (isRoot == false) {
            // Append the current app name
            builder.append(prefix).append(isTail ? "└── " : "├── ")
//                    .append("[app] ")
                    .append(getName())
                    .append("\n");
        }

//        // List the commands
//        int commandCount = commands.size();
//        int commandIndex = 0;
//        for (MapCommand command : commands) {
//            if(command.getCmd().internalCommand){
//                commandCount--;
//                continue;
//            }
//            builder.append(prefix)
//                    .append(isTail ? "    " : "│   ")
//                    .append(commandIndex++ == commandCount - 1 ? "└── " : "├── ")
//                    //.append("[cmd] ")
//                    .append(command.getName())
//                    .append("\n");
//        }

        // List the files
        int fileCount = files.size();
        int fileIndex = 0;
        for (MapFile file : files) {
            builder.append(prefix)
                    .append(isTail ? "    " : "│   ")
                    .append(fileIndex++ == fileCount - 1 ? "└── " : "├── ")
                    .append("[file] ")
                    .append(file.getName())
                    .append("\n");
        }

        // List the folders
        int folderCount = folders.size();
        int folderIndex = 0;
        for (MapFolder folder : folders) {
            folder.buildTree(builder, prefix + (isTail ? "    " : "│   "), ++folderIndex == folderCount, false);
        }

        // List the apps (recursive)
        int appCount = apps.size();
        int appIndex = 0;
        for (MapApp app : apps) {
            app.buildTree(builder, prefix + (isTail ? "    " : "│   "), ++appIndex == appCount, false);
        }
    }

}