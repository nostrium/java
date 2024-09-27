/*
 * Define user permissions
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.utils.cybersec;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.HashMap;
import online.nostrium.user.User;
import online.nostrium.user.UserType;
import online.nostrium.folder.FolderUtils;
import online.nostrium.servers.terminal.AppData;

/**
 * @author Brito
 * @date: 2024-08-29
 * @location: Germany
 */
public final class Permissions {

    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<UserType> 
            userTypeAccessPermitted = new ArrayList(),
            userTypeAccessDenied = new ArrayList();

    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<String> usersPermitted = new ArrayList();

    @SuppressWarnings("unchecked")
    @Expose
    ArrayList<String> usersBanned = new ArrayList();

    @SuppressWarnings("unchecked")
    @Expose
    HashMap<String, Long> usersSuspended = new HashMap();

    // are these permissions associated to an app?
    private AppData data = null;

    public Permissions() {
    }

    public Permissions(AppData data) {
        this.data = data;
    }

    public Permissions(UserType... userTypes) {
        for(UserType userType : userTypes){
            addUserType(userType);
        }
    }

    public boolean isPermitted(User userToCheck) {
        
        // always permit when there is nothing defined
        if(userTypeAccessPermitted.isEmpty() 
                && userTypeAccessDenied.isEmpty()
                && usersPermitted.isEmpty()
                && usersBanned.isEmpty()
                && usersSuspended.isEmpty()){
            return true;
        }
        
        
        // first check: user types not permitted
        for (UserType userType : userTypeAccessDenied) {
            if (userToCheck.getUserType() == userType) {
                return false;
            }
        }
        // second check: users banned
        for (String npub : usersBanned) {
            if (npub.equalsIgnoreCase(userToCheck.getNpub())) {
                return false;
            }
        }
        // third check: is it a permitted user?
        for (String npub : usersPermitted) {
            if (npub.equalsIgnoreCase(userToCheck.getNpub())) {
                return true;
            }
        }
        // fourth check: is it a permitted user type?
        for (UserType userType : userTypeAccessPermitted) {
            if (userToCheck.getUserType() == userType) {
                return true;
            }
        }
        
        // there are rules to close the entrance
        // but no rules specifying who gets in at this stage
        // however, this account for sure was not banned
        if(userTypeAccessDenied.isEmpty() == false
                || usersBanned.isEmpty() == false){
            return true;
        }

        // no qualified situation? In that case permit
        return false;
    }

    /**
     * @param userTypeToAdd
     */
    public void addUserType(UserType userTypeToAdd) {
        if (userTypeAccessPermitted.contains(userTypeToAdd)) {
            return;
        }
        // if the user type was previously denied, remove it
        if (userTypeAccessDenied.contains(userTypeToAdd)) {
            userTypeAccessDenied.remove(userTypeToAdd);
        }
        // add it up
        userTypeAccessPermitted.add(userTypeToAdd);
        // make the changes permanent
        writeChangesToDisk();
    }

    /**
     * @param userTypeToDeny
     */
    public void denyUserType(UserType userTypeToDeny) {
        // avoid duplicates
        if(userTypeAccessDenied.contains(userTypeToDeny)){
            return;
        }
        // if the user type was previously permitted, remove it
        if (userTypeAccessPermitted.contains(userTypeToDeny)) {
            userTypeAccessPermitted.remove(userTypeToDeny);
        }
        userTypeAccessDenied.add(userTypeToDeny);
        writeChangesToDisk();
    }

    public void banUser(User userToBan) {
        if (usersBanned.contains(userToBan.getNpub()) == false) {
            usersBanned.add(userToBan.getNpub());
        }
        if (usersPermitted.contains(userToBan.getNpub())) {
            usersPermitted.remove(userToBan.getNpub());
        }
        // make the changes permanent
        writeChangesToDisk();

    }

    public void addUser(User userToAdd) {
        if (usersBanned.contains(userToAdd.getNpub())) {
            usersBanned.remove(userToAdd.getNpub());
        }
        if (usersPermitted.contains(userToAdd.getNpub()) == false) {
            usersPermitted.add(userToAdd.getNpub());
        }
        // make the changes permanent
        writeChangesToDisk();
    }

    public void suspendUser(User user, int days) {
        String npub = user.getNpub();
        // was suspended before? Add a new one
        if (usersSuspended.containsKey(npub)) {
            usersSuspended.remove(npub);
        }
        // Calculate the current time
        Long time = System.currentTimeMillis();

        // Calculate the future time when the suspension will end
        Long timeFuture = time + days * 24 * 60 * 60 * 1000L;

        // Update the suspension map
        usersSuspended.put(npub, timeFuture);
        // make the changes permanent
        writeChangesToDisk();
    }

    public boolean isStillSuspended(User user) {
        String npub = user.getNpub();

        // Check if the user is in the suspended map
        if (usersSuspended.containsKey(npub)) {
            Long suspensionEndTime = usersSuspended.get(npub);

            // Check if the current time is before the suspension end time
            if (System.currentTimeMillis() < suspensionEndTime) {
                return true; // The user is still suspended
            } else {
                // Suspension period has passed, remove the user from the map
                usersSuspended.remove(npub);
            }
        }

        return false; // The user is not suspended
    }

    /**
     * Write the changes to disk
     */
    private void writeChangesToDisk() {
        if (data == null) {
            return;
        }
        data.put(FolderUtils.namePermissions, this);
        data.save();
    }

    public void clearEveryone() {
        userTypeAccessDenied.clear();
        userTypeAccessPermitted.clear();
        usersPermitted.clear();
        usersBanned.clear();
        usersSuspended.clear();
    }

}
