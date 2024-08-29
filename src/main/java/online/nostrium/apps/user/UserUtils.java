package online.nostrium.apps.user;

import java.io.File;
import java.util.ArrayList;
import online.nostrium.main.Folder;
import static online.nostrium.main.Folder.nameEndingJsonUser;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.utils.EncryptionUtils;
import online.nostrium.utils.FileFunctions;
import online.nostrium.utils.Log;
import static online.nostrium.utils.nostr.NostrUtils.generateNostrKeys;
import online.nostrium.utils.TextFunctions;
import static online.nostrium.utils.TextFunctions.sha256;

/**
 * @author Brito
 * @date: 2024-08-04
 * @location: Germany
 */
public class UserUtils {
    
    public static int 
            minCharacters = 4,
            maxCharacters = 64,
            maxCharactersAbout = 256;

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
    

    public static CommandResponse setAbout(User user, String text) {
        if(TextFunctions.isValidText(text) == false
                || text.length() > maxCharactersAbout){
            return reply(TerminalCode.FAIL, "Text is not valid, or too large");
        }
        
        String textCleaned = TextFunctions.cleanString(text);
        if(textCleaned.length() != text.length()){
            return reply(TerminalCode.FAIL, "Only alphanumeric characters are permitted");
        }
        
        // write the text
        user.setAboutMe(text);
        user.save();        
        
        return reply(TerminalCode.OK, "Done");
    }
    
    public static CommandResponse setUsername(User user, String text) {
        if(TextFunctions.isValidText(text) == false
                || text.length() > maxCharacters){
            return reply(TerminalCode.FAIL, "Invalid username");
        }
        
        String textCleaned = TextFunctions.cleanString(text);
        if(textCleaned.length() != text.length()){
            return reply(TerminalCode.FAIL, "Only alphanumeric characters are permitted");
        }
        
        if(text.contains(" ")){
            return reply(TerminalCode.FAIL, "User name cannot contain spaces");
        }
        
        // don't permit duplicate names
        User userSameUsername = UserUtils.getUserByUsername(text);
        if(userSameUsername != null){
            return reply(TerminalCode.FAIL, "User name already taken, please choose another one");
        }
        
        // set the user name
        user.setUsername(text);
        
        // no longer an anon, upgrade to member
        if(user.getUserType() == UserType.ANON){
            user.setUserType(UserType.MEMBER);
            user.setDisplayName(text);
        }
        
        // save the changes
        user.save();
        
        // all done
        return reply(TerminalCode.OK, "Done");
    }
    
    public static CommandResponse setWWW(User user, String text) {
        if(TextFunctions.isValidText(text) == false
                || text.length() > maxCharactersAbout){
            return reply(TerminalCode.FAIL, "Text is not valid, or too large");
        }
        
        // write the text
        user.setWebsite(text);
        user.save();        
        
        return reply(TerminalCode.OK, "Done");
    }

    public static CommandResponse setPassword(User user, String parameters) {
         if(user.username == null){
            return reply(TerminalCode.INCOMPLETE, "Please define your username before setting a password");
        }
        
        if(parameters == null){
            return reply(TerminalCode.INCOMPLETE, "Please include a password");
        }

        // clean the password
        parameters = TextFunctions.sanitizePassword(parameters);
        
        if(parameters.isEmpty()){
            return reply(TerminalCode.INCOMPLETE, "Please include a password (minimum "
                    + minCharacters
                    + " characters)");
        }
        
        if(parameters.length() < minCharacters){
            return reply(TerminalCode.FAIL, "Too short, needs at minimum "
                    + minCharacters
                    + " characters");
        }
        
        if(parameters.length() > maxCharacters){
            return reply(TerminalCode.FAIL, "Too long password, max is "
                    + maxCharacters
                    + " characters");
        }
        
        // never store the password, just its hashed version
        String passwordHash = sha256(parameters);
        
        // change the password and save to disk
        user.setPasswordHash(passwordHash);
        user.setPassword(parameters);
        
        // encrypt the nsec
        String nsec = user.getNsec();
        String nsecEnc = EncryptionUtils.encrypt(nsec, parameters);
        user.setNsecEncrypted(nsecEnc);
        
        user.save();
        
        return reply(TerminalCode.OK, "Done");
    }

    
    public static CommandResponse reply(TerminalCode code, String text){
        return new CommandResponse(code, text);
    }

    public static boolean isValidPassword(String password) {
        if(TextFunctions.sanitizePassword(password).length()
                != password.length()){
            return false;
        }
        if(password.length() > maxCharacters || password.length() < minCharacters){
            return false;
        }
        // all done
        return true;
    }

    public static boolean isValidUsername(String username) {
         if(TextFunctions.cleanString(username)
                 .equalsIgnoreCase(username) == false){
            return false;
        }
        if(username.length() > maxCharacters 
                || username.length() < minCharacters){
            return false;
        }
        // all done
        return true;
    }
    
}