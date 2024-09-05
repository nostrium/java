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
    public abstract String processCommand(Scene scene);

    public abstract String performChoices(Scene scene);
    
}
