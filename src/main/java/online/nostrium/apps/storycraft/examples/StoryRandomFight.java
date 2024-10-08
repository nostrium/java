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
public class StoryRandomFight {
    
public static String text = 
        
"""

# Scene: Azurath Entrance
> Welcome to the ancient ruins of Azurath. The air is thick with the scent of damp stone and decaying leaves.

## Choice: You look around and...
- [Fight the stone Golem](#opponent-stone-golem): #scene-victory
- [End the game](#scene-end)

------

# Scene: Victory
> You have defeated the adversary. The land now rests safe and sound, for today.

## Choice: Let's go away
- [End the game](#scene-end)

------

# Scene: End
> Your adventure has come to an end. Perhaps one day you will return to the ruins of Azurath, but for now, you leave with the stories of what you encountered.
> Thank you for playing! The ruins of Azurath will await your return.

------

# Action: Attack
> Define what happens when Player A attacks player B

- AttackPower = A:Attack + (A:Experience / (A:Attack * 0.5))
- DefendPower = B:Defense + (B:Experience / (B:Defense * 0.5))
- B:Health = B:Health - chooseGreater(0, AttackPower - DefendPower)
- If A:Health < 0 then write "You have lost"; A:Coins = 0; #scene-end
- If B:Health < 0 then write "You have won!"; A:Coins = A:Coins + 10

# Player
- Health: 60
- Attack: 50
- Defense: 5
- Experience: 30
- Coins = 0
                  
# Opponent: Stone Golem
- Actions: Attack
- Health: 60
- Attack: 10
- Defense: 5
- Experience: 30

""";
   
}
