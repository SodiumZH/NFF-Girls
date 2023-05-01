# Change Log

## 1.18.2

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

### 0.0.0-snapshot.11

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