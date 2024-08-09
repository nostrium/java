/*
 * Internal utils for users
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.users;

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
        user.setDisplayName("Anonymouse#" + user.getNpub().substring(0, 4));
        String timestamp = TextFunctions.getDate();
        user.setRegisteredTime(timestamp);
        user.setLastLoginTime(timestamp);
        return user;
    }

    public static User getUser(String npub) {
        File folder = FileFunctions.getThirdLevelFolder(
                Folder.getFolderUsers(), npub, false);
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
