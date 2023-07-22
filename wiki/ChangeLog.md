# Change Log

## 1.19.2

### 0.1.10

Added Mob Storage Pod.

Fixed some AI adjustment not working.

Fixed Ender Executor generating angry particles on being attacked even if not in befriending process.

Fixed Kobold and Imp not generating heart particles on befriending.

### 0.1.9

WARNING: Due to item change, Mob Respawners in earlier versions may be incompatible to this version and above. They will possibly **VANISH** on updating to this version! Please make sure to have released all mobs from Respawners before updating!!

Added Kobold and Imp support.

Added weapon: Netherite Fork.

Fixed Soul Amulet II not working.

Fixed server crash on player taking damage when having Ender Protection effect.

Fixed Ender Executors stop attacking unexpectedly.

### 0.1.8.1

Reduced the cost of some Baubles.

Fixed Soul Amulet II not working.

Fixed Amulet of Resistance II not providing sun immunity.

### 0.1.8

Added Baubles: Amulet of Courage, Life Jade

Added upgrade to some Baubles: Soul Amulet, Amulet of Resistance, Amulet of Courage, Life Jade. 

Added Bauble effect description.

Added Evil Magnet, a tool for finding respawners in the level.

Reduced the cost of some recipes.

Adjusted Necromancer's Wand: 

 -- Changed bullet speed from 4 to 16.

 -- Reduced usage cooldown from 5 s to 2.5 s.

 -- Now the bullet will blast on hit something, and give previous effects to all living entities in the  3x3x3 area around. If a living entity is directly hit, a stronger effect will be given to it, either negative or positive.

 -- Increased Wither effect duration from 5 s to 10 s.

 -- Increased  Strength effect duration from 5 s to 10 s.

 -- If directly hit: the level of Wither will increase by 1; the duration of Strength will be 15 s; healing amount will be doubled. 

Reduced the forgiving time of undead mobs for Undead Affinity effect from 15 min to 5 min.

Changed the hatred duration of most befriendable mob. Now the hatred durations are listed in `wiki/hatred-durations.txt` in the repo.

Fixed Zombies and variants GUI Bauble slot display error.

Fixed crash caused by null damage source on mob death, described in issue #7

Fixed some items in other mods may make Ender Executor and Necrotic Reaper non-hostile even during befriending process.

Fixed Mob Respawner item entities vanishing in fire and lava.

### 0.1.7

Fixed Creeper Girl not being interactable.

Fixed Creeper Girl and Ender Executor still be waiting instead of following right after befriending.

### 0.1.6

Added Ghastly Seeker support.

Added Commanding Wand.

Adjusted mob interaction method:

 --To switch mob's AI state, player must have Commanding Wand on either hand, and right click the mob.

 --To open mob's inventory GUI, player must have Commanding Wand on either hand, and press shift+right click.

 --For Necrotic Reapers, use Necromancer's Wand instead.

Optimized the AI of flying befriended mobs.

Adjusted the AI of Ghastly Seeker (non-befriended, HMaG) out of the Nether. Now if it's not in Nether, it will fly at the height no more than 32 blocks above the ground. Also it won't move too far from players. This change makes effect only if DWMG is loaded. 

Now Wither Skeleton Girls will not catch Curse of Necromancy effect.

Now mob will drop player-kill loot and affected by Looting enchantment if killed by befriended mobs.  

Now befriended mob's AI state will be Following instead of Waiting right after befriended.

Fixed monster not being hostile to befriended mobs.

Fixed Wither Skeleton Girls not accepting Nether Stars.

Fixed Skeletons not consuming arrow on shooting.

Fixed crash and helmet becoming Dummy Item on skeletons killing mobs.

Fixed Stray Girl server crash.

Fixed Sunhat not preventing Skeleton/Stray Girl from avoiding the sun.

Fixed Soul Carpet not dropping on broken.

Fixed armor losing durability on taking bypass-armor damages on befriended mobs.

Fixed GUI persisting when mob is removed from level or moving too far away.

Reduced random stroll radius from 64 to 16.

Removed and fixed some deprecated methods in code.

### 0.1.5.1

Removed verbose debug output.

### 0.1.5

Added Banshee support.

Adjusted Hornet befriending condition. Now it won't be immediately interrupted if there're no enough Honey Blocks, but the progress will drop by 0.1 per second while the mob releases massive smoke particles.

Added Curse of Necromancy effect in place of Wither effect for Necromancer's Hat. The damaging effect is same to Wither I, but not causing the HP slot turning black.

Slightly adjusted the recipes to reduce the Evil Crystal cost.

Fixed server crashing on befriended mobs' conversion.

Fixed some compatibility issues:

--Solved mob interaction conflict with Quantum Catcher (*Forbidden and Arcanus*), fixed GitHub issue #6.

