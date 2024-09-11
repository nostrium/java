/*
 * GODS: Guardians of Decentralized Systems
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.games.gods;

import online.nostrium.apps.basic.CommandLs;
import online.nostrium.servers.terminal.notifications.NotificationType;
import online.nostrium.apps.games.gods.arena.TerminalGODS_Arena;
import online.nostrium.apps.games.gods.bank.TerminalGODS_Bank;
import online.nostrium.apps.games.gods.walk.TerminalGODS_Walk;
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
public class TerminalGODS extends TerminalApp {
    
    public GODS_Status status = GODS_Status.loadFromFile(this);

    public TerminalGODS(Screen screen, User user) {
        super(screen, user);
        // add some specific commands
        addCommand(new CommandGodsClear(this));
        addCommand(new CommandLs(this));
        addCommand(new CommandGods_Status(this));
        
         // add apps inside
        addApp(new TerminalGODS_Walk(screen, user));
        addApp(new TerminalGODS_Arena(screen, user));
        addApp(new TerminalGODS_Bank(screen, user));
    }

    @Override
    public String getDescription() {
        return "Guardians Of Decentralized Systems";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        return reply(TerminalCode.NOT_FOUND);
    }

    @Override
    public String getIntro() {
        
        
        String text = "GODS: Guardians of Decentralized Systems\n\n" +
              "In a digital realm where freedom and privacy once flourished, a new threat has\n" +
              "arisen—the Blockchain. Once a tool for decentralization, it has grown sentient,\n" +
              "seeking to dominate and control all systems it touches. Its chains tighten\n" +
              "around the decentralized realms, strangling the privacy and freedoms it was\n" +
              "meant to protect.\n\n" +
              "You, a newly chosen Guardian, must confront this rogue entity. Your journey\n" +
              "will take you through vast data plains and encrypted fortresses as you gather\n" +
              "allies and master the forgotten arts of true decentralization and privacy.\n\n" +
              "Your mission: dismantle the Blockchain’s growing power and restore the balance\n" +
              "and privacy that once kept the digital realms free. The fate of this world\n" +
              "depends on your courage and skill.\n\n" +
              "Will you rise to the challenge and defend the realms as one of the GODS?";


        
        
        String intro = paint(GREEN, text);
        
        return intro;
    }

    public GODS_Status getStatus() {
        return status;
    }
    

    @Override
    public String getName() {
        return "gods";
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
