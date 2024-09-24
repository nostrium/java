/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

import online.nostrium.user.User;
import online.nostrium.user.UserType;
import online.nostrium.user.UserUtils;
import online.nostrium.utils.cybersec.Permissions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Brito
 * @date: 2024-08-29
 * @location: Germany
 */
public class PermissionTest {
    
    public PermissionTest() {
    }

    @Test
    public void helloPermissions() {
        User user1 = UserUtils.createUserAnonymous();
        user1.setUserType(UserType.ADMIN);
        
        User user2 = UserUtils.createUserAnonymous();
        user2.setUserType(UserType.ANON);
        
        // set permission only for ADMIN
        Permissions permission = new Permissions(UserType.ADMIN);
        
        // should not be allowed
        assertFalse(
                permission.isPermitted(user2)
        );
        
        permission.addUserType(UserType.ANON);
        // should now be allowed
        assertTrue(permission.isPermitted(user2));
        
        // ban the user
        permission.banUser(user2);
        // should not be allowed
        assertFalse(permission.isPermitted(user2));
        
    }
}
