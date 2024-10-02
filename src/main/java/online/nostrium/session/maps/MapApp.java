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

/**
 * @author Brito
 * @date: 2024-10-01
 * @location: Germany
 */
public class MapApp extends Map{
    MapApp parent = null;
    Set<MapFolder> folders = new TreeSet<>();
    Set<MapApp> apps = new TreeSet<>();
    Set<MapFile> files = new TreeSet<>();
    
    

    public MapApp(TerminalApp app) {
        super(MapType.APP, app.getName());
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
        }
    }

    public Map getParent() {
        return parent;
    }

    public void setParent(MapApp parent) {
        this.parent = parent;
    }

   


    
}
