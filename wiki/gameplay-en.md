# *Days with Monster Girls* gameplay instruction

## Wiki Version info

### Current version: 

#### 0.0.9 (1.18.2)

#### 0.1.9 (1.19.2)



### *WARNING: This mod is still in development. Keep your backup to prevent data-breaking on unexpected errors.*

##### Note: the wiki update may lag behind the game. Check which version the wiki is supporting. You could also check the wiki/gameplay_en.md file, but it may contain information and changes that haven't been published.



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

Necrotic Reaper

Banshee (0.0.5 / 0.1.5)

Ghastly Seeker (0.0.6 / 0.1.6)





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

## Favorability System

Favorability shows the affinity between you and your mob. It's a value between 0 - 100, and 50 initially.

You can slightly increase the favorability by doing things below:

- Sleeping with your mob nearby.
- Attacking and killing enemies with your mob.
- Healing them with items.

Actions below will GREATLY drop the favorability:

- Killing your mob by yourself will drop the favorability to zero.
- Attacking your mob. (If the damage value is below 0.5 it will not drop.)
- Watching your mob die in front of you (-20).

High favorability will increase the mob's attack damage, while low favorability will decrease it. Also, if the favorability is below 5, the mob will refuse to follow you or attack your target.

For their attitudes towards you, be kind to your mobs!

## Experience and Level

Mobs can upgrade their level by killing enemies. If an enemy is killed by a befriended mob, it will not drop EXP Orbs, but the EXP will be directly given to your mob. The EXP amount needed for level-up is identical to players. The given EXP can also fix its equipment with Mending enchantment.

Each level increases the mob's max HP by 1 and attack damage by 0.1.

If the mob dies, it will lose a half of all its EXP accumulated.

## Mob details

#### Zombie Girl (HMaG)

##### Befriending

Using simple item-giving process. (Item giving cooldown 10s)

Accepted items and corresponding progress increase:

*Soul Cake Slice*:  5% for 1.0,  15% for 2/3, 80% for 1/3

*Soul Apple*: 0.04 ~ 0.08

*Soul Powder*: 0.02 ~ 0.04

During the process, you must keep an "Undead Affinity" effect, which makes undead mobs be neutral to you. Also, you must haven't provoked them, either because of attacking them or getting close to them without Undead Affinity effect. Once you provoked them, it will spend ~~15~~ 5 minutes for them to forgive you.

Each time an item is successfully given, glint particles will appear. Each time the favorability increases by 0.2, a heart particle will appear on the mob.

Once you provoked them and/or lost the Undead Affinity effect, the process will be interrupted.

##### Interaction

To interact, player must have a Commanding Wand on either hand. (0.0.6 / 0.1.6)

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

