/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

import java.io.File;
import online.nostrium.apps.basic.TerminalBasic;
import online.nostrium.folder.FolderUtils;
import online.nostrium.main.core;
import online.nostrium.session.ChannelType;
import online.nostrium.session.Session;
import online.nostrium.session.maps.MapApp;
import online.nostrium.session.maps.MapFolder;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import online.nostrium.utils.screens.ScreenCLI;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * @author Brito
 * @date: 2024-10-01
 * @location: Germany
 */
public class MapFileTest {
    
    public MapFileTest() {
    }

    @Test
    @SuppressWarnings("SizeReplaceableByIsEmpty")
    public void mapFolderTest() {
        MapFolder mapFolder = new MapFolder("/");
        File folderStart = FolderUtils.getFolderBase();
        mapFolder.setRealFile(folderStart);
        mapFolder.index();
        assertTrue(mapFolder.getFolders().size() > 0);
    }
    
    @Test
    @SuppressWarnings("SizeReplaceableByIsEmpty")
    public void mapAppTest() {
        core.startConfig();
        Session session = new Session(ChannelType.CLI, "test");
        ScreenCLI screen = new ScreenCLI(session);
        User user = UserUtils.createUserAnonymous();
        session.setUser(user);
        session.setScreen(screen);
        try{
            TerminalBasic app = new TerminalBasic(session);
            session.setup(app, UserUtils.createUserAnonymous(), screen);
        }catch (Exception e){
            e.printStackTrace();
        }
        
        MapApp map = (MapApp) session.getMap();
        
        assertTrue(map.getApps().size() > 0);
        
        System.out.println(map.getTree());
        
    }
    
}
