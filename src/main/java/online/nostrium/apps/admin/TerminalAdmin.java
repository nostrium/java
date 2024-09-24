/*
 * Panel for system administration
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.admin;

import online.nostrium.servers.terminal.notifications.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import static online.nostrium.servers.terminal.TerminalColor.GREEN;
import online.nostrium.utils.screens.Screen;
import online.nostrium.user.User;
import online.nostrium.user.UserType;

/**
 * @author Brito
 * @date: 2024-08-29
 * @location: Germany
 */
public class TerminalAdmin extends TerminalApp {
    

    public TerminalAdmin(Screen screen, User user) {
        super(screen, user);
         // add apps inside
        this.addCommand(new CommandRegisterSSL(this));
        this.addCommand(new CommandAdminLog(this));
        
        // make sure that only ADMIN can enter here
        permissions.clearEveryone();
        permissions.addUserType(UserType.ADMIN);
    }

    @Override
    public String getDescription() {
        return "Administration tools";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        return reply(TerminalCode.NOT_FOUND);
    }

    @Override
    public String getIntro() {
        String text = screen.getWindowFrame("Administration");
        String intro = paint(GREEN, text);
        return intro;
    }

    @Override
    public String getName() {
        return "admin";
    }

    @Override
    public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
    }
    

}
