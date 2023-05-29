# Change Log

## 1.19.2

### 0.1.4

WARNING: Mob Respawner save data format changed in this version, making it incompatible with versions below. To solve this, load 0.1.3 (or 0.1.2 for multiplayer), respawn all mobs in the respawners, and then update to 0.1.4.

Fixed GUI not working in the multiplayer game.

Fixed 0.1.3 crashing the multiplayer server.

Fixed Respawners spawning pigs in the multiplayer game.

Fixed Slimes not damaging befriended mobs.

Fixed Creeper Girls and Ender Executors missing texture.

Set that monsters are hostile to certain types of befriended mobs:

- Zombies and derivatives (not girls, including Zombified Piglins and Zoglins) attacks Skeleton/Stray/Wither Skeleton Girls.
- Skeletons and derivatives (not girls) attacks Zombie/Husk/Drowned Girls.
- Slimes (not girls), Magma Cubes, Illagers, Phantoms (including Dyssomnias), Piglin Brutes and Hoglins attack all befriended mobs.
- Ghasts attack non-undead mobs.
- Blazes attack Skeleton/Stray Girls and flying befriended mobs.
- Spiders attack non-arthropod befriended mobs.

### 0.1.3

Versions above won't use "alpha" postfix anymore. All versions with prefix 0 are alpha.

Saves of snapshot versions are no longer supported since this version. To port legacy data to new versions, open it in 0.0.2-alpha, interact with all your mobs and save.

Added Favorability System.

Added Exp & Level System. 

Added armor durability drop on mob hurt.

Now Soul Amulet and Resistance Amulet can provide undead mobs sun immunity.

Some numerical changes:

- Reduced the atk enhancement from Efficiency enchantment for Necrotic Reapers, from 20% to 10% hoe basic atk each level. 
- Reduced the speed-down effect of Resistance Amulet from 20% to 10% each.
- Increased Undead Affinity effect duration from Soul Cake from 30 s to 1.5 min each slice; from 1.5 min to 5 min for Each Soul Cake Slice.
- Increased Ender Protection effect duration from Ender Pie, from 30 s to 3 min.

Fixed befriended mob not dropping inventory on death.

Fixed mob main-hand weapons not breaking on durability ran out.

### 0.1.2-alpha

Added Necrotic Reaper support.

Added *Necromancer's Wand*.

Added Sharpness enchantment effect and durability drop features on befriended mob attacking with weapons.

Increased *Death Crystal Powder* production from 4 to 9 each *Crystal of Death*.

Befriended mobs now don't attack their hostile/neutral relatives, and *vice versa*. 

### 0.1.1-alpha

Extracted BefriendMobs API out as a lib. Now this mod needs BefriendMobs API as dependency.

