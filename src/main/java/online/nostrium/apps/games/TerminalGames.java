/*
 * Games for fun and profit
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.games;

import online.nostrium.apps.games.gods.TerminalGODS;
import online.nostrium.apps.games.guess_number.TerminalGuessNumber;
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
public class TerminalGames extends TerminalApp {
    

    public TerminalGames(Session session) {
        super(session);
         // add apps inside
        addApp(new TerminalGODS(session));
        addApp(new TerminalGuessNumber(session));
    }

    @Override
    public String getDescription() {
        return "Games";
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {
        return reply(TerminalCode.NOT_FOUND);
    }

    @Override
    public String getIntro() {
        String text = "ASCII text games";
        String intro = paint(GREEN, text);
        return intro;
    }

    @Override
    public String getPathWithName() {
        return "games";
    }

    @Override
    public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
    }
    
    @Override
    public String getId() {
        String path = TerminalUtils.getPath(this);
        return path;
    }

}
