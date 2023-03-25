# *Days with Monster Girls* gameplay instruction



## Version info

### Latest version: 0.0.0-snapshot.2



### *WARNING: everything is experimental now!!! Keep your backup to prevent data-breaking on unexpected errors.*



## Supported mobs

### HMaG

Zombie Girl

Skeleton Girl

Husk Girl

Creeper Girl

## General befriending methods

### Item-giving process

Right click the mob with certain types of item on the main hand to give. When given, the mob's "favorability" will increase depending on the item type. The initial value is 0, and when it reaches 1, the process is done. (This doesn't always mean to successfully befriend. Sometimes other processes start after this.)



## Mob details

#### Zombie Girl (HMaG)

##### Befriending

Using item-giving process. Each item given, there's 10s cooldown.

Accepted items and corresponding favorability increase:

*Soul Cake Slice*:  5% for 1.0,  15% for 2/3, 80% for 1/3

*Soul Apple*: 0.04 ~ 0.08

*Soul Powder*: 0.02 ~ 0.04

During the process, you must keep an "Undead Affinity" effect, which makes undead mobs be neutral to you. Also, you must haven't provoked them, either because of attacking them or getting close to them without Undead Affinity effect. Once you provoked them, it will spend 15 minutes for them to forgive you.

Each time an item is successfully given, green star particles will appear. Each time the favorability increases by 0.2, a heart particle will appear on the mob.

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

*Particle of Lightning*: 10% for 1/2, 30% for 1/4, 60% for 1/8.

*Gunpowder*: 0.015 ~ 0.03.

*TNT*: 0.03 ~ 0.06.

After the favorability reaches 1, the Creeper Girl will stop moving, start swelling for 4s  and generate tons of green-star and smoke particles. After the 4s, it will make a **HUGE EXPLOSION** (Range 12, block-breaking). When it's swelling, you must stay no further than 8 blocks away from it (or it will be interrupted and you have to start from giving it items). You must survive from the explosion, and after that it's done.

**Interaction**

Right click with Flint & Steel to ignite it.

You can customize its armor and hand items. There's an extra ammo slot (explosive icon), and only with ammo it can explode (no damage to itself).

The ammo slot receives either Gunpowder or TNT and consumes one for each explosion. With Gunpowder its explosion has range 3 and doesn't break blocks. With TNT its explosion has range 4 and breaks blocks (if mobGriefing is true). If it's charged, the explosion range will be 6 for Gunpowder and 8 for TNT, consuming 2 ammo items.

You can manually charge it by using a Particle of Lightning to it. If it's charged, you can right click with bare hand to uncharge it, dropping a Particle of Lightning.

##### Features

On combat, it will try exploding on enemies if having ammo, and try close combat if having a weapon on the main hand.



## Foods

### Soul Cake

Put on the ground to eat. Similar to vanilla cakes, providing nutrition 3 and 30s Undead Affinity effect each slice.

### Soul Cake Slice

The non-block variation of Soul Cake. Each item provides nutrition 9 and 90s Undead Affinity.

### Enderberry

A food providing nutrition 6 and teleport (like Chorus Fruit).

Tip: it's probably unadvisable to directly eat Enderberries. It's much more useful as ingredients of other items, or seeds for growing more berries.