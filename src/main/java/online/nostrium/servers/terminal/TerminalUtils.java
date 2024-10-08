/*
 * Internal utilities for the text terminals
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.terminal;

/**
 * Author: Brito
 * Date: 2024-08-04
 * Location: Germany
 */
public class TerminalUtils {
    

//    public static String getPath(TerminalApp app) {
//        return app.getMap().getPath();
////        if(app.appParent == null){
////            return "/";
////        }
////        // traverse the parent apps to build the path
////        String path = app.getPathWithName();
////        String subFolders = app.getSubFolders();
////        if(subFolders.isEmpty() == false){
////            path += subFolders;
////        }
////        TerminalApp appCurrent = app;
////        while(appCurrent.appParent.appParent != null){
////            appCurrent = appCurrent.appParent;
////            path = appCurrent.getPathWithName() + "/" + path;
////        }
////        path = "/" + path;
////        return path;
//    }

    public static TerminalApp getAppRoot(TerminalApp app) {
        TerminalApp appSelected = app;
        while(appSelected.appParent != null){
            appSelected = appSelected.appParent;
        }
        return appSelected;
    }
}
