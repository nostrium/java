/*
 * GODS: Guardians of Decentralized Systems
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.games.gods.bank;

import online.nostrium.servers.terminal.notifications.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import static online.nostrium.servers.terminal.TerminalColor.GREEN;
import online.nostrium.utils.screens.Screen;
import online.nostrium.user.User;
import online.nostrium.servers.terminal.TerminalUtils;

/**
 * @author Brito
 * @date: 2024-08-19
 * @location: Germany
 */
public class TerminalGODS_Bank extends TerminalApp {

    public TerminalGODS_Bank(Screen screen, User user) {
        super(screen, user);
        // add some specific commands
//        addCommand(new CommandGodsClear(this));
//        addCommand(new CommandLs(this));
//        addCommand(new CommandGodsWalk(this));
    }

    @Override
    public String getDescription() {
        return "Bank system";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        return reply(TerminalCode.NOT_FOUND);
    }

    @Override
    public String getIntro() {
        
        
        String text = "Welcome to the Vault of GODS\n\n" +
              "In the ever-changing digital realm, security and strategy are as important as\n" +
              "strength. The Vault, known as the Bank of GODS, is where Guardians safeguard\n" +
              "their hard-earned resources, ensuring they have the means to continue their\n" +
              "quest for power and influence.\n\n" +
              "Here, you can deposit your digital wealth to protect it from loss, or withdraw\n" +
              "funds to finance your endeavors in the Arena, the marketplace, or elsewhere in\n" +
              "the realms. Interest accumulates over time, rewarding those who plan ahead and\n" +
              "save wisely.\n\n" +
              "The Vault is not just a place for storage, it is a cornerstone of your strategy\n" +
              "as a Guardian. Manage your assets well, and you will find yourself well-\n" +
              "prepared for the challenges ahead.\n\n" +
              "Step into the Bank of GODS, and take control of your destiny.";


        
        
        String intro = paint(GREEN, text);
        
        return intro;
    }

    @Override
    public String getName() {
        return "bank";
    }

    @Override
    public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
        screen.writeln("Received a notification");
    }
    
    @Override
    public String getId() {
        String path = TerminalUtils.getPath(this);
        return path;
    }

}
