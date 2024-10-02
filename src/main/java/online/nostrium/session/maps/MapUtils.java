/*
 * Utils for maps
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.session.maps;

import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-10-02
 * @location: Germany
 */
public class MapUtils {
    
    public static MapApp createInitialMap(Session session){
        TerminalApp app = session.getApp();
        MapApp mapApp = new MapApp(app);
        mapApp.index();
        session.setCurrentLocation(mapApp);
        return mapApp;
    }

}
