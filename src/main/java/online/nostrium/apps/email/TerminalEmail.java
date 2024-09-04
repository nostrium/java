/*
 * Panel for system administration
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.email;

import online.nostrium.servers.terminal.notifications.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.servers.terminal.screens.Screen;
import online.nostrium.user.User;
import online.nostrium.user.UserType;
import static online.nostrium.servers.email.EmailUtils.createFoldersBasic;
import online.nostrium.servers.terminal.TerminalUtils;

/**
 * @author Brito
 * @date: 2024-09-02
 * @location: Germany
 */
public class TerminalEmail extends TerminalApp {
    

    public TerminalEmail(Screen screen, User user) {
        super(screen, user);
         // add apps inside
        this.removeCommand("ls");
        this.addCommand(new CommandEmailLs(this));
        
        // make sure that only ADMIN can enter here
        permissions.clearEveryone();
        permissions.denyUserType(UserType.ANON);
        
        createFoldersBasic(user);
    }

    @Override
    public String getDescription() {
        return "Send and receive emails";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        return reply(TerminalCode.NOT_FOUND);
    }

    @Override
    public String getIntro() {
        screen.writeln(screen.getWindowFrame("Email"));
        return "Type 'help' to see the available commands";
    }

    @Override
    public String getName() {
        return "email";
    }

    // shows an intro for this app
    public String getId(){
        String path = TerminalUtils.getPath(this);
        return path;
    }
    

}
