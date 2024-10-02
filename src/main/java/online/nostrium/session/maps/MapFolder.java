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
public class MapFolder extends Map {

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

    @Override
    public void index() {
        indexFolder();
    }

    public void indexFolder() {
        if (realFile == null || realFile.isFile()) {
            return;
        }

        // list the files
        File[] filesFound = realFile.listFiles();
        if (filesFound == null || filesFound.length == 0) {
            return;
        }
        // iterate all found files
        for (File item : filesFound) {
            if (item.isFile()) {
                MapFile mapFile = new MapFile(item.getName());
                mapFile.realFile = item;
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

    public void indexApps() {
        for (MapApp app : apps) {
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

    public String getTree() {
        StringBuilder treeBuilder = new StringBuilder();
        buildTree(treeBuilder, "", true);
        return treeBuilder.toString();
    }

    public void buildTree(StringBuilder builder, String prefix, boolean isTail) {
        // Append the current folder name
        builder.append(prefix)
                .append(isTail ? "└── " : "├── ")
                .append(getName())
                .append("\n");

        // List the folders (recursive)
        int folderCount = folders.size();
        int folderIndex = 0;
        for (MapFolder folder : folders) {
            folder.buildTree(builder, prefix + (isTail ? "    " : "│   "), ++folderIndex == folderCount);
        }

        // List the apps (recursive)
        int appCount = apps.size();
        int appIndex = 0;
        for (MapApp app : apps) {
            app.buildTree(builder, prefix + (isTail ? "    " : "│   "), ++appIndex == appCount, false);
        }

        // List the files
        int fileCount = files.size();
        int fileIndex = 0;
        for (MapFile file : files) {
            builder.append(prefix)
                    .append(isTail ? "    " : "│   ")
                    .append(fileIndex++ == fileCount - 1 ? "└── " : "├── ")
                    .append(file.getName())
                    .append("\n");
        }
    }

}
