# *Days with Monster Girls* gameplay instruction

## Wiki Version info

### Current version: 

#### 0.0.0-snapshot.10 (1.18.2)

#### 0.1.0-snapshot.11 (1.19.2)



### *WARNING: everything is experimental now!!! Keep your backup to prevent data-breaking on unexpected errors.*

##### Note: the wiki update may lag behind the game. Check the version wiki is supporting.



## Supported mobs

### HMaG

Zombie Girl

Skeleton Girl

Husk Girl

Creeper Girl

Ender Executor

Stray Girl

Wither Skeleton Girl

Drowned Girl

Hornet



## General befriending methods

### Item-giving process

Right click the mob with certain types of item on the main hand to give. Sometimes the giving action also requires other conditions e.g. player's effects or equipments. When given, the mob's "befriending progress" (or "progress" below) will increase depending on the item type. The initial value is 0, and when it reaches 1, the process is done. (This doesn't always mean to successfully befriend. Sometimes other processes start after this.)

Usually there are both cheap and expensive items to give. Cheap items will push the progress very slightly (usually <10%, sometimes even <1%). (Please note that it has a cooldown time each item given.) Expensive items usually push the progress >10% and, if you're lucky enough, even >50% or immediate success.

If giving succeeded, the mob will generate glint (green star) particles. If failed because of cooldown, the mob will generate smoke particles. Each time the progress increases by 20%, the mob will generate 1 heart particle.

If other conditions are fulfilled but failed because of hostility (has ever been hostile recently), the mob will generate angry particles on given. (Some mobs ignore hostility.)



#### Interruption

Something may interrupt the process. Once interrupted, the progress will drop back to 0 and everything have to restart. At this time the mob will generate angry particles.



## Bauble System

Bauble system adds several additional "Bauble" slots. These Bauble slots allows to put only specific items and make some effects depending on the item inside.

In DWMG, mobs' Bauble slots are labelled with gem icon. For effects, see "Bauble Items" below.



## Mob details

#### Zombie Girl (HMaG)

##### Befriending

Using simple item-giving process. (Item giving cooldown 10s)

Accepted items and corresponding progress increase:

*Soul Cake Slice*:  5% for 1.0,  15% for 2/3, 80% for 1/3

*Soul Apple*: 0.04 ~ 0.08

*Soul Powder*: 0.02 ~ 0.04

During the process, you must keep an "Undead Affinity" effect, which makes undead mobs be neutral to you. Also, you must haven't provoked them, either because of attacking them or getting close to them without Undead Affinity effect. Once you provoked them, it will spend 15 minutes for them to forgive you.

Each time an item is successfully given, glint particles will appear. Each time the favorability increases by 0.2, a heart particle will appear on the mob.

Once you provoked them and/or lost the Undead Affinity effect, the process will be interrupted.

##### Interaction

Right click to switch the AI state among Wait, Follow and Wander. 

Shift + Right click to open inventory. You can customize their armor and hand items. 

(The 2 interaction ways are available for all mobs unless other noted.)

They can be healed by using *Soul Powder* (+5) or *Soul Apple* (+15). (Same for all vanilla undead mob girls.)

They have 2 bauble slots. (Save for Husks and Drowneds.)

##### Features

Currently converting to Drowned is not added because Drowned Girl is not finished. If the Zombie Girl is converted from a Husk Girl, it can be converted back by using a sponge to it.



#### Skeleton Girl (HMaG)

##### Befriending

Simple item-giving process. (Item giving cooldown 10s)

*Soul Cake Slice*: 1% for 1.00, 4% for 3/4, 15% for 1/2, 80% for 1/4.

*Soul Powder*: 0.015 ~ 0.03.

*Soul Apple*: 0.03 ~ 0.06.

##### Interaction

Same to Zombie Girl. 

You can customize their armors, hand items (bow and shield icons) and secondary weapon (sword icon, described below). There is an arrow slot (arrow icon), and you must put arrows inside to enable shooting, and they will consume arrows to shoot.

Their have 1 bauble slot. (Same for Strays and Wither Skeletons.)

##### Features

The secondary weapon slot (sword icon) enables the skeleton to automatically swap the weapons on close combat and shooting. When the enemy is too close or the arrows run out, it will try swapping to the sword/tool in the secondary weapon slot for close combat. If the enemy is far away and it has arrows, it will also try swapping to the bow.

It can convert to Stray Girl in Powder Snow.



#### Husk Girl (HMaG)

##### Befriending

Identical to Skeleton Girl.

##### Interaction

Identical to Zombie Girl. 

##### Features

It can convert to Zombie Girl in water. The converted Zombie Girl can be recovered to Husk by using a sponge to it.



#### Drowned Girl (HMaG)

##### Befriending

Identical to Zombie Girls.

##### Interaction

Identical to Zombie Girls.

##### Features

Drowned Girls can swim in water like vanilla Drowned. A known issue is they move very slowly in shallow water (1-2 blocks depth). You may make it teleport on following to remove it from shallow water, as a temporary solution.

If a Drowned Girl is converted from Zombie Girl, it can be converted back by using a sponge.



#### Stray Girl (HMaG)

##### Befriending

Identical to Skeleton Girl.

##### Interaction

Identical to Skeleton Girl.

##### Features

If it's converted from a Skeleton, it can be converted back by using Flint and Steel.



#### Wither Skeleton Girl (HMaG)

##### Befriending

Simple Item-giving process. (Item giving cooldown 10s)

Items accepted:

*Soul Cake Slice*: 1% for 0.60, 4% for 0.45, 15% for 0.30, 80% for 0.15.

*Nether Star*: 20% for 1.00, 80% for 0.50.

*Soul Powder*: 0.005 ~ 0.01.

*Soul Apple*: 0.01 ~ 0.02.

##### Interaction

Identical to Skeleton Girl.



#### Creeper Girl (HMaG)

##### Befriending

Firstly, do the item-giving process. (Item giving cooldown 5s)

Accepted items:

*Particle of Lightning* : 10% for 1/2, 30% for 1/4, 60% for 1/8.

*Gunpowder*: 0.015 ~ 0.03.

*TNT*: 0.03 ~ 0.06.

After the progress reaches 1, the Creeper Girl will stop moving, start swelling for 4s and generate tons of glint and smoke particles. After the 4s, it will make a **HUGE EXPLOSION** (Range 12, block-breaking). When it's swelling, you must stay <8 blocks away\(or it will be interrupted and you have to start from giving it items). You must survive from the explosion, and after that it's done.

**Interaction**

Right click with Flint & Steel to ignite it (not consuming the mob).

You can customize its armor and hand items. There's an extra ammo slot (explosive icon), and only with ammo it can explode (no damage to itself, but will damage any other entities including the player and other befriended mobs).

The ammo slot receives either Gunpowder or TNT and consumes one for each explosion. With Gunpowder its explosion has range 3 and doesn't break blocks. With TNT its explosion has range 4 and breaks blocks (if mobGriefing is true). If it's charged, the explosion range will be doubled, 6 for Gunpowder and 8 for TNT, consuming 2 ammo items and uncharging it.

You can manually charge it by using a Particle of Lightning to it. If it's charged, you can right click with bare hand to uncharge it, dropping a Particle of Lightning.

It can be healed with Redstone (5), Gunpowder(5) and Redstone Block(15).

Creeper Girls don't have bauble slots.

##### Features

On combat, it will try exploding on enemies if having ammo, and try close combat if having a weapon on the main hand.

Its explosion can kill the player, but cannot kill other befriended mobs (including tamed animals e.g. vanilla wolves), leaving 1 HP.



#### Ender Executor (HMaG)

##### Befriending

First, look at it to make it be angry with you. When it's less than 8 blocks away, the process starts.

During the process the mob will always be hostile to you. You must do the item-giving process while getting attacked. (Item giving cooldown 15s)

If it failed to attack you (no matter if blocked with shield) for 10s, it will be interrupted.

Accepted items:

*Ender Pie*: 5% for 3/4, 10% for 1/2, 85% for 1/4.

*Ender Plasm*: 0.01 ~ 0.02

*Eye of Ender*: 0.005 ~ 0.01

Once you get >8 blocks away, the process will be interrupted and it will become neutral.

##### Interaction

You can customize its hand items. There's a block slot (cube icon, not available now) and 2 additional Bauble slots.

It can be healed with Eye of Ender(5).



#### Hornet (HMaG)

##### Befriending

With general item-giving process. (Item giving cooldown 10s)

During the whole process, there must be at least 8 Honey Blocks in the 9x9x9 area around it. (As it can fly, probably you need far more than 8 Honey Blocks.) Once there is <8, it will be interrupted. If it damaged you, there will be 60s cooldown during which you cannot give item, but no interruption.

Accepted Items:

*Honey Bottle*: 0.04 - 0.08 (leaving a glass bottle)

*Honey Block*: 0.08 - 0.16

##### Interaction

You can customize its hand items.

It can be healed with *Honey Bottle* (5), *Honeycomb*(10), *Honey Block*(15) and *Mysterious Petal* (to max).

It has 2 bauble slots.



## Foods

### Soul Cake

Put on the ground to eat. Similar to vanilla cakes, providing nutrition 3 and 30s Undead Affinity effect each slice.

### Soul Cake Slice

The non-block variation of Soul Cake. Each item provides nutrition 9 and 90s Undead Affinity.

### Enderberry

A food providing nutrition 6 and teleport (like Chorus Fruit).

Tip: it's probably unadvisable to eat raw Enderberries. It's much more useful as ingredients for other items.

### Ender Pie

A food providing nutrition 16 and 30s Ender Protection effect.



## Effects

### Undead Affinity

Making undead mobs be neutral to you.

Befriending of most undead mobs requires this effect.

It can be obtained by eating Soul Cake or Soul Cake Slice, or standing on a Soul Carpet.

### Ender Protection

Making you teleport on being hurt.

If you drop into the void with this effect, you'll be pulled up to Y=64. If there are solid blocks around, you'll be teleported onto it. If teleporting fails, you will obtain 10s Slow Falling effect. This will immediately remove the Ender Protection effect.

It can be obtained by eating Ender Pie.



## Blocks

### Soul Carpet

A variation of Carpet. Player standing on it will get Undead Affinity effect which immediately expires once player steps out, including jumping up.



## Items

### Respawner

An item dropped on befriended mob die to respawn the mob. Only owner can pick up and use it to respawn.

The dropped respawner item entities are invulnerable (except to creative player or /kill), and don't expire. If it drops into the void, it will be lifted up and find a solid ground to place. If there's no solid ground, it will get floating.



### Bauble Items

#### Soul Amulet

Max HP +10, ATK +3 (Only for undead mobs)

#### Amulet of Resistance

Armor +4, Speed -20%

#### Fruit of Insomnia (HMaG)

Max HP +60, ATK +8 (Only at night, not repeatable)

#### Healing Jade

Persistently healing 0.1 HP per second.

#### Aqua Jade

Speed 4x in water (Only for Drowned Girls, not repeatable)

#### Poisonous Thorn

Adding Poison III instead of II on attacking. (Only for Hornet)



### Armors

#### Sunhat

Allowing undead mobs not to burn under sun without durability damage. Undead mobs wearing Sunhat will also performs normally under sun, instead of trying to avoid sun as the highest priority.

It's other properties are identical to Leather Helmet.

#### Necromancer's Hat

A helmet giving the wearer Effects Strength II, Haste II, Undead Affinity and **Wither I**.  If the wearer is standing on the Soul Carpet, it will not catch Wither effect.

It's other properties are identical to Leather Helmet.