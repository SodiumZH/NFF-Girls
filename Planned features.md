# *HMaG-Plus* Development Plan

This expansion mod of *HMaG* is intended to (1) allow to tame mobs and let them fight for you, and (2) add more usages of HMaG items & materials.



## Systems

### Favorability (Kizuna Level)

A value indicating how tight your connection with your tamed mob.

It can be increased when your mob kills hostile mobs, receiving a present, or healed.

It can be decreased when you attack your mob, or attack/kill villagers/iron golems/other tamed mobs (including vanilla mobs) near your mob.

If it's higher, more new commands will be unlocked, and better items will drop when receiving a present.

Some mobs don't have this value. 

## Items

### Content

#### Materials

Crystal of Death

Soul Flour

Cloth of Death

#### Foods

Soul Cookie

Soul Pancake





### Undead Related

#### Crystal of Death

##### Crafting

N	Ns	N

Lc	D	Lc

Ec	Pl	Ec

N = *Necrofiber	Ns = *Nether Star Fragment*	Lc = *Lich's Cloth*	Ec = *Evil Crystal*	D = *Diamond*	Pl = *Particle of Lightning*

##### Usage

Brewing with *Raw Portions* to make *Death Affinity Portions*.

Making Soul Flour.



#### Soul Flour

##### Crafting

Crystal of Death + 3 Soul Powder + 3 Wheat => 3 Soul Flour

##### Usage

Making soul foods.



#### Soul Cookie

##### Crafting

2 Soul Flour + Cocoa Bean + Sugar => 4 Soul Cookie

##### Usage

Eating: nutrition of vanilla cookie + Invisibility effect



#### Soul Pancake

##### Crafting

3 Soul Flour + 2 Egg + Milk Bucket => Soul Pancake (leaving a bucket)

##### Usage

Eating: nutrition of vanilla pumpkin pie + Death Affinity effect

Taming undead mobs



#### Cloth of Death

##### Crafting

S	N	S

S	Lc	S

S	S	S

S = String	N = Necrofiber	Lc = Lich's Cloth

##### Usage

Making *Necromancer's Hat*.

Making *Soul Carpet*.



#### Soul Carpet

##### Crafting

*Cloth of Death* + *Soul Powder* *2 -> *Soul Carpet* *8

Same as vanilla carpets. Undead mobs (tamed or not) or players having *Death Affinity* effect will get *Strength II* and *Regeneration I* effects when standing on it.



#### Necromancer's Hat

##### Crafting

4 *Cloth of Death* + *Evil Crystal* => Necromancer's Hat





### Creeper Related

#### Creeper Controller

##### Crafting

Rt	As	Rt

I	As	I

Ec	Ab	Ec

Rt = *Redstone Torch*	As = *Amethyst Shard*	I = *Iron Ingot*	Ec = *Evil Crystal*	Ab = *Amethyst Block*

##### Usage

Bind to a tamed Creeper Girl to configure its behaviors.



### Ender Related

#### Enderberry / Enderberry Jam

##### Crafting

(Any berries in HMaG) + *Ender Plasm* + *Eye of Ender* + *Particle of Lightning* + Evil Crystal => Enderberry Seed =Planting=> Enderberry 1-2 

2 Enderberry + Mysterious Petal + Core Nucleus + Honey Bottle  => Enderberry Jam (in a bottle)

**Usage**

Eating (Enderberry) : 6 hunger + 3.6 saturation + 0.2s (4 ticks) *Saturation I* effect + teleporting (like chorus fruit)

Making *Ender Pie*.



#### Ender Pie

##### Crafting

Enderberry Jam +  *Soul Apple* + 3 Wheat + Egg + Milk Bucket => Ender Pie (leaving a bottle and a bucket)

##### Usage

Eating: 12 hunger + 7.2 saturation + 0.5s (10 ticks) *Saturation II* effect + teleporting

Taming *Ender Executors*.



#### Enderite

##### Crafting

Netherite Ingot + Evil Crystal + Ancient Stone Block + 6 Ender Plasm => Raw Enderite =Blasting=> Enderite Ingot (32 * blasting time for iron)

9 Enderite Ingot <=> Enderite Block

##### Usage

Upgrading Ancient Suit on Smithing Table to make Ender Ancient Suit





### Agriculture

#### Berry Seeds

Same for Cureberry, Randomberry, Expberry, Enderberry

Berry  => Berry Seed

Berry Seed =Planting=> Berry 1-2



### Equipment

#### Necromancer's Hat

4 Cloth of Death + Evil Crystal => Necromancer's Hat

Same defense as *Leather Helmet*. It provides *Strength II*, *Night Vision*, *Haste II* , *Death Affinity* and *Wither I*.



#### Insomnia Set

4 Dyssomnia's Skin + Evil Crystal => Insomnia Cap 	(Lv) + (Lv-1) + (0.3Lv)

6 Dyssomnia's Skin + 2 Evil Crystal => Insomnia Tunic	(1+2Lv) + (Lv-1) + (0.3Lv)

5 Dyssomnia's Skin + 2 Evil Crystal => Insomnia Pants	(2Lv) + (Lv-1) + (0.3Lv)

4 Dyssomnia's Skin + 2 Evil Crystal => Insomnia Boots	(Lv) + (Lv-1) + (0.3Lv)

Lv increase by 1 each 2 days no sleeping; No Lv calculated when Lv=1

Can be repaired on Anvil with Dyssomnia's Skin



#### Ender Ancient Set

Obtained by upgrading Ancient Set with Enderite Ingot

Same armor to Netherite Set; all set bonus of Ancient Set, added by set bonus below:

2 => Immune to Levitation effect

3 => 50% chance to teleport when hit by projectile (consuming a chorus fruit, only when having chorus fruits in the shortcut bar)

4 => 100% chance to teleport 

No teleporting when no chorus fruit in shortcut bar

(Chorus fruit acts as a switch of teleporting)



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

If it's given an HMaG berry (tag hmagplus:hmag_berries),  it will drop an *Ender Eye* / *Ender Plasm* / *Shulker Shell* / *Shulker Shell* *2 (10%), added with *Evil Crystal Fragments* 1~3.  (Shared cooldown 5min)

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



