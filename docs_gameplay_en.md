# *Days with Monster Girls* gameplay instruction



## Version info

### Latest version: 

#### 0.0.0-snapshot.3 (1.18.2)

#### 0.1.0-snapshot.3 (1.19.2)



### *WARNING: everything is experimental now!!! Keep your backup to prevent data-breaking on unexpected errors.*



## Supported mobs

### HMaG

Zombie Girl

Skeleton Girl

Husk Girl

Creeper Girl

Ender Executor

## General befriending methods

### Item-giving process

Right click the mob with certain types of item on the main hand to give. When given, the mob's "befriending progress" (or "progress" below) will increase depending on the item type. The initial value is 0, and when it reaches 1, the process is done. (This doesn't always mean to successfully befriend. Sometimes other processes start after this.)

Usually there are both cheap and expensive items to give. Cheap items will push the progress very slightly (usually <10%, sometimes even <1%). Expensive items usually push the progress >10% and, if you're lucky enough, even >50% or immediate success.

#### Interruption

Something may interrupt the process. Once interrupted, the progress will drop back to 0 and everything have to restart.



## Mob details

#### Zombie Girl (HMaG)

##### Befriending

Using item-giving process. Each item given, there's 10s cooldown.

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

There are 2 extra slots (gem icon), and you can put diamond inside, but any related features are not added so far.

They can be healed by using *Soul Powder* (+5) or *Soul Apple* (+15). (Same for all vanilla undead mob girls.)

##### Features

Currently converting to Drowned is not added because Drowned Girl is not finished. If the Zombie Girl is converted from a Husk Girl, it can be converted back by using a sponge to it.



#### Skeleton Girl (HMaG)

##### Befriending

Same to Zombie Girls except the probabilities.

*Soul Cake Slice*: 1% for 1.00, 4% for 3/4, 15% for 1/2, 80% for 1/4.

*Soul Powder*: 0.015 ~ 0.03.

*Soul Apple*: 0.03 ~ 0.06.

##### Interaction

Same to Zombie Girl. 

You can customize their armors, hand items (bow and shield icons) and secondary weapon (sword icon, described below). There is an arrow slot (arrow icon), and you must put arrows inside to enable shooting, and they will consume arrows to shoot.

##### Features

The secondary weapon slot (sword icon) enables the skeleton to automatically swap the weapons on close combat and shooting. When the enemy is too close or the arrows run out, it will try swapping to the sword/tool in the secondary weapon slot for close combat. If the enemy is far away and it has arrows, it will also try swapping to the bow.



#### Husk Girl (HMaG)

##### Befriending

Identical to Skeleton Girl.

##### Interaction

Identical to Zombie Girl. 

##### Features

It can convert to Zombie Girl in water. The converted Zombie Girl can be recovered to Husk by using a sponge to it.



#### Creeper Girl (HMaG)

##### Befriending

Firstly, do the item-giving process.

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

##### Features

On combat, it will try exploding on enemies if having ammo, and try close combat if having a weapon on the main hand.

Its explosion can kill the player, but cannot kill other befriended mobs (including tamed animals e.g. vanilla wolves), leaving 1 HP.



#### Ender Executor

##### Befriending

First, look at it to make it be angry with you. When it's less than 8 blocks away, the process starts.

During the process the mob will always be hostile to you. You must do the item-giving process while getting attacked.

Accepted items:

*Ender Pie*: 5% for 3/4, 10% for 1/2, 85% for 1/4.

*Ender Plasm*: 0.01 ~ 0.02

*Eye of Ender*: 0.005 ~ 0.01

Once you get >8 blocks away, the process will be interrupted and it will become neutral.

##### Interaction

You can customize its hand items. There's a block slot (cube icon) and 2 additional item slots (gem icon) which are unimplemented.



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

A variation of Carpet. Player standing on it will get Undead Affinity effect (which immediately expires once player steps out).

