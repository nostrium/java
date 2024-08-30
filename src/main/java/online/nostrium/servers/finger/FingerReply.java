/*
 * Data structure for Finger reply
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.servers.finger;

import online.nostrium.apps.user.User;

/**
 * @author Brito
 * @date: 2024-08-29
 * @location: Germany
 */
public class FingerReply {

    private String username;         // Stores the login name of the user.
    private String npub;             // Stores the npub of the user.
    private String realName;         // Stores the real name of the user.
    private String terminal;         // Stores the terminal or device the user is logged in from.
    private String idleTime;         // Stores how long the terminal has been idle.
    private String loginTime;        // Stores the time the user logged in.
    private String officeLocation;   // Stores the office or room number of the user.
    private String homeDirectory;    // Stores the path to the user's home directory.
    private String shell;            // Stores the shell being used by the user.
    private String mailStatus;       // Indicates whether the user has unread mail.
    private String plan;             // Stores the contents of the `.plan` file.

    public FingerReply() {
        // Default constructor
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(String idleTime) {
        this.idleTime = idleTime;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public String getShell() {
        return shell;
    }

    public void setShell(String shell) {
        this.shell = shell;
    }

    public String getMailStatus() {
        return mailStatus;
    }

    public void setMailStatus(String mailStatus) {
        this.mailStatus = mailStatus;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getNpub() {
        return npub;
    }

    public void setNpub(String npub) {
        this.npub = npub;
    }
    

    /**
     * Converts a User object to a FingerReply object.
     *
     * @param user The User object to convert.
     * @return A FingerReply object populated with data from the User object.
     */
    public static FingerReply convertUser(User user) {
        FingerReply reply = new FingerReply();

        reply.setUsername(noneWhenEmpty(user.getUsername()));
        reply.setNpub(noneWhenEmpty(user.getNpub()));
        reply.setRealName(noneWhenEmpty(user.getDisplayName()));
        reply.setLoginTime(noneWhenEmpty(user.getLastLoginTime()));
        reply.setOfficeLocation(noneWhenEmpty(user.getWebsite()));
        reply.setMailStatus(noneWhenEmpty(user.getNostrVerifiedAddress()));
        reply.setPlan(noneWhenEmpty(user.getAboutMe()));

        // Fields not directly available in User, setting defaults or placeholders
        reply.setTerminal("[N/A]");
        reply.setIdleTime("[N/A]");
        reply.setHomeDirectory("/home/" + user.getUsername());
        reply.setShell("/bin/bash");  // Assuming a common default shell

        return reply;
    }
    
    private static String noneWhenEmpty(String text) {
        if(text == null || text.isEmpty()){
            return "[none]";
        }else
            return text;
    }

    @Override
    public String toString() {
        return String.format(
                "Login: %s"
                + "\n"
                + "Name: %s"
                + "\n"
                + "NPub: %s"
                + "\n"
                + "Directory: %s"
                + "\n"
                + "Shell: %s"
                + "\n"
                + "On since %s on %s"
                + "\n"
                //+ "%s idle"
                //+ "\n"
                + "Office: %s"
                + "\n"
                + "Mail: %s"
                + "\n"
                + "Plan: %s"
                + "\n",
                username, realName, npub, 
                homeDirectory, shell, loginTime, 
                terminal, //idleTime,
                officeLocation, 
                mailStatus, plan
        );
    }
}
