/*
 * Internal utils for users
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.apps.user;

import java.io.File;
import online.nostrium.main.Folder;
import online.nostrium.utils.FileFunctions;
import static online.nostrium.utils.NostrUtils.generateNostrKeys;
import online.nostrium.utils.TextFunctions;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class UserUtils {

    /**
     * Create an admin user with nsec and npub set to all zeros
     *
     * @return user that was automatically generated
     */
    public static User getUserAdmin() {
        User user = new User();
        // set nsec and npub to all zeros
        String zeroKeys = "00000000000000000000000000000000";
        user.setNsec(zeroKeys);
        user.setNpub(zeroKeys);
        user.setDisplayName("admin");
        String timestamp = "2000-01-01T00:00:00Z";
        user.setRegisteredTime(timestamp);
        user.setLastLoginTime(timestamp);
        return user;
    }
    
    public static String getAnonUserDisplayName(String npub){
        return Folder.nameAnonUsers
                + "#" + npub.substring(5, 8).toUpperCase();
    }
    
    public static String getUserDisplayNameBasic(String npub){
        return npub.substring(5, 8).toUpperCase();
    }
    
    

    /**
     * Create an anonymous user
     *
     * @return user that was automatically generated
     */
    public static User createUserAnonymous() {
        User user = new User();
        // generate random nostr keys
        String[] keys = generateNostrKeys();
        user.setNsec(keys[0]);
        user.setNpub(keys[1]);
        String name = getAnonUserDisplayName(user.getNpub());
        user.setDisplayName(name);
        String timestamp = TextFunctions.getDate();
        user.setRegisteredTime(timestamp);
        user.setLastLoginTime(timestamp);
        return user;
    }

    public static User getUser(String npub) {
        String npubId = npub.substring(5);
        File folder = FileFunctions.getThirdLevelFolder(
                Folder.getFolderUsers(), npubId, false);
        File file = new File(folder, npub + ".json");
        
        // user not found
        if(file.exists() == false){
            return null;
        }        
        
        User user = User.jsonImport(file);
        
        if(user == null){
            return null;
        }
        
        return user;
    }

}
