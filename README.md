# BattleArena
 
 ![Image of Battle Arena](https://cdn.discordapp.com/attachments/503002454418259968/633374772906033191/Screen_Shot_2019-10-14_at_2.33.14_PM.png)
 
## Introduction

Battle Arena is a deathmatch game where two players fight to the death. Users create their own AI agent that can participate in the competition of ERAU Artificial Intelligence Club 2019.
 
## Rules
The following are the subsections of rules for the Battle Arena game.

### Map
The map is a 20x20 grid that is vertically symmetrical. The map is randomly generated for each game, populated with walls and health packs.

### Round
The game is round-based, meaning that each round both players perform their actions simultaneously.

### Storm
Every 10 rounds, the storm will advance by 1 block. The storm is the number of diagonal blocks from the corners of the map.
If a player comes into contact with a storm, they will die instantly and horribly. We wall want our wizards to go to heaven, don't we? But <i>noooo</i>, they die a fiery death and have no after-life. :(

### Health
Health represents the amount of damage that a player can take before dying. When a player comes into contact with a projectile or a mine, they lose 1 health point. When their health reaches 0, they die an honorable death. The player starts with 3 health points, and can reach a maximum of 5 health points via health packs.

#### Health Packs
Health packs are scattered around the map. When a player comes into contact with it, they will gain 1 health point.

### Actions
An action is what a AI agent can perform in a single round. Only 1 action can be performed per round. If no actions are executed, a <code>Action.NoAction</code> is performed.

#### Movement
A player can move in 4 directions: up, down, left, right. They can only move to a cell that doesn't contain a wall.

#### Shooting
A player can shoot in 4 directions: up, down, left, right. Projectiles can collide with other projectiles and mines, destroying both in the process. Projectiles will move 1 unit in the specified direction per round, matching the player's movement speed. Also, if a projectile comes into contact with a health pack, the shooter will gain +1 health point.

#### Placing Mines
A player can place a mine in 4 directions: up, down, left, right. A mine does not move and will explode when it comes into contact with a player, another mine, and a projectile. Placing a mine has a cooldown of 10 rounds; meaning, that it can only be placed once every 10 rounds.

## API
For the complete API available to user, read the [API Link](API.md).
