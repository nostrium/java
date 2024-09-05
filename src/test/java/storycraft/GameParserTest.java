/*
 * Unit tests for the GameParser class, which parses the game script and loads the scenes and items.
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package storycraft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import online.nostrium.apps.storycraft.GamePlay;
import online.nostrium.apps.storycraft.GameScreen;
import online.nostrium.apps.storycraft.GameScreenCLI;
import online.nostrium.apps.storycraft.Item;
import online.nostrium.apps.storycraft.Opponent;
import online.nostrium.apps.storycraft.Scene;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Brito
 * @date: 2024-09-03
 * @location: Germany
 */
public class GameParserTest {

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testParseScriptFromString() throws IOException {

        String script = """
                       
# Scene: Azurath Entrance
> Welcome to the ancient ruins of Azurath. The air is thick with the scent of damp stone and decaying leaves.
> You see a sword in the entrance, seems to have been lost by someone running away
                        
## Choices:
- [Take](#item-sword)
- [Move forward](#scene-main-hall)

## Item: Sword
Type: Weapon  
Description: A worn and damaged sword, but it still has some fight left in it.
Attack Bonus: 3  
Durability: 10

------------

# Scene: Main Hall
> The main hall is vast, with towering pillars and a high, vaulted ceiling.

## Item: Ancient Shield
Type: Shield  
Description: A shield from a bygone era, worn but sturdy.  
Defense Bonus: 5  
Durability: 20

## Random: You look around and...
- 30% chance: [Find a hidden alcove with a shield](#scene-find-shield)
- 20% chance: [Nothing happens](#scene-nothing-happens)
- 20% chance: [Find a pot of coins](#scene-find-coins)
- 30% chance: [Fight a Skeleton Warrior](#scene-take-treasure)

## Choices:
- [Run back to the entrance](#scene-azurath-entrance)
- [Move forward to Treasure room](#scene-take-treasure)
          
-----
                        
# Scene: Nothing Happens
> You continue exploring the hall, but nothing unusual happens. The eerie silence only adds to your unease.

## Choices:
- [Continue exploring the hall](#scene-main-hall)

-----
                                                         
# Scene: Find Shield
> As you clear away the rubble, you discover a hidden alcove containing an ancient shield. It's battered, but it might still offer some protection.

## Item: Ancient Shield
Type: Shield  
Description: A shield from a bygone era, worn but sturdy.  
Defense Bonus: 5  
Durability: 20

## Choices:
- [Take the shield](#item-find-shield)
- [Go back to the main hall](#scene-main-hall)                        
                             
-----
                        
# Scene: Find Coins
> While exploring the hall, you stumble upon a small pot. Inside, you find a stash of coins, glinting in the dim light.

## Choices:
- [Take coins](#item-coins-10-30)
- [Continue exploring the hall](#scene-main-hall)

# Item: Coins (10-30)
Type: Currency  
Description: A pot of coins found in the ruins.  
Coins: 10-30                      
                        
-----

# Scene: Take Treasure
> As you gather the treasure, you hear a low growl from behind you. You turn to see a massive stone golem, awakened by your greed.

                                              
## Opponent: Stone Golem
- Health: 150
- Attack: 20
- Defense: 15
- Experience: 100

## If win:
- [Take](#item-golem-heart)
- [Exit the ruins](#scene-exit-ruins)

## If lose:
- [Lose](#item-coins-20-25)
- [Return to the entrance](#scene-azurath-entrance)

## If run:
- [Lose](#item-coins-10-15)
- [Return to the entrance](#scene-azurath-entrance)

                        
-----
                        
# Scene: Exit Ruins
> You make your way out of the ruins, the sunlight blinding you as you emerge. The treasure of Azurath remains hidden, but you live to tell the tale.

## Intro
As you leave the ruins behind, you reflect on your journey. There are still many secrets to uncover, but for now, your adventure has come to an end.

## Choices:
- [Return to the entrance for another exploration](#scene-azurath-entrance)
- [End your adventure](#scene-end)

-----

# Scene: Leave Ruins
> Deciding that the ruins are too dangerous, you turn back and leave, vowing to return another day.

## Choices:
- [Return to the entrance](#scene-azurath-entrance)
- [End your adventure](#scene-end)

-----                        
                        
""";

        GameScreen screen = new GameScreenCLI();
        GamePlay game = new GamePlay(script, screen);

        assertTrue(game.isValid());

        Map<String, Scene> scenes = game.getScenes();

        // Verify scenes are correctly parsed
        assertNotNull(scenes);
        assertEquals(8, scenes.size());

        // Check the first scene
        Scene entranceScene = scenes.get("scene-azurath-entrance");
        assertNotNull(entranceScene);
        assertEquals(2, entranceScene.getChoices().size());

//        assertEquals("scene-main-hall", entranceScene.getNextScene());

        // Check the second scene
        Scene mainHallScene = scenes.get("scene-main-hall");
        assertNotNull(mainHallScene);
        assertEquals("The main hall is vast, with towering pillars and a high, vaulted ceiling.", mainHallScene.getDescription());

        // test the main title for the random choice
        String random = mainHallScene.getTitleRandom();
        assertEquals("You look around and...", random);
        
        // item testing
        assertEquals(1, entranceScene.getItems().size());
        Item sword = entranceScene.getItems().get(0);
        assertEquals(4, mainHallScene.getRandom().size());
        assertNotNull(sword);
        assertEquals("Sword", sword.getName());
        assertEquals("Weapon", sword.getType());
        assertEquals("A worn and damaged sword, but it still has some fight left in it.", sword.getDescription());

        // opponent
        Scene sceneTreasure = game.getScene("# Scene: Take Treasure");
        assertNotNull(sceneTreasure);
        
        assertFalse(sceneTreasure.getOpponents().isEmpty());
        Opponent op = sceneTreasure.getOpponents().get(0);
        
        assertNotNull(op);
        
        assertEquals(2, op.getMatchWin().size());
        
    }

}
