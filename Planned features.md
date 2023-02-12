# *Days with Monster Girls* Development Plan

## Development Targets

This mod is intended to allow monster girls of several other mods to be friendly and to interact with them.

Also, some additional features will be added as enhancements of the dependent mods.

Current dependencies:

Hostile Mobs and Girls (by Mechalopa) (mandatory)

Grimoire of Gaia 4 (by  Silentine & Mrbysco) (mandatory)



## Key Concepts

### Taming

To make a mob permanently friendly and interactable. A tamed mob can fight for you, and also have some behaviors on daily Minecraft actions. 

This is for making the player feel they are "living with" the mobs instead of just regard them as tools for fighting.

The term "taming" is just for intelligibility for developers. The formal name should be "befriending".



## Systems

### Favorability Level

A value indicating how tight your connection with your tamed mob.

It can be increased when your mob kills hostile mobs, receiving a present, or healed.

It can be decreased when you attack your mob, or attack/kill villagers/iron golems/other tamed mobs (including vanilla mobs) near your mob.

If it's higher, more new commands will be unlocked, and better items will drop when receiving a present.

Some mobs don't have this value. 





## Features

### Undead Related

#### Effects

##### Death Affinity

The key effect for the whole features of undead.

It will make the undead mobs neutral. And some actions are available only with this effect.



#### Materials

##### Crystal of Death

A key material for several undead-related features.

Expensive, using multiple products from undead mobs.

##### Cloth of Death

An undead material for making related armor and decorative blocks.

Not very expensive.



#### Foods

Undead foods are edibles providing some effects and applied for interaction with undead mobs.

Usually undead foods provide Death Affinity, and sometimes other effects.

Those applied on taming should be VERY expensive.

##### Soul Flour

A key material for making undead foods.

##### Soul Cookie

The undead version of Cookie.

Not very expensive.

##### Soul Pancake

The undead version of Pumpkin Pie.

Expensive.

##### Soul Cake

The undead version of Cake. 

Very expensive. For taming.

##### Soul Cake Slice

The non-block version of Soul Cake.

Very expensive. For taming.



#### Blocks

##### Soul Carpet

The undead version of Carpet. 

Undead mobs or players having *Death Affinity* effect will get *Strength II* and *Regeneration I* effects when standing on it.

It could have a pure-decorative version, which is cheaper and not providing any effects.



#### Equipment

##### Necromancer's Hat

A helmet providing several positive effects as well as **Wither**. (Wearing this hat will lead to consistent HP loss)

Some actions are available only wearing this hat.

##### Necromancer's Wand

A weapon allowing to attack non-undead mobs with negative effects, and heal undead mobs.

Some other actions need this weapon.



### Ender Related

#### Foods

##### Enderberry / Enderberry Jam

The key material for all Ender foods.

Obtaining Enderberry should be very hard.  It's a treasure, and can also be made in a very complex way.

##### Ender Pie

The Ender version of Pumpkin Pie.

Just like the Chorus Fruit, it can teleport the player.

Very expensive.



#### Minerals

##### Enderite

A rare mineral (even more hard to obtain than Netherite).

For making Ender tools/weapons/armor, mostly featured with teleporting.

##### Enderite Tools/Weapons

Allowing the player or the attacked target to teleport (switchable).

##### Enderite Armor

Allowing the player to teleport when getting hurt (switchable).



### Agriculture

#### Berry Seeds

Same for Cureberry, Randomberry, Expberry, Enderberry

Berry  => Berry Seed

Berry Seed =Planting=> Berry 1-2





### Miscellaneous

#### Mob Notebook

##### Crafting

Book + Evil Crystal Fragment => Mob Notebook

##### Usage

Description for HMaG mobs





## Effects

### Death Affinity

