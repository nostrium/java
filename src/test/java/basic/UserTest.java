/*
 *  Test user creation/management
 *   
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

import network.nostrium.users.User;
import network.nostrium.users.UserType;
import network.nostrium.users.UserUtils;
import network.nostrium.utils.TextFunctions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Date: 2024-08-04
 * Place: Germany
 * @author brito
 */
public class UserTest {
    
    public UserTest() {
    }

    @Test
    public void helloUserAnonymous() {
    
        User user = UserUtils.createUserAnonymous();
        assertNotNull(user);
        assertNotNull(user.getNpub());
        assertNotNull(user.getNsec());
        assertNotNull(user.getDisplayName());
        assertEquals(UserType.ANON, user.getUserType());
        String timestamp = TextFunctions.getDate();
        assertEquals(user.getRegistered(), timestamp);
        assertEquals(user.getLastLogin(), timestamp);
        
    }
}