If it failed to attack you for 10s, the progress will drop by 0.2. (If the attack is Shield-blocked, it's still regarded as attacked.)

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

During the whole process, there must be at least 8 Honey Blocks in the 9x9x9 area around it. (As it can fly, probably you need far more than 8 Honey Blocks.) Once there is <8, ~~it will be interrupted~~ the progress will drop by 0.1 each second. If it damaged you, there will be 60s cooldown during which you cannot give item, but no interruption.

Accepted Items:

*Honey Bottle*: 0.04 - 0.08 (leaving a glass bottle)

*Honey Block*: 0.08 - 0.16

##### Interaction

You can customize its hand items.

It can be healed with *Honey Bottle* (5), *Honeycomb*(10), *Honey Block*(15) and *Mysterious Petal* (to max).

It has 2 bauble slots.



#### Necrotic Reaper (HMaG)

##### Befriending

At first you must be wearing Necromancer's Hat on head and have a Necromancer's Wand.

When it's standing on a Soul Carpet, cast with *Necromancer's Wand* to hit it. 6 hits overall are required. Once hit, it will be always hostile to you. Each time it's hit, it will release more smoke particles, and increase attack damage by 5. 

If it failed to attack you for 10s, the hit count will drop by 1. (If the attack is Shield-blocked, it's still regarded as attacked.)

If you get >32 blocks away from it, the process will be interrupted.

##### Interaction

To interact with it, you must either wear a Necromancer's Hat or hold a Necromancer's Wand on either hand. Otherwise ~~you'll not be able to switch its AI state or open the inventory GUI, and~~ it will refuse to follow you or attack your targets. The requirements are removed if its favorability reaches 90.

Necromancer's Wand is required instead of Commanding Wand to switch AI state and open the GUI.

You can customize its hand items. The main hand only accept hoes as weapon. (Non-weapon items are also acceptable, but not taking any effect.)  The offhand slot doesn't accept anything so far. (Will be added in the future.)

Its attack damage greatly increases depending on the tier of the hoe. Efficiency enchantment also increases the attack damage of the hoe, 10% each level.

Hoe attack (only for Necrotic Reapers):  Wood/Gold = 1, Stone = 2, Iron = 3, Diamond = 5, Netherite = 8. Effeciency enhancement will be multiplied to this value, each level provides 10%. For example, a Netherite Hoe with Efficiency V will provide ATK = 8 x (1 + 10% * 5)  = 12. 

*Tip: if other mods provide hoes with higher tier than Netherite, it will be a strong weapon. Tiers above Netherite hoes (without Efficiency) will be subsequently: 13, 21, 34(max).

If the hoe has Fortune enchantment, more items will drop on killing mobs as if it has Looting enchantment. (0.x.8)

It has 4 Bauble slots.

It can be healed with Soul Powder (5) and Soul Apple (15).

~~It will be healed by 2 if hit by its owner's *Necromancer's Wand* magic ball. If the owner is wearing a *Necromancer's Hat*, it will be healed by 3 and get 5s Strength II effect. (0.x.8)~~

When it's affected by owner's *Necromancer's Wand* magic ball, it will be healed by 2 (blasted) or 4 (directly hit). If owner is wearing *Necromancer's Hat*, it will be healed by 3 (blasted) or 6 (directly hit), and get Strength effect for 10s (blasted) or 15s (directly hit). (0.x.8)

When the player is wearing a *Necromancer's Hat*, the owning Necrotic Reapers nearby (< 8 blocks) will provide Regeneration effect. The effect level depends on how many Necrotic Reapers there are around. (Regeneration III at most.)



#### Banshee (HMaG)

##### Befriending

Using item-giving process. Accepted items are identical to Skeleton Girls.

To give items, the player must have Wither effect (not the effect of Necromancer's Hat), and there are must >=8 wither roses (blocks) in the 15x15x15 area centered by the mob. Once there're <8, the progress will drop by 0.1 each second. If it damaged you, there will be 60s cooldown during which you cannot give item, but no interruption (no losing progress).

##### Interaction

You can customize its hand items. The item on off-hand can only stack to 1 no matter it's originally stackable.

When it's off-hand is holding a flower, it will apply effects of the Suspicious Stew of the corresponding flower. If the effect is positive, it will be applied to the allies in 8 blocks each 15 s, including the owner, the owner's other befriended mobs and tamed animals. If the effect is negative, it will be applied to the target on attacking. 

It can be healed by using *Soul Powder* (+5) or *Soul Apple* (+15). (Same as vanilla undead mob girls.)

It has 3 bauble slots.



#### Ghastly Seeker (HMaG) (0.0.6 / 0.1.6)

##### Befriending

Using item-giving process. Accepted items are identical to Skeleton Girls.

Ghastly Seekers are befriendable only when they're in the Overworld and can see sky (no opaque blocks above).

##### Interaction

It has 4 bauble slots.

It requires ammo to fire. The ammo slot (explosive icon) accepts either Fire Charges or Blasting Bottles (HMaG). When using Fire Charges, the explosions of the fireballs have normal power and don't break blocks. When using Blasting Bottles, the explosions have 1.5x normal power and break blocks.

The base explosion power is 1. Each ATK point adds extra 0.1 to the power. 



#### Kobold (HMaG) (0.0.9 / 0.1.9)

**Befriending**

Drop required items on the ground and it will try picking it up like Piglins. It will hold the item for a few seconds, consume it and increase the progress. The progress value mechanism is similar to item-giving process.

Accepted items:

*Iron Ingot* : 0.02 - 0.04

*Gold Ingot*: 0.03 - 0.05

*Emerald*: 0.03 - 0.05

*Diamond*: 0.05 - 0.10

*Netherite Scrap*: 0.15 - 0.25

*Netherite Ingot*: 0.30 - 0.50

*Iron Pickaxe*: 0.05 - 0.07

*Gold Pickaxe*: 0.07 - 0.10

*Diamond Pickaxe*: 0.12 - 0.18

*Netherite Pickaxe*: 0.50 - 1.00

(Pickaxes cannot be recovered after successfully befriending.)

When the progress is over 0.7, it will become neutral.

##### Interaction

It has 2 Bauble slots.

Its main hand can only hold pickaxes. Like for Necrotic Reapers, Fortune enchantment will act as Looting on killing mobs.

If it's holding mineral nuggets on the off hand, it will help locate surrounding corresponding ores, even if not seen. When it found the ore, it will consume a nugget, try moving towards the ore and generate massive glint (green star) particles.

Supported minerals: Copper, Iron, Gold, Emerald, Diamond, Coal, Lapis Lazuli, Redstone.

For Coal, Lapis Lazuli and Redstone, the mineral itself is required instead of the nugget.

It will try locating ores only under Follow mode.

It can be healed with Apple (5), Cookie (5), Pumpkin Pie (5), Lemon (10), Lemon Pie (20) and Golden Apple (to max).



#### Imp (HMaG) (0.x.9)

##### Befriending

Similar to Kobold.

Accepted items:

*Apple*: 0.02 - 0.04

*Warped Fungus* and *Crimson Fungus*: 0.03 - 0.05

*Golden Apple*: 0.12 - 0.24

*Lemon*: 0.03 - 0.05

*Golden Tropical Fish*: 0.08 - 0.16

*Gilded Blackstone*: 0.04 - 0.07

*Enchanted Golden Apple*: 0.80 - 1.40

##### Interaction

It has 2 Bauble slots.

Like Kobold, it can locate Ancient Debris. It required the mob to hold a Netherite Fork on the main hand and have Netherite Scrap nuggets on the off hand.

It can be healed with Apple (5), Cookie (5), Pumpkin Pie (5), Lemon (10), Lemon Pie (20) and Golden Apple (to max).





## Foods

### Soul Cake

Put on the ground to eat. Similar to vanilla cakes, providing nutrition 3 and 1.5 min Undead Affinity effect each slice.

### Soul Cake Slice

The non-block variation of Soul Cake. Each item provides nutrition 9 and 5 min Undead Affinity.

### Enderberry

A food providing nutrition 6 and teleport (like Chorus Fruit).

Tip: it's probably unadvisable to eat raw Enderberries. It's much more useful as ingredients for other items.

### Ender Pie

A food providing nutrition 16 and 3 min Ender Protection effect.



## Effects

### Undead Affinity

Making undead mobs be neutral to you.

Befriending of most undead mobs requires this effect.

It can be obtained by eating Soul Cake or Soul Cake Slice, standing on a Soul Carpet or wear a Necromancer's Hat.

### Ender Protection

Making you teleport on being hurt.

If you drop into the void with this effect, you'll be pulled up to Y=64. If there are solid blocks around, you'll be teleported onto it. If teleporting fails, you will obtain 10s Slow Falling effect. This will immediately remove the Ender Protection effect.

It can be obtained by eating Ender Pie.

### Curse of Necromancy

The harmful effect of Necromancer's Hat. This effect is same as Wither, but not causing the health bar turning black. ~~Wither Skeletons are not immune to this effect. (0.x.7)~~ Wither Skeletons are immune to this effect. (0.x.7)



## Blocks

### Soul Carpet

A variation of Carpet. Player/mob standing on it will get Undead Affinity effect which immediately expires once player steps out, including jumping up. It will also remove the ~~Wither I~~ Curse of Necromancy effect of Necromancer's Hat.



## Items

### Respawner

An item dropped on befriended mob die to respawn the mob. Only owner can pick up and use it to respawn.

The dropped respawner item entities are invulnerable (except to creative player or /kill), and don't expire. If it drops into the void, it will be lifted up and find a solid ground to place. If there's no solid ground, it will get floating.



### Bauble Items

#### Soul Amulet

(Wearable only for undead mobs)

Lv. 1: Max HP +10, ATK +3, sun immunity 

Lv. 2: Max HP +15, ATK +5, sun immunity

#### Amulet of Resistance

Lv. 1: Armor +4, Speed -10%, sun immunity for undead mobs

Lv. 2: Armor +6, Speed -10%, sun immunity for undead mobs

#### Fruit of Insomnia (HMaG)

Max HP +60, ATK +8 (Only working at night, applying only once)

#### Healing Jade

Persistently healing 0.1 HP per second.

#### Life Jade

Lv. 1: Max HP +5, persistently healing 0.15 HP per second

Lv. 2: Max HP +10, persistently healing 0.2 HP per second

#### Aqua Jade

Speed 4x in water (Only usable for Drowned Girls, applying only once)

#### Poisonous Thorn

Adding Poison III instead of II on attacking. (Only usable for Hornet)



### Armors

#### Sunhat

Allowing undead mobs not to burn under sun without durability damage. Undead mobs wearing Sunhat will also performs normally under sun, instead of trying to avoid sun as the highest priority.

It's other properties are identical to Leather Helmet.

#### Necromancer's Hat

A helmet giving the wearer Effects Strength II, Haste II, Undead Affinity and ~~**Wither I**~~ **Curse of Necromancy**.  If the wearer is standing on the Soul Carpet, it will not catch ~~Wither~~ the curse effect.

It's durability is identical to Iron Helmet, and other properties are identical to Leather Helmet.



### Weapons & Tools

#### Commanding Wand

Hold on either hand to switch mobs' AI state and open the GUI.

Use this item to (right click) an Evil Crystal Block to set all mobs' AI state to Follow in the 32x32x32 area centered by the block, and simultaneously move all respawners in the area onto the block.



#### Evil Magnet

Use this item to an Evil Crystal Block to move all respawner in the world onto the block.

It has 8 durability and can be repaired on anvil with Evil Crystal. Moving each Respawner costs 1 durability.



#### Necromancer's Wand

~~On usage it can shoot a magic ball which adds Wither III for 5s on hitting living entity. If the user is wearing a Necromancer's Hat, it will add Wither IV instead of III. (Changed: 0.1.8 / 0.0.8)~~  

On usage it shoots a magic ball which blasts on hitting blocks or entities, and give Wither III effect for 10 s to living entities within 1.5 blocks away. If the shooter is wearing Necromancer's Hat, it will apply Wither IV.

If the magic ball hits a living entity directly, the Wither level will increase by 1.

For owning befriended Necrotic Reapers, it doesn't give Wither but positive effects. The mob will be healed by 2 or 4 (directly hit). If shooter is wearing Necromancer's Hat, it will be healed by 3 or 6 (directly hit) and get Strength II effect for 10s or 15s (directly hit).

Each time used, the user will take a damage of 2.

Durability: 64 ~~(accepting Unbreaking and Mending enchantment)(0.x.9)~~ Not accepting any enchantments.

It will be unable to use if leaving only 1 durability, preventing it from breaking. 

It can be repaired with Death Crystal Powder on Anvil. Each item repairs ~~16(0.x.9)~~ 32 durability.



#### Netherite Fork (0.x.9)

An upgrade of Golden Fork (HMaG). Aside of general properties, Imps requires this item for locating Ancient Debris blocks.



#### Mob Storage Pod (0.x.10)

Right click the mob with this item to store the mob inside. This item is single-use.