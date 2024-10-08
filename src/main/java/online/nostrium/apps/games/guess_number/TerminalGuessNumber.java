/*
 * Guess a number
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.apps.games.guess_number;

import java.util.ArrayList;
import online.nostrium.session.NotificationType;
import online.nostrium.servers.terminal.CommandResponse;
import online.nostrium.servers.terminal.TerminalApp;
import online.nostrium.servers.terminal.TerminalCode;
import online.nostrium.user.User;
import online.nostrium.servers.terminal.TerminalColor;
import online.nostrium.servers.terminal.TerminalUtils;
import online.nostrium.session.Session;
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

    public TerminalGuessNumber(Session session) {
        super(session);
    }

    @Override
    public String getDescription() {
        return "Guess a number from "
                + min
                + " to "
                + max;
    }

    public void reset(boolean clearScreen) {
        tryCounter = 0;
        chosenNumber = MathFunctions.getRandomIntInRange(min+2, max-2);
        timeStart = System.currentTimeMillis();
        if(clearScreen){
            session.getScreen().clearScreen();
        }
    }

    @Override
    public CommandResponse defaultCommand(String commandInput) {

        // check if we are over the max
        if (tryCounter+1 == tryMax) {
            reset(false);
            return reply(TerminalCode.OK, "Sorry, failed to guess the number!"
                    + session.getScreen().breakLine()
                    + "The number was: " + chosenNumber
                    + "\n"
                    + "Please try with a new number, guess again:");
        }

        // reset the game when requested
        if (commandInput.equalsIgnoreCase("r")) {
            reset(true);
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

        int attemptsMissing = tryMax - tryCounter;
        
        String text = "Attempts left: " + attemptsMissing + " ";
        
        if(debug){
            text += "(value is " + chosenNumber + ") "; 
        }
        
        // number is valid
        int number = Integer.parseInt(commandInput);

        if (number > chosenNumber) {
            text += "-> Smaller than that";
        }

        if (number < chosenNumber) {
            text += "-> Bigger than that";
        }

        // great success!
        if (number == chosenNumber) {
            try{
                addRecordIfApplicable();
            }catch (Exception e){
                log(TerminalCode.CRASH, "Failed to write record", e.getMessage());
            }
            reset(false);
            text = session.getScreen().paint(TerminalColor.GREEN_BRIGHT, 
                    "Congratulations!");
        }

        return reply(TerminalCode.OK, text);
    }

    @Override
    public String getIntro() {
        this.reset(false);
        String text = "Guess the number";
        String intro = session.getScreen().getWindowFrame(text);
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
    public String getIdName() {
        return "guess";
    }

    @Override
    public void receiveNotification(User userSender, NotificationType notificationType, Object object) {
    }

    @Override
    public String getPathVirtual() {
        return session.getCurrentLocation().getPath();
    }

    /**
     * Add the new record to the top score
     */
    @SuppressWarnings("unchecked")
    private void addRecordIfApplicable() {
        long timeRequired = System.currentTimeMillis() - timeStart;
        UserRecordGuess scoreNow = new UserRecordGuess(
                this.tryCounter,
                session.getUser().getNpub(),
                System.currentTimeMillis(),
                timeRequired
        );

        // records existing
        ArrayList<UserRecordGuess> scores;
        String tagByAttempts = "score_by_attempts";
        if (dataUser.has(tagByAttempts) == false) {
            scores = new ArrayList();
            dataUser.put(tagByAttempts, scores);
        } else {
            scores = (ArrayList<UserRecordGuess>) dataUser.get(tagByAttempts);
        }

        // add the value
        if (scores.isEmpty()) {
            scores.add(scoreNow);
            dataUser.save();
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
            dataUser.put(tagByAttempts, scores);
            dataUser.save();
            // no need to proceed forward
            break;
        }

    }

}
