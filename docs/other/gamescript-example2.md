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

- AttackPower = A:Attack + (A:Experience / (A:Attack * 0.5))
- DefendPower = B:Defense + (B:Experience / (B:Defense * 0.5))
- B:Health = B:Health - chooseGreater(0, AttackPower - DefendPower)
- If A:Health < 0 then write "You have lost"; #scene-exit-game; stop
- If B:Health < 0 then write "You have won!"; #item-coins-10-30; stop


# Player
- Health: 60
- Attack: 50
- Defense: 5
- Experience: 30

# Opponent: Stone Golem
- Actions: Attack
- Health: 60
- Attack: 10
- Defense: 5
- Experience: 30