(As BefriendMobs API hasn't been approved in CurseForge, the .jar files are also available in Github: https://github.com/SodiumZH/Days-with-Monster-Girls/tree/1.18.2/files)

### 0.1.0-snapshot.11

Fixed Ender Executor befriending interruption on the mob attacking the player.

Now Ender Executor befriending will interrupt if it failed to attack the player (no matter blocked with Shield or not) within 10 seconds.

### 0.1.0-snapshot.10

Remade the Bauble System and changed the accepted items.

Added Baubles: Soul Amulet, Amulet of Resistance, Healing Jade, Aqua Jade, Poisonous Thorn.

Added Equipment: Necromancer's Hat, Sunhat.

### 0.0.0-snapshot.9

**WARNING: It is known that mobs LOSE THE OWNER INFORMATION on updating to this version. Now ownerless mob from this bug will be owned by the first player interacted with it.**

**WARNING: BACKUP BEFORE UPDATING!!! This update contains save data format change. If it contains any problem porting old save data to new format, the data may break!!!**

Added Hornet support.

Fixed Creeper Girl befriending still succeeding when player is killed by the final 12-ranged explosion.

Fixed befriendable mob not forgiving player after 15 min.

### 0.1.0-snapshot.8

**WARNING: BACKUP BEFORE UPDATING!!! This update contains save data format change. If it contains any problem porting old save data to new format, the data may break!!!**

Added Drowned Girl support.

Added Mob Respawner.

Fixed Creeper Girl wrong AI behaviors.

### 0.1.0-snapshot.7

Fixed Golems being hostile to befriended mobs. Now they're neutral.

Fixed befriended mobs starting to attack immediately after switching AI state.

### 0.1.0-snapshot.6

Added Bauble system (see wiki for details).

Fixed world timers ticking twice each frame (i.e. timers running 2x faster).

Disabled /summon command for raw befriended mobs which may cause unknown errors.

Added 15 min forgiving cooldown to undead mobs. After forgiving, they will not keep hostile when player has Undead Affinity effect. 

### 0.1.0-snapshot.5

Fixed Soul Carpet recipe missing tag.

### 0.1.0-snapshot.4

Added Stray Girl support.

Added Wither Skeleton Girl support.

Added 2s cooldown for giving healing items.

Added healing items to Creeper Girl and Ender Executor.

Rollbacked HMaG dependency to 6.1.0.

### 0.1.0-snapshot.3

Ported from 1.18.2 - 0.0.0-snapshot.3.

## 1.18.2

### 0.0.4

WARNING: Mob Respawner save data format changed in this version, making it incompatible with versions below. To solve this, load 0.0.3 (or 0.0.2 for multiplayer), respawn all mobs in the respawners, and then update to 0.0.4.

Fixed GUI not working in the multiplayer game.

Fixed 0.0.3 crashing the multiplayer server.

Fixed Respawners spawning pigs in the multiplayer game.

Fixed Slimes not damaging befriended mobs.

Set that monsters are hostile to certain types of befriended mobs:

- Zombies and derivatives (not girls, including Zombified Piglins and Zoglins) attacks Skeleton/Stray/Wither Skeleton Girls.
- Skeletons and derivatives (not girls) attacks Zombie/Husk/Drowned Girls.
- Slimes (not girls), Magma Cubes, Illagers, Phantoms (including Dyssomnias), Piglin Brutes and Hoglins attack all befriended mobs.
- Ghasts attack non-undead mobs.
- Blazes attack Skeleton/Stray Girls and flying befriended mobs.
- Spiders attack non-arthropod befriended mobs.

### 0.0.3

Versions above won't use "alpha" postfix anymore. All versions with prefix 0 are alpha.

Saves of snapshot versions are no longer supported since this version. To port legacy data to new versions, open it in 0.0.2-alpha, interact with all your mobs and save.

Added Favorability System.

Added Exp & Level System. 

Added armor durability drop on mob hurt.

Now Soul Amulet and Resistance Amulet can provide undead mobs sun immunity.

Optimized the performance of Necromancer's Wand.

Some numerical changes:

- Reduced the atk enhancement from Efficiency enchantment for Necrotic Reapers, from 20% to 10% hoe basic atk each level. 
- Reduced the speed-down effect of Resistance Amulet from 20% to 10% each.
- Increased Undead Affinity effect duration from Soul Cake from 30 s to 1.5 min each slice; from 1.5 min to 5 min for Each Soul Cake Slice.
- Increased Ender Protection effect duration from Ender Pie, from 30 s to 3 min.

Fixed befriended mob not dropping inventory on death.

Fixed mob main-hand weapons not breaking on durability ran out.

### 0.0.2-alpha

Added Necrotic Reaper support.

Added *Necromancer's Wand*.

Added Sharpness enchantment effect and durability drop features on befriended mob attacking with weapons.

Increased *Death Crystal Powder* production from 4 to 9 each *Crystal of Death*.

Befriended mobs now don't attack their hostile/neutral relatives, and *vice versa*. 

### 0.0.1-alpha

Extracted BefriendMobs API out as a lib. Now this mod needs BefriendMobs API as dependency.

(As BefriendMobs API hasn't been approved in CurseForge, the .jar files are also available in Github: https://github.com/SodiumZH/Days-with-Monster-Girls/tree/1.18.2/files)

### 0.0.0-snapshot.10

Fixed Ender Executor befriending interruption on the mob attacking the player.

Now Ender Executor befriending will interrupt if it failed to attack the player (no matter blocked with Shield or not) within 10 seconds.

### 0.0.0-snapshot.9

Remade the Bauble System and changed the accepted items.

Added Baubles: Soul Amulet, Amulet of Resistance, Healing Jade, Aqua Jade, Poisonous Thorn.

Added Equipment: Necromancer's Hat, Sunhat.

### 0.0.0-snapshot.8

**WARNING: It is known that mobs LOSE THE OWNER INFORMATION on updating to this version. Now ownerless mob from this bug will be owned by the first player interacted with it.**

**WARNING: BACKUP BEFORE UPDATING!!! This update contains save data format change. If it contains any problem porting old save data to new format, the data may break!!!**

Added Hornet support.

Fixed Creeper Girl befriending still succeeding when player is killed by the final 12-ranged explosion.

Fixed befriendable mob not forgiving player after 15 min.

### 0.0.0-snapshot.7

**WARNING: BACKUP BEFORE UPDATING!!! This update contains save data format change. If it contains any problem porting old save data to new format, the data may break!!!**

Added Drowned Girl support.

Added Mob Respawner.

Fixed Creeper Girl wrong AI behaviors.

Fixed befriended mobs starting to attack immediately after switching AI state.

### 0.0.0-snapshot.6

Fixed Golems being hostile to befriended mobs. Now they're neutral.

### 0.0.0-snapshot.5

Added Bauble system (see wiki for details).

Fixed world timers ticking twice each frame (i.e. timers running 2x faster).

Disabled /summon command for raw befriended mobs which may cause unknown errors.

Added 15 min forgiving cooldown to undead mobs. After forgiving, they will not keep hostile when player has Undead Affinity effect. 

### 0.0.0-snapshot.4

Added Stray Girl support.

Added Wither Skeleton Girl support.

Added 2s cooldown for giving healing items.

Added healing items to Creeper Girl and Ender Executor.

### 0.0.0-snapshot.3

Changed the mechanism for befriending of all supported mobs. Now the mobs don't require a certain amount of items, but have a progress value with maximum 1. Each support item adds a random value to the mob, and when the value reaches 1 it's done. 

Added Ender Executor support.

Added effects: Undead Affinity, Ender Protection.

Added foods: Soul Cake, Soul Cake Slice, Enderberry, Ender Pie.

Added blocks: Soul Carpet.

Updated HMaG dependency to 5.1.9.

Removed GoG4 dependency.

Fixed wrong mob attributes.

### 0.0.0-snapshot.2

Added Creeper Girl support.

### 0.0.0-snapshot.1

Added befriending Zombie Girl, Husk Girl and Skeleton Girl support.

## 1.19.2

### 0.1.3

Versions above won't use "alpha" postfix anymore. All versions with prefix 0 are alpha.

Saves of snapshot versions are no longer supported since this version. To port legacy data to new versions, open it in 0.0.2-alpha, interact with all your mobs and save.

Added Favorability System.

Added Exp & Level System. 

Added armor durability drop on mob hurt.

Now Soul Amulet and Resistance Amulet can provide undead mobs sun immunity.

Optimized the performance of Necromancer's Wand.

Some numerical changes:

- Reduced the atk enhancement from Efficiency enchantment for Necrotic Reapers, from 20% to 10% hoe basic atk each level. 
- Reduced the speed-down effect of Resistance Amulet from 20% to 10% each.
- Increased Undead Affinity effect duration from Soul Cake from 30 s to 1.5 min each slice; from 1.5 min to 5 min for Each Soul Cake Slice.
- Increased Ender Protection effect duration from Ender Pie, from 30 s to 3 min.

Fixed befriended mob not dropping inventory on death.

Fixed mob main-hand weapons not breaking on durability ran out.

### 0.1.2-alpha

Added Necrotic Reaper support.

Added *Necromancer's Wand*.

Added Sharpness enchantment effect and durability drop features on befriended mob attacking with weapons.

Increased *Death Crystal Powder* production from 4 to 9 each *Crystal of Death*.

Befriended mobs now don't attack their hostile/neutral relatives, and *vice versa*. 

### 0.1.1-alpha

Extracted BefriendMobs API out as a lib. Now this mod needs BefriendMobs API as dependency.

(As BefriendMobs API hasn't been approved in CurseForge, the .jar files are also available in Github: https://github.com/SodiumZH/Days-with-Monster-Girls/tree/1.18.2/files)

### 0.1.0-snapshot.11

Fixed Ender Executor befriending interruption on the mob attacking the player.

Now Ender Executor befriending will interrupt if it failed to attack the player (no matter blocked with Shield or not) within 10 seconds.

### 0.1.0-snapshot.10

Remade the Bauble System and changed the accepted items.

Added Baubles: Soul Amulet, Amulet of Resistance, Healing Jade, Aqua Jade, Poisonous Thorn.

Added Equipment: Necromancer's Hat, Sunhat.

### 0.0.0-snapshot.9

**WARNING: It is known that mobs LOSE THE OWNER INFORMATION on updating to this version. Now ownerless mob from this bug will be owned by the first player interacted with it.**

**WARNING: BACKUP BEFORE UPDATING!!! This update contains save data format change. If it contains any problem porting old save data to new format, the data may break!!!**

Added Hornet support.

Fixed Creeper Girl befriending still succeeding when player is killed by the final 12-ranged explosion.

Fixed befriendable mob not forgiving player after 15 min.

### 0.1.0-snapshot.8

**WARNING: BACKUP BEFORE UPDATING!!! This update contains save data format change. If it contains any problem porting old save data to new format, the data may break!!!**

Added Drowned Girl support.

Added Mob Respawner.

Fixed Creeper Girl wrong AI behaviors.

### 0.1.0-snapshot.7

Fixed Golems being hostile to befriended mobs. Now they're neutral.

Fixed befriended mobs starting to attack immediately after switching AI state.

### 0.1.0-snapshot.6

Added Bauble system (see wiki for details).

Fixed world timers ticking twice each frame (i.e. timers running 2x faster).

Disabled /summon command for raw befriended mobs which may cause unknown errors.

Added 15 min forgiving cooldown to undead mobs. After forgiving, they will not keep hostile when player has Undead Affinity effect. 

### 0.1.0-snapshot.5

Fixed Soul Carpet recipe missing tag.

### 0.1.0-snapshot.4

Added Stray Girl support.

Added Wither Skeleton Girl support.

Added 2s cooldown for giving healing items.

Added healing items to Creeper Girl and Ender Executor.

Rollbacked HMaG dependency to 6.1.0.

### 0.1.0-snapshot.3

Ported from 1.18.2 - 0.0.0-snapshot.3.

