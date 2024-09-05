# Scene: Azurath Entrance
> Welcome to the ancient ruins of Azurath. The air is thick with the scent of damp stone and decaying leaves. You are a treasure hunter, drawn by the legends of untold riches hidden within.
> The entrance to the ruins is overgrown with vines. Faded carvings on the stone walls hint at the once-great civilization that lived here.

## Intro
You step into the ruins, the sound of your footsteps echoing off the stone walls. The air is cooler inside, and a sense of foreboding settles over you.

## Choices:
- [Explore the main hall](#scene-main-hall)
- [Investigate the side chamber](#scene-side-chamber)
- [Leave the ruins](#scene-leave-ruins)

-----

# Scene: Main Hall
> The main hall is vast, with towering pillars and a high, vaulted ceiling. Broken statues and shattered pottery litter the floor.

## Intro
As you move deeper into the hall, 
you notice something glinting in the dim light. It appears to be a small, ornate chest partially buried under rubble.

## Random:
- 30% chance: [Fight a Skeleton Warrior](#scene-fight-skeleton)
- 30% chance: [Find a hidden alcove with a shield](#scene-find-shield)
- 20% chance: [Nothing happens](#scene-nothing-happens)
- 20% chance: [Find a pot of coins](#scene-find-coins)

-----

# Scene: Fight Skeleton
> A skeleton warrior emerges from the shadows, its hollow eyes glowing with malevolent energy. It raises its rusty sword and charges at you.

## Enemy: Skeleton Warrior
- Health: 60
- Attack: 10
- Defense: 5
- Experience: 30

## If win:
- [Take](#item-rusty-sword)
- [Take](#item-coins-5-15)
- [Continue exploring the hall](#scene-continue-hall)

## If lose:
- [Lose](#item-coins-5-10)
- [Return to the entrance](#scene-azurath-entrance)

## If run:
- [Lose](#item-coins-1-5)
- [Return to the entrance](#scene-azurath-entrance)

# Item: Rusty Sword
Type: Weapon  
Description: A worn and damaged sword, but it still has some fight left in it.  
Attack Bonus: 3  
Durability: 10

# Item: Coins (5-15)
Type: Currency  
Description: A small amount of coins collected from the defeated Skeleton Warrior.  
Value: 5..15 coins

# Item: Coins (5-10)
Type: Currency  
Description: A small amount of coins lost during the fight.  
Value: -5..10 coins

# Item: Coins (1-5)
Type: Currency  
Description: A small amount of coins lost while escaping the fight.  
Value: -1..5 coins

-----

# Scene: Find Shield
> As you clear away the rubble, you discover a hidden alcove containing an ancient shield. It's battered, but it might still offer some protection.

## Item: Ancient Shield
Type: Shield  
Description: A shield from a bygone era, worn but sturdy.  
Defense Bonus: 5  
Durability: 20

## Choices:
- [Take](#scene-continue-hall)
- [Leave](#scene-continue-hall)

-----

# Scene: Nothing Happens
> You continue exploring the hall, but nothing unusual happens. The eerie silence only adds to your unease.

## Choices:
- [Continue exploring the hall](#scene-continue-hall)

-----

# Scene: Find Coins
> While exploring the hall, you stumble upon a small pot. Inside, you find a stash of coins, glinting in the dim light.

## Gain:
- [Take](#item-coins-10-30)
  
## Choices:
- [Continue exploring the hall](#scene-continue-hall)

# Item: Coins (10-30)
Type: Currency  
Description: A pot of coins found in the ruins.  
Value: 10-30 coins

-----

# Scene: Side Chamber
> The side chamber is smaller, with walls covered in intricate carvings. A large, menacing statue stands at the far end of the room.

## Enemy: Stone Guardian
- Health: 100
- Attack: 15
- Defense: 10
- Experience: 60

## Intro
As you approach the statue, its eyes suddenly glow red, and it begins to move. The Stone Guardian is awake and ready to defend the chamber.

## Choices:
- [Fight the Stone Guardian](#scene-fight-guardian)
- [Flee the chamber](#scene-leave-chamber)

-----

# Scene: Fight Guardian
> You draw your weapon and prepare to face the Stone Guardian. Its massive stone fists crash down towards you as the battle begins.

## If win:
- [Take](#item-stone-shard)
- [Continue exploring the hall](#scene-continue-hall)

## If lose:
- [Lose](#item-coins-10-15)
- [Return to the entrance](#scene-azurath-entrance)

## If run:
- [Lose](#item-coins-5-10)
- [Return to the entrance](#scene-azurath-entrance)

# Item: Stone Shard
Type: Material  
Description: A shard of the Stone Guardian. It's surprisingly light and has a faint magical aura.  
Attack Bonus: 5  
Durability: 20

# Item: Coins (10-15)
Type: Currency  
Description: A small amount of coins lost during the fight.  
Value: 10-15 coins

# Item: Coins (5-10)
Type: Currency  
Description: A small amount of coins lost while escaping the fight.  
Value: 5-10 coins

-----

# Scene: Chamber Treasure
> With the Stone Guardian defeated, you notice a hidden compartment in the floor. Inside, you find an ancient artifact.

## Intro
The artifact is covered in strange runes that seem to shift as you look at them. You feel a surge of power as you pick it up.

## Choices:
- [Take](#scene-take-artifact)
- [Leave the artifact](#scene-leave-artifact)

-----

# Scene: Take Artifact
> You take the artifact, feeling its power coursing through you. This relic might be the key to uncovering the secrets of Azurath.

## Choices:
- [Take](#item-gold-sword)
- [Continue exploring](#scene-continue-hall)

# Item: Gold Sword
Type: Weapon  
Description: A shard of the Stone Guardian. It's surprisingly light and has a faint magical aura.  
Attack Bonus: 5  
Durability: 20

-----

# Scene: Leave Artifact
> You decide the artifact is too dangerous to take and leave it where it lies.

## Choices:
- [Continue exploring](#scene-continue-hall)

-----

# Scene: Continue Hall
> As you move further into the hall, the air grows colder. You see a large doorway at the far end, leading deeper into the ruins.

## Random:
- 50% chance: [Fight a Spectral Wraith](#scene-fight-wraith)
- 50% chance: [Find a hidden treasure room](#scene-hidden-treasure-room)

-----

# Scene: Fight Wraith
> A chilling presence fills the air as a Spectral Wraith materializes before you. Its ghostly form hovers, and it reaches out with icy hands to drain your life force.

## Enemy: Spectral Wraith
- Health: 80
- Attack: 12
- Defense: 8
- Experience: 50

## If win:
- [Take](#item-wraith-cloak)
- [Take](#item-coins-10-20)
- [Continue through the hall](#scene-continue-hall)

## If lose:
- [Lose](#item-coins-10-15)
- [Return to the entrance](#scene-azurath-entrance)

## If run:
- [Lose](#item-coins-5-10)
- [Return to the entrance](#scene-azurath-entrance)

# Item: Wraith Cloak
Type: Armor  
Description: A cloak woven from the essence of the Spectral Wraith. It offers some protection against dark magic.  
Defense Bonus: 7  
Durability: 15

# Item: Coins (10-20)
Type: Currency  
Description: A small amount of coins collected from the defeated Spectral Wraith.  
Value: 10-20 coins

# Item: Coins (10-15)
Type: Currency  
Description: A small amount of coins lost during the fight.  
Value: 10-15 coins

# Item: Coins (5-10)
Type: Currency  
Description: A small amount of coins lost while escaping the fight.  
Value: 5-10 coins

-----

# Scene: Hidden Treasure Room
> The door creaks open, revealing a room filled with ancient treasures. Gold coins, jewels, and artifacts are piled high, glittering in the dim light.

## Intro
You have found the fabled treasure of Azurath. However, a sense of unease fills the air—this treasure is surely guarded.

## Choices:
- [Take some treasure](#scene-take-treasure)
- [Leave the treasure](#scene-leave-treasure)

-----

# Scene: Take Treasure
> As you gather the treasure, you hear a low growl from behind you. You turn to see a massive stone golem, awakened by your greed.

## Enemy: Stone Golem
- Health: 150
- Attack: 20
- Defense: 15
- Experience: 100

## If win:
- [Take](#item-golem-heart)
- [Take](#item-coins-20-30)
- [Exit the ruins](#scene-exit-ruins)

## If lose:
- [Lose](#item-coins-20-25)
- [Return to the entrance](#scene-azurath-entrance)

## If run:
- [Lose](#item-coins-10-15)
- [Return to the entrance](#scene-azurath-entrance)

# Item: Golem Heart
Type: Material  
Description: A powerful artifact, the heart of the stone golem pulses with immense energy.  
Attack Bonus: 10  
Durability: 25

# Item: Coins (20-30)
Type: Currency  
Description: A significant amount of coins collected from the defeated Stone Golem.  
Value: 20-30 coins

# Item: Coins (20-25)
Type: Currency  
Description: A significant amount of coins lost during the fight.  
Value: 20-25 coins

# Item: Coins (10-15)
Type: Currency  
Description: A significant amount of coins lost while escaping the fight.  
Value: 10-15 coins

-----

# Scene: Leave Treasure
> You decide that the treasure isn't worth the risk and quietly leave the room, hoping to avoid any traps or guardians.

## Choices:
- [Return to the corridor](#scene-continue-hall)
- [Exit the ruins](#scene-exit-ruins)

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

# Scene: End
> Your adventure has come to an end. Perhaps one day you will return to the ruins of Azurath, but for now, you leave with the stories of what you encountered.

## Intro
Thank you for playing! The ruins of Azurath will await your return.
