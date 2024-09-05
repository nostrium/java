/*
 * Story example
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */

package online.nostrium.apps.storycraft.examples;

/**
 * Basic navigation of rooms
 * 
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class StoryRandomRooms {
    
public static String text = 
        
"""
   
# Scene: Azurath Entrance
> Welcome to the ancient ruins of Azurath. The air is thick with the scent of damp stone and decaying leaves. You are a treasure hunter, drawn by the legends of untold riches hidden within.
> The entrance to the ruins is overgrown with vines. Faded carvings on the stone walls hint at the once-great civilization that lived here.

> You step into the ruins, the sound of your footsteps echoing off the stone walls. The air is cooler inside, and a sense of foreboding settles over you.

## Random: You decide to...
- 40% [Explore the main hall](#scene-main-hall)
- 40% [Leave the ruins](#scene-exit-ruins)
- 20% [Nothing happens](#scene-nothing-happens)

-----

# Scene: Main Hall
> The main hall is vast, with towering pillars and a high, vaulted ceiling. Broken statues and shattered pottery litter the floor.

## Intro
As you move deeper into the hall, 
you notice something glinting in the dim light. It appears to be a small, ornate chest partially buried under rubble.

## Choices:
- [Return to the entrance](#scene-azurath-entrance)
- [Nothing happens](#scene-nothing-happens)
- [Exit the ruins](#scene-exit-ruins)

-----

# Scene: Nothing Happens
> You continue exploring the hall, but nothing unusual happens. The eerie silence only adds to your unease.

## Choices:
- [Return to the entrance](#scene-azurath-entrance)
- [Exit the ruins](#scene-exit-ruins)

-----

# Scene: Exit Ruins
> You make your way out of the ruins, the sunlight blinding you as you emerge. The treasure of Azurath remains hidden, but you live to tell the tale.
>As you leave the ruins behind, you reflect on your journey. There are still many secrets to uncover, but for now, your adventure has come to an end.

## Choices:
- [Return to the entrance for another exploration](#scene-azurath-entrance)
- [End your adventure](#scene-end)

-----

# Scene: End
> Your adventure has come to an end. Perhaps one day you will return to the ruins of Azurath, but for now, you leave with the stories of what you encountered.
> Thank you for playing! The ruins of Azurath will await your return.

""";
   
}
