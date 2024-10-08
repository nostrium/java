/*
 * Define an app on the virtual file system
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0  ,
 */
package online.nostrium.session.maps;

import online.nostrium.servers.web.App;

/**
 * @author Brito
 * @date: 2024-10-01
 * @location: Germany
 */
public class MapApp extends MapBox {

//    
//    Set<MapFolder> folders = new TreeSet<>();
//    Set<MapApp> apps = new TreeSet<>();
//    Set<MapCommand> commands = new TreeSet<>();
//    Set<MapFile> files = new TreeSet<>();

    public MapApp(App app) {
        super(MapType.APP, app.getIdName());
        this.relatedApp = app;
        app.setMap(this);
    }

//    @Override
//    public void index() {
//        if (appRelated == null) {
//            return;
//        }
//
//        // add the apps
//        indexApps();
//    }
//
//    private void indexApps() {
//        if (appRelated == null) {
//            return;
//        }
//        // add app apps
//        for (TerminalApp app : appRelated.appChildren) {
//            // create a new map app
//            MapApp mapApp = new MapApp(app);
//            mapApp.setAppRelated(app);
//            mapApp.setParent(this);
//            mapApp.index();
//            apps.add(mapApp);
//            // add the commands too
//            for (TerminalCommand command : app.commands.values()) {
//                MapCommand mapCmd = new MapCommand(command);
//                mapCmd.setParent(mapApp);
//                mapApp.addCommand(mapCmd);
//            }
//        }
//    }
//
//    public Set<MapApp> getApps() {
//        return this.apps;
//    }
//
//    public Set<MapFile> getFiles() {
//        return this.files;
//    }
//
//    public Set<MapFolder> getFolders() {
//        return this.folders;
//    }
//
//    public void addCommand(MapCommand mapCmd) {
//        commands.add(mapCmd);
//    }
//
//    public String getTree() {
//        StringBuilder treeBuilder = new StringBuilder();
//        buildTree(treeBuilder, "", true, true);
//        return treeBuilder.toString();
//    }
//
//    public void buildTree(StringBuilder builder, String prefix, boolean isTail, boolean isRoot) {
//        if(isRoot == false){
//        // Append the current app name
//        builder.append(prefix).append(isTail ? "└── " : "├── ")
//                .append("[app] ")
//                .append(getName())
//                .append("\n");
//        }
//        
//        // List the commands
//        int commandCount = commands.size();
//        int commandIndex = 0;
//        for (MapCommand command : commands) {
//            builder.append(prefix)
//                    .append(isTail ? "    " : "│   ")
//                    .append(commandIndex++ == commandCount - 1 ? "└── " : "├── ")
//                    //.append("[cmd] ")
//                    .append(command.getName())
//                    .append("\n");
//        }
//
//        // List the files
//        int fileCount = files.size();
//        int fileIndex = 0;
//        for (MapFile file : files) {
//            builder.append(prefix)
//                    .append(isTail ? "    " : "│   ")
//                    .append(fileIndex++ == fileCount - 1 ? "└── " : "├── ")
//                    .append("[file] ")
//                    .append(file.getName())
//                    .append("\n");
//        }
//
//        // List the folders
//        int folderCount = folders.size();
//        int folderIndex = 0;
//        for (MapFolder folder : folders) {
//            folder.buildTree(builder, prefix + (isTail ? "    " : "│   "), ++folderIndex == folderCount);
//        }
//
//        // List the apps (recursive)
//        int appCount = apps.size();
//        int appIndex = 0;
//        for (MapApp app : apps) {
//            app.buildTree(builder, prefix + (isTail ? "    " : "│   "), ++appIndex == appCount, false);
//        }
//    }

}
