/*
 * Story example
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.storycraft.examples;

/**
 * Randomly find items
 * 
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class StoryRandomItems {
    
public static String text = 
        
"""
   
# Scene: Azurath Entrance
> Welcome to the ancient ruins of Azurath. The air is thick with the scent of damp stone and decaying leaves. You are a treasure hunter, drawn by the legends of untold riches hidden within.
> The entrance to the ruins is overgrown with vines. Faded carvings on the stone walls hint at the once-great civilization that lived here.

> You step into the ruins, the sound of your footsteps echoing off the stone walls. The air is cooler inside, and a sense of foreboding settles over you.

## Random: You decide to...
- 50% [Explore the main hall](#scene-main-hall)
- 50% [Nothing happens](#scene-nothing-happens)

-----

# Scene: Main Hall
> The main hall is vast, with towering pillars and a high, vaulted ceiling. Broken statues and shattered pottery litter the floor.

## Choices:
- [Return to the entrance](#scene-azurath-entrance)
- [Nothing happens](#scene-nothing-happens)
- [Get rusty sword](#item-rusty-sword)
- [Exit the ruins](#scene-exit-ruins)

-----

# Scene: Nothing Happens
> You continue exploring the hall, but nothing unusual happens. The eerie silence only adds to your unease.

## Choices:
- [End your adventure](#scene-end)
- [Return to the entrance](#scene-azurath-entrance)

-----

# Scene: End
> Your adventure has come to an end. Perhaps one day you will return to the ruins of Azurath, but for now, you leave with the stories of what you encountered.
> Thank you for playing! The ruins of Azurath will await your return.

-----

# Item: Rusty Sword
Type: Weapon  
Description: A worn and damaged sword, but it still has some fight left in it.  
Attack Bonus: 3  
Durability: 10

-----


""";
   
}