Make all undead mobs neutral to the player. (Except if it's already hostile)

Potion: 

Crystal of Death + Raw Potion => Death Affinity Potion (1.5 min) =Redstone=> 4 min

## Taming

### General

Every type of mobs should have its original taming method. Also, tamed mobs will have different behaviors from before, some are similar, and some could be very different.

To tame a mob, the player must not have made any damage to it. Also, if a taming process takes some time, the player must not die for any reason during the process.



### Details

#### Zombie Girl / Skeleton Girl / Husk Girl / Stray Girl / Drowned Girl / Wither Skeleton Girl

##### Taming condition

The player must have the *Death Affinity* effect, and the mob must not have already been hostile to the player. At this time the player could give (right click) the mob 1-3 *Soul Pancake(s)* to tame it. (10% for 1, 30% for 2, 60% for 3.)

##### Behaviors

After tamed, all their equipment will vanish. (Except picked-up items, they will drop.)  You can customize their weapon and armor via GUI.

They can be healed by feeding with *Bone Meal*.



#### Creeper Girl

##### Taming condition

Give it 3 TNTs. (It's difficult to do that before it explodes! ) After that the Creeper Girl will stop moving and exploding, and start shaking (like Zombie Villagers) for 5-10s. During the process, once the player gets >4 blocks away the Creeper Girl, the process will stop. (The TNTs are gone and it restarts to chase you and explode.) After the process, a huge explosion (power 12, twice of the charged Creeper) will happen. If the player is not killed by the explosion, it's done. 

##### Behaviors

Tamed Creeper Girl will no longer randomly explode. You can give it TNTs to allow it to explode (power 3 at its position, no damage to itself but can damage the player). One TNT can provide one explosion. You can bind a *Creeper Controller* to a specific Creeper Girl to control its explosion. Whether the explosion breaks blocks can be switched on *Creeper Controller*. You can also use a *Particle of Lightning* to make it charged, and its next explosion will have power 6 and consume 2 TNTs.

Its armor can be customized.

It can be healed by feeding with *Gunpowder*.



#### Ender Executor

##### Taming condition

Keep it not hostile and give it 3-5 *Ender Pies*. (10% for 3, 30% for 4, 60% for 5.) After that it will temporarily become hostile for 10-20s, and you cannot get >4 blocks away or die(or taming will fail). After the process it's done.

##### Behaviors

Similar to wolves. When hostile to a mob, it will perform similarly to the behaviors when it attacks players.  However, if it touches water (including rain) or gets hit by projectiles, it will still randomly teleport.

Its weapon can be customized.

It can be healed by feeding with *Chorus Fruit*.

If it's given an *apple* or a *lemon*, it will drop an *Ender Pearl */ *Ender Plasm* / *Evil Crystal Fragment* . 

If it's given an HMaG berry (tag dwmg:hmag_berries),  it will drop an *Ender Eye* / *Ender Plasm* / *Shulker Shell* / *Shulker Shell* *2 (10%), added with *Evil Crystal Fragments* 1~3.  (Shared cooldown 5min)

For other mods:

Alex's Mobs: Core Nucleus => *Mimicream* (100%) + *Evil Crystal* (11%)

BYG: Diamond => Ametrine Gem / Therium Shard / Budding Ametrine Ore (very rare) / Therium Block + Therium Remover (very very rare)



#### Necrotic Reaper

##### Taming condition

The player must wear *Necromancer's Hat*. Make the Necrotic Reaper stand on the *Soul Carpet* (no matter how, but it cannot become hostile), and give it a *Soul Cake*.

##### Behaviors

The player must wear *Necromancer's Hat* to let the Necrotic Reaper follow and fight for you. If your Necrotic Reaper is <8 blocks away from you, you will get *Regeneration I* effect, partly offsetting the *Wither I* effect brought by *Necromancer's Hat*.

If the player isn't wearing *Necromancer's Hat*, it will only walk randomly and will not provide you any effect. However, it will never attack you.

It can be healed by feeding with *Soul Apple*.



## Present Looting-Table

For tamed monster girls, you can give specific items as present to increase the favorability, and receive a present back. Each mob has its gifting cooldown.



### Zombie Girl / Skeleton Girl / Husk Girl / Stray Girl / Drowned Girl / Wither Skeleton Girl

Cooldown: 30 s

Soul Powder => An item in killing looting table

Soul Apple / Soul Cookie => An item in killing looting table, except Rotten Flesh and Bone. For Skeleton & Stray, Bone Block takes the place of Bone.

For Drowned, a Trident is rarely dropped.



### Creeper Girl

Cooldown: 5 min

Cookie / Pumpkin Pie => Gunpowder / Evil Crystal Fragment (ECF below)



### Ender Executor

Cooldown: 20 min

Apple / Lemon => Ender Pearl / Ender Plasm / ECF

HMaG Berry => Ender Eye / Ender Plasm / Shulker Shell 1-2

(Alex's Mobs) Core Nucleus => Mimicream (100%) + Evil Crystal

(BYG) Diamond => Ametrine Gem / Therium Shard / Budding Ametrine Ore (very rare) / Therium Block (very very rare)

* Therium Remover: a single-use item to remove a Therium Block, dropping 1-3 Therium Shards.



