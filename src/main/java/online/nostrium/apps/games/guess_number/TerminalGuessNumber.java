/*
 * Guess a number
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.games.guess_number;

import java.util.ArrayList;
import online.nostrium.notifications.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import static online.nostrium.servers.terminal.TerminalColor.GREEN;
import online.nostrium.servers.terminal.screens.Screen;
import online.nostrium.apps.user.User;
import online.nostrium.servers.terminal.TerminalColor;
import online.nostrium.servers.terminal.TerminalUtils;
import online.nostrium.utils.Log;
import online.nostrium.utils.MathFunctions;
import online.nostrium.utils.TextFunctions;

/**
 * @author Brito
 * @date: 2024-08-19
 * @location: Germany
 */
public class TerminalGuessNumber extends TerminalApp {

    int tryCounter = 0;
    int tryMax = 6;
    int min = 1;
    int max = 100;
    int chosenNumber = MathFunctions.getRandomIntInRange(min, max);
    long timeStart = 0;
    int maxScoreRecords = 10;
    boolean debug = false;

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
        chosenNumber = MathFunctions.getRandomIntInRange(min+2, max-2);
        timeStart = System.currentTimeMillis();
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {

        // check if we are over the max
        if (tryCounter == tryMax) {
            reset();
            return reply(TerminalCode.OK, "Sorry, failed to guess the number!"
                    + "\n"
                    + "The number was: " + chosenNumber
                    + "\n"
                    + "Please try with a new number, guess again:");
        }

        // reset the game when requested
        if (commandInput.equalsIgnoreCase("r")) {
            reset();
            return reply(TerminalCode.OK, getIntro());
        }

        // check if the number is valid
        if (TextFunctions.isValidNumberInRange(
                commandInput, min, max) == false) {
            return reply(TerminalCode.OK, "Invalid number."
                    + "\n"
                    + "Please write a number between "
                    + min
                    + " and "
                    + max);
        }

        // increase the counter
        tryCounter++;

        String text = "Attempt #" + tryCounter;
        
        if(debug){
            text += " (value is " + chosenNumber + ")"; 
        }
        
        this.screen.writeln(text);

        // number is valid
        int number = Integer.parseInt(commandInput);

        if (number > chosenNumber) {
            return reply(TerminalCode.OK, "Smaller than that");
        }

        if (number < chosenNumber) {
            return reply(TerminalCode.OK, "Bigger than that");
        }

        // great success!
        if (number == chosenNumber) {
            try{
                addRecordIfApplicable();
            }catch (Exception e){
                Log.write("Exception", e.getMessage());
            }
            reset();
            return reply(TerminalCode.OK,
                    screen.paint(TerminalColor.GREEN_BRIGHT, "Congratulations!")
            );
        }

        return reply(TerminalCode.OK, text);
    }

    @Override
    public String getIntro() {
        String text = "Guess the number";
        String intro = screen.getWindowFrame(GREEN, text);
        intro += "\n"
                + "\n"
                + "You have 6 attempts to guess a number between 1 and 100"
                + "\n"
                + "On each attempt I'll say if the number is higher or lower"
                + "\n"
                + "Press (r) to reset at any time, or type the numbers to be guessed."
                + "\n"
                + "Write a number bellow and press ENTER to get started."
                + "\n"
                ;

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

    /**
     * Add the new record to the top score
     */
    @SuppressWarnings("unchecked")
    private void addRecordIfApplicable() {
        long timeRequired = System.currentTimeMillis() - timeStart;
        UserRecordGuess scoreNow = new UserRecordGuess(
                this.tryCounter,
                this.user.getNpub(),
                System.currentTimeMillis(),
                timeRequired
        );

        // records existing
        ArrayList<UserRecordGuess> scores;
        String tagByAttempts = "score_by_attempts";
        if (data.has(tagByAttempts) == false) {
            scores = new ArrayList();
            data.put(tagByAttempts, scores);
        } else {
            scores = (ArrayList<UserRecordGuess>) data.get(tagByAttempts);
        }

        // add the value
        if (scores.isEmpty()) {
            scores.add(scoreNow);
            data.save();
            return;
        }

        // only add if less attempts
        for (int i = 0; i < scores.size(); i++) {
            UserRecordGuess score = scores.get(i);
            // is this a new record or not?
            if (scoreNow.attempts > score.attempts) {
                continue;
            }
            // it is a new record, move all the items below
            scores.add(i, scoreNow);
            // remove the last placed
            if (scores.size() >= maxScoreRecords) {
                scores.remove(scores.size() - 1);
            }
            // save the result
            data.put(tagByAttempts, scores);
            data.save();
            // no need to proceed forward
            break;
        }

    }

}
