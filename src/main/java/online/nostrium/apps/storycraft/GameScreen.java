/*
 * Defines an interaction screen with the game
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.storycraft;

/**
 * @author Brito
 * @date: 2024-09-04
 * @location: Germany
 */
public abstract class GameScreen {

    public abstract void writeln(String text);
    public abstract void writeln(String text, String value);
    public abstract void clearScreen();
    public abstract Choice processCommand(Scene scene);
    public abstract String processCommand(String... actions);
    
    public abstract Choice performChoices(Scene scene);

    public abstract void delay(int i); 

    public abstract String performChoices(String... actionsAvailable);
    
}
