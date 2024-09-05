/*
 * Defines an interaction screen with the game
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.storycraft;

import online.nostrium.utils.time;

/**
 * @author Brito
 * @date: 2024-09-04
 * @location: Germany
 */
public abstract class GameScreen {

    public abstract void writeln(String text);
    public abstract Choice processCommand(Scene scene);

    public abstract Choice performChoices(Scene scene);

    public abstract void delay(int i); 
    
}
