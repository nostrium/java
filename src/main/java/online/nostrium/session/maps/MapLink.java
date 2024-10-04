/*
 * Define a map of files on the virtual file system
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.session.maps;

import online.nostrium.servers.terminal.TerminalApp;

/**
 * @author Brito
 * @date: 2024-10-04
 * @location: Germany
 */
public class MapLink extends Map{
    
    final TerminalApp app;

    public MapLink(String virtualPath, TerminalApp app) {
        super(MapType.LINK, virtualPath);
        this.app = app;
    }

    public TerminalApp getApp() {
        return app;
    }

}
