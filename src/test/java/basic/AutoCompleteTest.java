/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

import online.nostrium.apps.basic.TerminalBasic;
import online.nostrium.main.core;
import online.nostrium.session.ChannelType;
import online.nostrium.session.Session;
import online.nostrium.session.maps.AutoComplete;
import online.nostrium.session.maps.Map;
import online.nostrium.session.maps.MapApp;
import online.nostrium.session.maps.MapBox;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import online.nostrium.utils.screens.ScreenCLI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Brito
 * @date: 2024-10-07
 * @location: Germany
 */
public class AutoCompleteTest {
    
    public AutoCompleteTest() {
    }

   @Test
    @SuppressWarnings("SizeReplaceableByIsEmpty")
    public void mapTabAutocompleteTest() {
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
        
        // basic testings
        MapApp map = (MapApp) session.getMap();
        assertTrue(map.getApps().size() > 0);
        System.out.println(map.getTree());
        
        // test that we can navigate
        String path = "/user/";
        Map test1 = map.findPath(path);
        assertEquals("user", test1.getName());
        
        MapBox map1 = (MapBox) test1;
        
        // now test that see commands
        String suggestion = AutoComplete.autoComplete("c", map1);
        assertEquals("cd | clear", suggestion);
        
        suggestion = AutoComplete.autoComplete("cd ", map1);
        assertEquals("blog | forum | public", suggestion);
        
        suggestion = AutoComplete.autoComplete("cd p", map1);
        assertEquals("cd public", suggestion);
        
        String emptyResult = AutoComplete.autoComplete("cd x", map1);
        assertEquals("", emptyResult);
        
        
    }
}
