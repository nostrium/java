package online.nostrium.servers.apps.user;

import java.io.File;
import java.util.ArrayList;
import online.nostrium.main.Folder;
import static online.nostrium.main.Folder.nameEndingJsonUser;
import online.nostrium.utils.FileFunctions;
import online.nostrium.utils.Log;
import static online.nostrium.utils.nostr.NostrUtils.generateNostrKeys;
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

    /**
     * Get all the user files saved on disk
     * @return 
     */
    public static ArrayList<File> getUserFiles(){
        File folder = Folder.getFolderUsers();
        return FileFunctions.searchFiles(
                        folder, nameEndingJsonUser
                );
    }
    
    /**
     * Finds an user based on username
     * @param username to be used
     * @return null when nothing was found
     */
    public static User getUserByUsername(String username){
        ArrayList<File> files = getUserFiles();
        if(files == null || files.isEmpty()){
            return null;
        }
        for(File file : files){
            User user = User.jsonImport(file);
            if(user == null){
                continue;
            }
            // too many users don't define the user name
            if(user.getUsername() == null){
                continue;
            }
            if(user.getUsername().equalsIgnoreCase(username)){
                return user;
            }
        }
        return null;
    }
    
    
    
    public static User getUserByNpub(String npub) {
        String npubId = npub.substring(5);
        File folder = FileFunctions.getThirdLevelFolderForUser(
                Folder.getFolderUsers(), npubId, false);
        File file = new File(folder, npub + nameEndingJsonUser);
        
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
    
    
    /**
     * Counts the number of valid users available.
     *
     * @return The count of valid User objects.
     */
    public static int countUsers() {
        int userCount = 0;
        File folderBase = Folder.getFolderUsers();
        
        // Adjust search pattern to match the new naming convention
        ArrayList<File> files = FileFunctions.searchFiles(folderBase, "-user.json");

        for (File file : files) {
            // Attempt to import the user from the file
            User user = User.jsonImport(file);
            if (user != null) {
                // User is valid, increment the count
                userCount++;
            } else {
                // User is invalid, log the issue
                Log.write("Invalid user file", file.getPath());
            }
        }

        return userCount;
    }
    

}
