# Intro
This is a simple demonstration of a fight story
between the player and opponent.

The fight is defined on the [Action: Attack](#action-attack)
paragraph. 

If you note, you can define any other
action as needed. The interaction between a player
and the opponent can be written there. In fact, they
don't even need to fight at all and you can define
other type of interaction if you wish.

The "chooseGreater" function will choose the value
between two parameters that is the highest. On this
specific case is used to assure the health of the
attacked side does not increase when attacked by the
opponent.


# Scene: Azurath Entrance
> Welcome to the ancient ruins of Azurath. The air is thick with the scent of damp stone and decaying leaves.

## Choice: You look around and...
- [Fight the ogre](#opponent-ogre): #scene-victory
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
```
AttackPower = A['Attack'] + (A['Experience'] / (A['Attack'] * 0.5)); 
DefendPower = B['Defense'] + (B['Experience'] / (B['Defense'] * 0.5));
if (B['Health'] > 0) {B['Health'] = B['Health'] - Math.max(0, AttackPower - DefendPower);}
if (A['Health'] < 0) { A['Coins'] = 0; } // lose all coins
if (B['Health'] < 0) { A['Coins'] = A['Coins'] + 10; } // win coins
// write an output
output = A['Health'] < 0 ? '#scene-end' : (B['Health'] < 0 ? 'You have won!' : null);
```

------

# Player
```
         ___
        |===|       
        |___|       
  ___  /#####\       
 | | |//#####\\      
 |(o)|/ ##### \\     
 | | |  ^^^^^  &[=======
  \_/   |#|#|         
        |_|_|         
        [ | ] 
```
- Actions: Attack
- Attack: 10
- Health: 50
- Defense: 5
- Experience: 30
- Coins: 0
         
         
# Opponent: Ogre
```
                     __/='````'=\_          
      ,-,            \ (o)/ (o) \\          
     /-_ `'-,         )  (_,    |\\          
  / ````==_ /     ,-- \ '==='`  /~~~-,        
  \/       /     /     '----`         \       
  /       /-.,, /  ,                   `-   
 /'--..,_/`-,_ /`-,/                 ,   \   
/ `````-/     (    ,,              ,/,    `,  
`'--.,_/      /   <,_`'-,-`  `'---`/`      )  
             /    |  `-,_`'-,_ .--`      .`   
            /.    )------`'-,_`>   ___.-`]    
              `--|`````````-- /   /-,_ ``)    
                 |           , `-,/`-,_`-     
                 \          /\  ,     ',_`>   
                  \/`\_/V\_/  \/\,-/\`/      
                   ( .__. )    ( ._.  )       
                   |      |    |      |       
                    \.---_|    |_---,/        
                    ooOO(_)    (_)OOoo
```
- Actions: Attack
- Health: 40
- Attack: 10
- Defense: 5
- Experience: 20