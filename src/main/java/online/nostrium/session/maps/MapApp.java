/*
 * Define an app on the virtual file system
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.session.maps;

import java.util.Set;
import java.util.TreeSet;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCommand;

/**
 * @author Brito
 * @date: 2024-10-01
 * @location: Germany
 */
public class MapApp extends Map{
    Map parent = null;
    Set<MapFolder> folders = new TreeSet<>();
    Set<MapApp> apps = new TreeSet<>();
    Set<MapCommand> commands = new TreeSet<>();
    Set<MapFile> files = new TreeSet<>();
    
    

    public MapApp(TerminalApp app) {
        super(MapType.APP, app.getPathWithName());
        this.appRelated = app;
    }
    
    @Override
    public void index(){
        if(appRelated == null){
            return;
        }
        
        // add the apps
        indexApps();
    }
    
    private void indexApps() {
        if(appRelated == null){
            return;
        }
        // add app apps
        for(TerminalApp app : appRelated.appChildren){
            // create a new map app
            MapApp mapApp = new MapApp(app);
            mapApp.setAppRelated(app);
            mapApp.setParent(this);
            mapApp.index();
            apps.add(mapApp);
            // add the commands too
            for(TerminalCommand command : app.commands.values()){
                MapCommand mapCmd = new MapCommand(command);
                commands.add(mapCmd);
            }
        }
    }

    public Map getParent() {
        return parent;
    }

    public void setParent(MapApp parent) {
        this.parent = parent;
    }
   
    public Set<MapApp> getApps(){
        return this.apps;
    }

    public Set<MapFile> getFiles(){
        return this.files;
    }

    public Set<MapFolder> getFolders(){
        return this.folders;
    }


    
}
