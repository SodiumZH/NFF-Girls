# Befriend Mobs API

## Overview

Befriend Mobs API is intended to help modders easily create features of befriending (or taming) mobs and friendly mobs despite their type hierarchy.

For example, vanilla tamable mobs (e.g. wolf) inherit TamableMob class (except horse and variants). If you want to make hostile mobs (e.g. Zombie) friendly or tamed, only using vanilla API it may become complex. 

You can easily create a subclass of Zombie (or anything else extends LivingEntity) with Befriended Mobs API without using any vanilla TamableMob interfaces, and without considering about the type hierarchy of the existing mobs.



## Key terms

### Befriending

A process for acquire ownership of a certain mob. This concept is more commonly called "Taming", but as I designed this system primarily for humanoid mobs (more specifically, mobs in some monster girl mods), let's use the word "Befriending" instead in this system.

### Befriendable Mobs

Existing mobs that can be befriended. Usually they belong to existing classes, generally not providing any methods to allow players to own them.

### Befriended Mobs

The class of a mob after befriending, usually inheriting the corresponding befriendable mob class. It should have a player as owner, and performs more like "tamed" mobs e.g. wolves. They must **implement IBefriendedMob interface**.

### Befriending Process

A procedure to befriend a mob, i.e. convert a befriendable mob into the corresponding befriended mob. It's highly customizable (see below).



## How to configure a befriendable mob

1) Register a mapping among befriendable mob type, corresponding befriended mob and a befriending handler which defines the befriending process.

To register the mapping, use:

`BefriendingTypeRegistry.register(YourBefriendableMob.getType(), YourBefriendedMob.getType(), new YourBefriendingHandler());`

This action maps the type of befriended



## How to create a friendly mob

1) Create a mob class (anything extends LivingEntity; PathfinderMob recommended) and **implement IBefriendedMob interface**. (Do not create default implementations in IDE)

2) Copy-paste code in "General Settings" box from the example mob class. (Of course you can adjust them if you know what you're doing.)

3) Implement other functions. See comments in the example mob class.