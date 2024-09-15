/*
 * Unit tests for the GameParser class, which parses the game script and loads the scenes and items.
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package storycraft;

import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import online.nostrium.apps.storycraft.GamePlay;
import online.nostrium.apps.storycraft.GameScreen;
import online.nostrium.apps.storycraft.GameScreenCLI;
import online.nostrium.apps.storycraft.Item;
import online.nostrium.apps.storycraft.Opponent;
import online.nostrium.apps.storycraft.Actions;
import online.nostrium.apps.storycraft.Choice;
import online.nostrium.apps.storycraft.LinkType;
import online.nostrium.apps.storycraft.Player;
import online.nostrium.apps.storycraft.Scene;
import online.nostrium.apps.storycraft.StoryUtils;
import online.nostrium.main.Folder;
import online.nostrium.user.User;
import online.nostrium.user.UserUtils;
import org.apache.commons.io.FileUtils;

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

        String script = 
"""
                       
# Scene: Azurath Entrance
> Welcome to the ancient ruins of Azurath. The air is thick with the scent of damp stone and decaying leaves.
> You see a sword in the entrance, seems to have been lost by someone running away
                        
## Choice:
- [Take](#item-sword)
- [Move forward](#scene-main-hall)

# Item: Sword
Type: Weapon  
Description: A worn and damaged sword, but it still has some fight left in it.
Attack Bonus: 3  
Durability: 10

------------

# Scene: Main Hall
> The main hall is vast, with towering pillars and a high, vaulted ceiling.

## Random: You look around and...
- 30% chance: [Find a hidden alcove with a shield](#scene-find-shield)
- 20% chance: [Nothing happens](#scene-nothing-happens)
- 20% chance: [Find a pot of coins](#scene-find-coins)
- 30% chance: [Fight a Skeleton Warrior](#scene-take-treasure)

## Choice:
- [Run back to the entrance](#scene-azurath-entrance)
- [Move forward to Treasure room](#scene-take-treasure)
          
-----
                        
# Scene: Nothing Happens
> You continue exploring the hall, but nothing unusual happens. The eerie silence only adds to your unease.

## Choice:
- [Continue exploring the hall](#scene-main-hall)

-----
                                                         
# Scene: Find Shield
> As you clear away the rubble, you discover a hidden alcove containing an ancient shield. It's battered, but it might still offer some protection.

## Choice:
- [Take the shield](#item-find-shield)
- [Go back to the main hall](#scene-main-hall)                        
                             
-----
                        
# Scene: Find Coins
> While exploring the hall, you stumble upon a small pot. Inside, you find a stash of coins, glinting in the dim light.

## Choice:
- [Take coins](#item-coins-10-30)
- [Continue exploring the hall](#scene-main-hall)

# Item: Coins (10-30)
Type: Currency  
Description: A pot of coins found in the ruins.  
Coins: 10-30                      
                        
-----

# Scene: Take Treasure
> As you gather the treasure, you hear a low growl from behind you. You turn to see a massive stone golem, awakened by your greed.

## Choice:
- [Take coins](#item-coins-10-30)
- [Continue exploring the hall](#scene-main-hall)
                        
-----
                        
# Scene: Exit Ruins
> You make your way out of the ruins, the sunlight blinding you as you emerge. The treasure of Azurath remains hidden, but you live to tell the tale.

## Intro
As you leave the ruins behind, you reflect on your journey. There are still many secrets to uncover, but for now, your adventure has come to an end.

## Choice:
- [Return to the entrance for another exploration](#scene-azurath-entrance)
- [End your adventure](#scene-end)

-----

# Scene: Leave Ruins
> Deciding that the ruins are too dangerous, you turn back and leave, vowing to return another day.

## Choice:
- [Return to the entrance](#scene-azurath-entrance)
- [End your adventure](#scene-end)

-----                        
           

# Item: Ancient Shield 2
Type: Shield  
Description: A shield from a bygone era, worn but sturdy.  
Defense Bonus: 5  
Durability: 20

# Item: Ancient Shield 1
Type: Shield  
Description: A shield from a bygone era, worn but sturdy.  
Defense Bonus: 5  
Durability: 20

                                                          
# Opponent: Stone Golem
- Health: 150
- Attack: 20
- Defense: 15
- Experience: 100            
 
""";

        GameScreen screen = new GameScreenCLI();
        User user = UserUtils.createUserAnonymous();
        GamePlay game = new GamePlay(script, screen, user);
        
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
        assertEquals(4, game.getItems().size());
        Item sword = game.getItems().get(0);
        assertEquals(4, mainHallScene.getRandom().size());
        assertNotNull(sword);
        assertEquals("Sword", sword.getName());
        assertEquals("Weapon", sword.getType());
        assertEquals("A worn and damaged sword, but it still has some fight left in it.", sword.getDescription());

        // opponent
        Scene sceneTreasure = game.getScene("# Scene: Take Treasure");
        assertNotNull(sceneTreasure);

        assertFalse(game.getOpponents().isEmpty());
        // get the first opponent listed
        String opId = "opponent-stone-golem";
        Opponent op = game.getOpponents().get(opId);

        assertNotNull(op);
        //assertEquals(2, op.getMatchWin().size());

    }

    @Test
    public void testRules() {

        String script2
                = """
                  
# Scene: Azurath Entrance
> Welcome to the ancient ruins of Azurath. The air is thick with the scent of damp stone and decaying leaves.
> You see a sword in the entrance, seems to have been lost by someone running away

## Random: You look around and...
- 30% chance: [Find a hidden alcove with a shield](#scene-find-shield)
- 20% chance: [Nothing happens](#scene-nothing-happens)
- 20% chance: [Find a pot of coins](#scene-find-coins)
- 30% chance: [Fight the stone Golem](#opponent-stone-golem); [Find a pot of coins](#scene-find-coins)
- Afterwards: [You return to the entrance](#scene-azurath-entrance)

# Action: Attack
> Define what happens when Player A attacks player B
```
AttackPower = A['Attack'] + (A['Experience'] / (A['Attack'] * 0.5)); 
DefendPower = B['Defense'] + (B['Experience'] / (B['Defense'] * 0.5));
if (B['Health'] > 0) {B['Health'] = B['Health'] - Math.max(0, AttackPower - DefendPower);}
if (A['Health'] < 0) { A['Coins'] = 0; } // lose all coins
if (B['Health'] < 0) { A['Coins'] = A['Coins'] + 10; } // win coins
// write an output
output = A['Health'] < 0 ? '#scene-end' : (B['Health'] < 0 ? 'You have won!' : null);
```

# Player
- Attack: 10
- Health: 50
- Defense: 5
- Experience: 30
- Coins: 0

# Opponent: Stone Golem
- Actions: Attack
- Health: 40
- Attack: 10
- Defense: 5
- Experience: 20
""";

        // parse the script
        GameScreen screen = new GameScreenCLI();
        //GamePlay game = new GamePlay(StoryNavigateRooms.text, screen);
        User user = UserUtils.createUserAnonymous();
        user.setUsername("brito");
        GamePlay game = new GamePlay(script2, screen, user);

        Player A = game.getPlayer();
        assertFalse(A.getAttributes().isEmpty());
        assertEquals(50, A.getAttributes().get("Health"));
        
        Map<String, Opponent> opponents = game.getOpponents();
        assertFalse(opponents.isEmpty());
        
        String opId = "opponent-stone-golem";
        Opponent B = opponents.get(opId);
        assertNotNull(B);
        
        int attackBefore = (int) B.getAttribute("Health");
        assertEquals(40, attackBefore);
        
        Actions actions = game.getActions();
        actions.run(A, B, "Attack", screen);

        // check that the health of the opponent is different now
        int attackAfter = (int) B.getAttribute("Health");
        assertEquals(37, attackAfter);

        // attack again
        actions.run(A, B, "Attack", screen);

        // check that the health of the opponent is different now
        attackAfter = (int) B.getAttribute("Health");
        assertEquals(34, attackAfter);
        
        // test the IF conditions
        
       // boolean actionsDone = game.runAction(nextSteps);
    }

    
    @Test
    public void testChoiceWithConsequence(){
        String line1 = "- [Fight the stone Golem](#opponent-stone-golem): #scene-victory";
        Choice choice = Choice.parse(line1);
        assertNotNull(choice);
        String nextActions = choice.getNextActions();
        assertNotNull(nextActions);
        assertEquals("Fight the stone Golem", choice.getTitle());
        assertEquals(LinkType.ACTION, choice.getLinkType());
        assertEquals("opponent-stone-golem", choice.getLink());
        assertEquals("#scene-victory", choice.getNextActions());
    }
    
    
    @Test
    public void testOpponent(){
        String data = """
                    # Opponent: Stone Golem
                    - Actions: Attack; Magic
                    - Health: 60
                    - Attack: 10
                    - Defense: 5
                    - Experience: 30
                      """;
        String anchor = "# Opponent: ";
        String anchorId = "opponent-";
        
        Opponent op = new Opponent();
        op.parse(data, anchor, anchorId);
        assertNotNull(op);
        assertEquals("Stone Golem", op.getName());
        assertEquals("opponent-stone-golem", op.getId());
        
        String[] actions = op.getActions();
        assertEquals(2, actions.length);
        assertEquals("Attack", actions[0]);
        assertEquals("Magic", actions[1]);
        
        
    }
    
    
    @Test
    public void testAsciiArt() throws IOException{
        /*
        Characters can have images, they are represented
        with ``` in the markdown format.
        */
        
        File folderBase = Folder.getFolderStories();
        File folderExamples = new File(folderBase, "examples");
        File file = new File(folderExamples, "FightExample.md");
        assertTrue(file.exists());
        String text = FileUtils.readFileToString(file, "UTF-8");
        
        // parse the script
        GameScreen screen = new GameScreenCLI();
        User user = UserUtils.createUserAnonymous();
        GamePlay game = new GamePlay(text, screen, user);
        
        assertFalse(game.getOpponents().isEmpty());
        Opponent op = game.getOpponents().get("opponent-ogre");
        String textImage = op.getTextImage();
        assertNotNull(textImage);
        
        Player player = game.getPlayer();
        
        text = StoryUtils.showIntro(player, op);
        
        // check that player has stats
        assertFalse(player.getAttributes().isEmpty());
        assertEquals(50, player.getAttributes().get("Health"));
        
        System.out.println(text);
        
//        System.out.println("");
//        System.out.println("Press ENTER to continue");
//        Scanner scanner = new Scanner(System.in);
//        scanner.nextLine(); // Waits for the user to press Enter
//        System.exit(0);
        
    }
    
    
    
}