--Fixed undead mob still adding hatred when player got undead-neutral by other ways e.g.  Unsettling Kimono (*Alex's Mobs*).

### 0.1.4

WARNING: Mob Respawner save dat format changed in this version, making it incompatible with versions below. To solve this, load 0.1.3 (or 0.1.2 for multiplayer), respawn all mobs in the respawners, and then update to 0.1.4.

Fixed GUI not working in the multiplayer game.

Fixed 0.1.3 crashing the multiplayer server.

Fixed Respawners spawning pigs in the multiplayer game.

Fixed Slimes not damaging befriended mobs.

Fixed Creeper Girls and Ender Executors missing texture.

Set that monsters are hostile to certain types of befriended mobs:

--Zombies and derivatives (not girls, including Zombified Piglins and Zoglins) attacks Skeleton/Stray/Wither Skeleton Girls.

Skeletons and derivatives (not girls) attacks Zombie/Husk/Drowned Girls.

--Slimes (not girls), Magma Cubes, Illagers, Phantoms (including Dyssomnias), Piglin Brutes and Hoglins attack all befriended mobs.

--Ghasts attack non-undead mobs.

--Blazes attack Skeleton/Stray Girls and flying befriended mobs.

--Spiders attack non-arthropod befriended mobs.

### 0.1.3

Versions above won't use "alpha" postfix anymore. All versions with prefix 0 are alpha.

Saves of snapshot versions are no longer supported since this version. To port legacy data to new versions, open it in 0.1.2-alpha, interact with all your mobs and save.

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

### 0.0.8

Added Baubles: Amulet of Courage, Life Jade

Added upgrade to some Baubles: Soul Amulet, Amulet of Resistance, Amulet of Courage, Life Jade. 

Added Bauble effect description.

Added Evil Magnet, a tool for finding respawners in the level.

Reduced the cost of some recipes.

Adjusted Necromancer's Wand: 

 -- Changed bullet speed from 4 to 16.

 -- Reduced usage cooldown from 5 s to 2.5 s.

 -- Now the bullet will blast on hit something, and give previous effects to all living entities in the  3x3x3 area around. If a living entity is directly hit, a stronger effect will be given to it, either negative or positive.

 -- Increased Wither effect duration from 5 s to 10 s.

 -- Increased  Strength effect duration from 5 s to 10 s.

 -- If directly hit: the level of Wither will increase by 1; the duration of Strength will be 15 s; healing amount will be doubled. 

Reduced the forgiving time of undead mobs for Undead Affinity effect from 15 min to 5 min.

Changed the hatred duration of most befriendable mob. Now the hatred durations are listed in `wiki/hatred-durations.txt` in the repo.

Fixed Zombies and variants GUI Bauble slot display error.

Fixed crash caused by null damage source on mob death, described in issue #7

Fixed some items in other mods may make Ender Executor and Necrotic Reaper non-hostile even during befriending process.

Fixed Mob Respawner item entities vanishing in fire and lava.

Fixed Sunhat not working for Skeleton and Stray Girls.

Fixed crash related to sun immunity of befriended Skeleton and related mobs.

### 0.0.7

Fixed Creeper Girl not being interactable.

Fixed Creeper Girl and Ender Executor still be waiting instead of following right after befriending.

### 0.0.6

Added Ghastly Seeker support.

Added Commanding Wand.

Adjusted mob interaction method:

 --To switch mob's AI state, player must have Commanding Wand on either hand, and right click the mob.

 --To open mob's inventory GUI, player must have Commanding Wand on either hand, and press shift+right click.

 --For Necrotic Reapers, use Necromancer's Wand instead.

Optimized the AI of flying befriended mobs.

Adjusted the AI of Ghastly Seeker (non-befriended, HMaG) out of the Nether. Now if it's not in Nether, it will fly at the height no more than 32 blocks above the ground. Also it won't move too far from players. This change makes effect only if DWMG is loaded. 

Now Wither Skeleton Girls will not catch Curse of Necromancy effect.

Now mob will drop player-kill loot and affected by Looting enchantment if killed by befriended mobs.  

Now befriended mob's AI state will be Following instead of Waiting right after befriended.

Fixed monster not being hostile to befriended mobs.

Fixed Wither Skeleton Girls not accepting Nether Stars.

Fixed Skeletons not consuming arrow on shooting.

Fixed crash and helmet becoming Dummy Item on skeletons killing mobs.

Fixed Stray Girl server crash.

Fixed Soul Carpet not dropping on broken.

Fixed armor losing durability on taking bypass-armor damages on befriended mobs.

Fixed GUI persisting when mob is removed from level or moving too far away.

Reduced random stroll radius from 64 to 16.

Removed and fixed some deprecated methods in code.

### 0.0.5.1

Removed verbose debug output.

### 0.0.5

Added Banshee support.

Adjusted Hornet befriending condition. Now it won't be immediately interrupted if there're no enough Honey Blocks, but the progress will drop by 0.1 per second while the mob releases massive smoke particles.

Added Curse of Necromancy effect in place of Wither effect for Necromancer's Hat. The damaging effect is same to Wither I, but not causing the HP slot turning black.

Slightly adjusted the recipes to reduce the Evil Crystal cost.

Fixed server crashing on befriended mobs' conversion.

Fixed some compatibility issues:

--Solved mob interaction conflict with Quantum Catcher (*Forbidden and Arcanus*), fixed GitHub issue #6.

--Fixed undead mob still adding hatred when player got undead-neutral by other ways.

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