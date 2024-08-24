/*
 * Guess a number
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.games.guess_number;

import online.nostrium.notifications.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import static online.nostrium.servers.terminal.TerminalColor.GREEN;
import online.nostrium.servers.terminal.screens.Screen;
import online.nostrium.apps.user.User;
import online.nostrium.servers.terminal.TerminalUtils;

/**
 * @author Brito
 * @date: 2024-08-19
 * @location: Germany
 */
public class TerminalGuessNumber extends TerminalApp {

    String[] tries = new String[]{"First try",
        "Second try",
        "Third try",
        "Fourth try",
        "Fifth try",
        "Sixth try"
    };

    int tryCounter = 1;
    int tryMax = 6;
    int min = 1;
    int max = 100;

    public TerminalGuessNumber(Screen screen, User user) {
        super(screen, user);
    }

    @Override
    public String getDescription() {
        return "Guess a number from "
                + min
                + " to "
                + max;
    }

    public void reset() {
        tryCounter = 0;
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {

        // reset the game
        if (commandInput.equalsIgnoreCase("r")) {
            reset();
            return reply(TerminalCode.OK, "reset");
        }

        tryCounter++;

        String text = "";
        +"Current tries: " + tryCounter;

        return reply(TerminalCode.OK, text);
    }

    @Override
    public String getIntro() {
        String text = "Guess the number";
        String intro = screen.getWindowFrame(GREEN, text);
        intro += "\n"
                + "You have 6 attempts to guess a number between 1 and 100"
                + "\n"
                + "On each attempt I'll say if the number is higher or lower"
                + "\n"
                + "Press (r) to reset at any time, or type the numbers to be guessed.";

        return intro;
    }

    @Override
    public String getName() {
        return "guess";
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
