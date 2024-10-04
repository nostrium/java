/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

import java.io.File;
import java.util.TreeSet;
import online.nostrium.apps.basic.TerminalBasic;
import online.nostrium.folder.FolderUtils;
import online.nostrium.main.core;
import online.nostrium.session.ChannelType;
import online.nostrium.session.Session;
import online.nostrium.session.maps.Map;
import online.nostrium.session.maps.MapApp;
import online.nostrium.session.maps.MapFolder;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import online.nostrium.utils.screens.ScreenCLI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        ScreenCLI screen = new ScreenCLI();
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
        
        assertNotNull(map);
        assertTrue(map.getApps().size() > 0);
        
        System.out.println(map.getTree());
        
        // test that we can navigate
        String path = "/user/forum";
        Map test1 = map.findPath(path);
        assertEquals("forum", test1.getName());
        
        // test the ../
        Map test2 = test1.findPath("../");
        assertEquals("user", test2.getName());
        
        // test the ..
        Map test2_1 = test1.findPath("..");
        assertEquals("user", test2_1.getName());
        
        
        // test the ../
        Map test3 = test2.findPath("/");
        assertEquals("basic", test3.getName());
        
        // test jumping ../
        Map test4 = test1.findPath("../blog");
        assertEquals("blog", test4.getName());
        
        // test ./
        Map test5 = test4.findPath("./blog");
        assertEquals("blog", test5.getName());
        
        // test multiple ../../
        Map test6 = test1.findPath("../../email");
        assertEquals("email", test6.getName());
    }
    
    @Test
    @SuppressWarnings("SizeReplaceableByIsEmpty")
    public void mapListTest() {
        core.startConfig();
        Session session = new Session(ChannelType.CLI, "test");
        ScreenCLI screen = new ScreenCLI();
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
        
        // test that we can navigate
        String path = "/user/";
        Map test1 = map.findPath(path);
        assertEquals("user", test1.getName());
        
        TreeSet<Map> list = test1.listFiles("");
        assertEquals(3, list.size());
    }
    
}
