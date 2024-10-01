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
public class MapKey {

    String virtualPath;
    File realFile;
    TerminalApp appRelated;
    
}
