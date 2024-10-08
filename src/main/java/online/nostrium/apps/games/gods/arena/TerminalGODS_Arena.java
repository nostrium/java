/*
 * GODS: Guardians of Decentralized Systems
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.games.gods.arena;

import online.nostrium.session.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import static online.nostrium.servers.terminal.TerminalColor.GREEN;
import online.nostrium.user.User;
import online.nostrium.servers.terminal.TerminalUtils;
import online.nostrium.session.Session;

/**
 * @author Brito
 * @date: 2024-08-19
 * @location: Germany
 */
public class TerminalGODS_Arena extends TerminalApp {

    public TerminalGODS_Arena(Session session) {
        super(session);
        // add some specific commands
//        addCommand(new CommandGodsClear(this));
//        addCommand(new CommandLs(this));
//        addCommand(new CommandGodsWalk(this));
    }

    @Override
    public String getDescription() {
        return "Challenge other players and the masters";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        return reply(TerminalCode.NOT_FOUND);
    }

    @Override
    public String getIntro() {
        
        
        String text = "Welcome to the Arena of GODS\n\n" +
                    "In the heart of the digital realm lies the Arena, a battleground where Guardians\n" +
                    "test their skills, strategy, and strength. Here, you can challenge other\n" +
                    "Guardians at your level or above, proving your worth in the relentless pursuit\n" +
                    "of glory.\n\n" +
                    "The Arena is where reputations are forged and legends are born. Each victory\n" +
                    "not only brings you closer to mastering your abilities but also earns you the\n" +
                    "respect of your peers. Only the most courageous and skilled will rise through\n" +
                    "the ranks to achieve the next level of power and prestige.\n\n" +
                    "Are you ready to face your fellow Guardians and claim your place among the\n" +
                    "elite? Enter the Arena, and let the battle begin!";


        
        
        String intro = paint(GREEN, text);
        
        return intro;
    }

    @Override
    public String getIdName() {
        return "arena";
    }

    @Override
    public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
        session.getScreen().writeln("Received a notification");
    }
    
    @Override
    public String getPathVirtual() {
        return session.getCurrentLocation().getPath();
    }

}
