/*
 * A root container for MapApp and MapFolder
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.session.maps;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import online.nostrium.servers.terminal.TerminalCommand;
import online.nostrium.servers.web.App;
import online.nostrium.session.RestrictedFiles;
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
        // clear previous data
        folders.clear();
        apps.clear();
        commands.clear();
        files.clear();
        links.clear();

        // specific for the user app
        if (relatedApp != null
                && relatedApp.getRelatedFolder() != null) {
            relatedFolder = relatedApp.getRelatedFolder();
        }

        // start the new indexing
        if (relatedApp != null) {
            // add the apps
            indexApps();
        }
        if (relatedFolder != null) {
            indexFolder();
        }
    }

    private void indexApps() {
        // needs to have an associated app
        if (relatedApp == null) {
            return;
        }

        // add the commands 
        for (TerminalCommand command : relatedApp.commands.values()) {
            MapCommand mapCmd = new MapCommand(command);
            mapCmd.setParent(this);
            addCommand(mapCmd);
        }
        // Add any links and virtual folders
        links.addAll(relatedApp.links);
        folders.addAll(relatedApp.folders);

        // add the apps
        for (App app : relatedApp.appChildren) {

            // only include if you have permission
            User user = app.session.getUser();
            if (app.permissions.isPermitted(user) == false) {
                continue;
            }
            // create a new map app
            MapApp mapApp = new MapApp(app);
            mapApp.setAppRelated(app);
            mapApp.setParent(this);
            mapApp.index();
            app.setMap(mapApp);
            // all done
            apps.add(mapApp);
        }
    }

    public void indexFolder() {
        if (relatedFolder == null) {
            return;
        }

        // the folder is mentioned but does not exist (yet)
        if (relatedFolder.exists() == false) {
            return;
        }

        // list the files
        File[] filesFound = relatedFolder.listFiles();
        if (filesFound == null || filesFound.length == 0) {
            return;
        }
        // iterate all found files
        for (File item : filesFound) {

            if (RestrictedFiles.dontList(item)) {
                continue;
            }

            if (item.isFile()) {
                MapFile mapFile = new MapFile(item.getName());
                mapFile.relatedFolder = item;
                mapFile.setParent(this);
                files.add(mapFile);
            } else {
                // avoid folder names with the same name as apps
                // because it should be the apps indexing the files

                // crawl inside the next folders and apps
                MapFolder mapFolder = new MapFolder(item.getName());
                mapFolder.setRelatedFolderOrFile(item);
                mapFolder.index();
                mapFolder.setParent(this);
                folders.add(mapFolder);
            }
        }

        // move the files in folders with same name to the
        // apps where they are found
        ArrayList<MapFolder> toRemoveFolders = new ArrayList<>();
        for (MapFolder folder : this.folders) {
            MapApp app = getAppWithName(folder.getName());
            if (app == null) {
                continue;
            }
            // found a match, merge it
            app.getFolders().addAll(folder.folders);
            app.getFiles().addAll(folder.files);
            app.getApps().addAll(folder.apps);
            app.getLinks().addAll(folder.links);
            app.setRelatedFolderOrFile(folder.relatedFolder);
            toRemoveFolders.add(folder);
        }
        // remove all the folders
        for(MapFolder toRemoveFolder : toRemoveFolders){
            folders.remove(toRemoveFolder);
        }
    }

    private MapApp getAppWithName(String name) {
        for (MapApp app : this.apps) {
            if (app.getName().equals(name)) {
                return app;
            }
        }
        return null;
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

        // List the apps (recursive)
        int appCount = apps.size();
        int appIndex = 0;
        for (MapApp app : apps) {
            app.buildTree(builder, prefix + (isTail ? "    " : "│   "), ++appIndex == appCount, false);
        }

        // List the folders
        int folderCount = folders.size();
        int folderIndex = 0;
        for (MapFolder folder : folders) {
            folder.buildTree(builder, prefix + (isTail ? "    " : "│   "), ++folderIndex == folderCount, false);
        }

        // List the virtual folders
        int linkCount = links.size();
        int linkIndex = 0;
        for (MapLink link : links) {
            builder.append(prefix)
                    .append(isTail ? "    " : "│   ")
                    .append(linkIndex++ == linkCount - 1 ? "└── " : "├── ")
                    .append("[link] ")
                    .append(link.getName())
                    .append("\n");
        }
    }

}
