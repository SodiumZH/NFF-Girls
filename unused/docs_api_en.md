# Befriend Mobs API instruction

## Overview

Befriend Mobs system is intended to help modders easily create features of befriending (or taming) mobs and friendly mobs despite their type hierarchy.

For example, vanilla tamable mobs (e.g. wolf) inherit `TamableAnimal` class (except horse and variants). If you want to make hostile mobs (e.g. Zombie) friendly or tamed, only using vanilla API it may become complex, and Befriend Mobs system is designed to help on this. You can easily create a subclass of Zombie (or anything else extends Mob) with Befriended Mobs without using any vanilla `TamableMob` interfaces.



## Key terms

### Befriending

Features for acquiring ownership of a certain mob. This concept is more commonly called "Taming", but as I designed this system primarily for humanoid mobs (more specifically, mobs in some monster girl mods), let's use the word "Befriending" instead in this system.

### Befriendable Mobs

Existing mobs that can be befriended. Usually they belong to existing classes, generally not providing any methods to allow players to own them.

### Befriended Mobs

The class of a mob after befriending, usually inheriting the corresponding befriendable mob class. It should have a player as owner, and performs more like "tamed" mobs e.g. wolves. They must **implement `IBefriendedMob` interface**. 

The term Befriended Mob is actually not limited to mobs after befriending. It can be simply created as a friendly mob inheriting mobs of classes you need.

For creating Befriended Mobs, see below. Simply creating a Befriended Mob class are not related to any Befriendable Mobs.

### Befriending Process

A procedure to befriend a mob, *i.e*. convert a befriendable mob into the corresponding befriended mob. It's highly customizable (see below).



## How to create a befriended mob class

1. Create a mob class (anything extends `Mob`; `PathfinderMob` or `Monster` recommended) **implementing `IBefriendedMob` interface**. 

2. Do general setup: register entity type; register entity renderer; register entity attributes

3. Copy-paste code from the template mob class (in the "template" package). 

4. Implement other functions. Positions labeled with `/* ... */` should be changed. Code under "General Settings" box from the template mob class usually doesn't need to modify. (Of course you can adjust them if you know what you're doing.)  ~~See comments in the example mob class for details.~~ (Not done yet)

5. (Optional, if you need GUI) Create the inventory menu. Inventory menu is for adding the mob's inventory into the GUI. Inventory menu for befriended mob **inherits `AbstractInventoryMenuBefriended` class**. In this class you need to override `addMenuSlots` method for the inventory of your mob. 

   By default, after invoking `addMenuSlots`, the player inventory will be automatically added just like in the vanilla inventory screen, at the position specified by `getPlayerInventoryPosition` method which you need to override. If player inventory is not needed, override `doAddPlayerInventory` to false.

   Finally, override `makeGui` method to generate GUI screen from this menu. It must return a new GUI screen instance. For how to make, see below.

6. Configure GUI. The GUI screen class for Befriended Mobs **inherit `AbstractGuiBefriended` class**. You must override `getTextureLocation` method to specify the texture resource location, and override `renderBg` to make the GUI background. Generally `render`method doesn't need to be overridden, but if you need some features other than the inventory and mob rendering (*e.g*. buttons), you need to manually implement them (including sending packs).

   Go back to the inventory menu class and construct a new GUI instance in `makeGui` method.

   Don't forget to ensure inventory menu and GUI are well aligned.

   On configuring inventory menu and GUI, the `IntVec2` (Integer Vector 2) is utilized to simplify the setting of multiple item slots. (See API for details.)

7. Add opening GUI action. Go back to the mob class and use `BefriendedHelper.openBefriendedInventory` to open the GUI. Please note that this method is only executed on SERVER and send pack to client to execute GUI opening.

## How to configure a befriendable mob

1. Register a mapping between befriendable mob type, corresponding befriended mob and a befriending handler which defines the befriending process.

   To register the mapping, use this in the`FMLCommonSetup` listener function:

   ```java
   BefriendingTypeRegistry.register(YourBefriendableMob.getType(), YourBefriendedMob.getType(), new YourBefriendingHandler());
   ```

   This action maps the type of the befriendable mob, the befriended mob and the befriending process handler type. 

2)  Create a handler class inheriting `AbstractBefriendingHandler` and override methods in which you define all behaviors on interacting with mobs of a specific class (defined in step 1 with type mapping). In these overridden methods you can add very complex logic about befriending the mobs. Use `Befriend` in the handler class to finally befriend the mob. (By default it will instantly convert the mob to the corresponding Befriended Mob and do data sync.)

   Please note that if you have a "Debug Befriender" item on the main hand, the interaction method in the handler will NOT be executed, and the target mob will be instantly befriended. (*i.e*. directly call the `Befriend()` method.)

## Features in `IBefriendedMob` interface

1. Ownership-related get/set functions: already defined in the "general setting" in the example and usually you just need to copy-paste it into your code.
2. AI State: there are 3 preset states: wait, follow and wander. You must manually specify which states are allowed in each AI goals (described below).
3.  (To be completed)

## How to port an existing AI Goal to a Befriended Goal

1. Create a class inheriting `BefriendedGoal` or `BefriendedTargetGoal`.

2. Copy-paste code in the whole existing AI goal.

3. The goal usually has a `mob` reference. Delete it because `BefriendedGoal` or `BefriendedTargetGoal` already keeps a `mob` reference as `IBefriendedMob`. If the owning mob needs some subclasses or interfaces, check and cast them on usage.

4. Change the class of input mob reference in the constructors to `IBefriendedMob`.

5. Now you get tons of errors in the IDE. As it will not automatically cast `IBefriendedMob` to `Mob`, replace the `mob` reference to `mob.asMob()` at each position. The `asMob()` method returns a `Mob` reference of the mob, same as `(Mob)mob`.

6. Add AI State filter at the end of the constructors using:

   ```java
   allowAllStates(); // Can use under any AI states
   allowAllStatesExceptWait(); // Can use under any AI states except wait
   allowState(BefriendedAIState state); // Add a state to the allowed list
   disallowState(BefrinededAIState state); // Remove a state from the allowed list
   disallowAllStates();	// Remove all AI states. Call this before resetting AIState filter if the class is inheriting other Befriended (Target) Goal class instead of the raw BefriendedGoal or BefriendedTargetGoal.
   ```

   

7. Add AI State filter at the beginning of `CanUse()` override:

   ```
   if (isDisabled())
   	return false;
   ```

   the `isDisabled()`function will check if the AI State is allowed and if this goal is manually disabled. To manually disable/enable the whole goal, use `block()` and `unblock()`. (For more details, see `BefriendedGoal` and `BefriendedTargetGoal` classes.)

8. Other changes depending on the specific goals.

## How to use `IntVec2` to simplify inventory menu and GUI configuration

`IntVec2` is a simple integer vector class for easily specifying slot positions in the inventory menu and GUI.

`SlotAbove(n)`, `SlotBelow(n)`, `SlotLeft(n)`, `SlotRight(n)` moves the input vector to the neighboring slot position. The `n` specifies how many times the vector should move, and if it's not specified it will be 1 by default. For example:

```java
IntVec2 v = new IntVec2(x, y); 
addSlot(..., v.x, v.y, ...); 	// Add a slot using the vector
v.slotAbove();	// Move v to the position of the slot above, i.e. 18 pixels upward.
addSlot(..., v.x, v.y, ...);	// Add another slot above the former slot
v.slotRight(2);	// Move v to the position of the 2nd slot on the right, i.e. 36 pixels on the right.
addSlot(..., v.x, v.y, ...);	// Add another slot at the current position. Now v is (x+36, y-18).
```

To specify position of a slot array, use `coord()`. For example:

```java
IntVec2 v = new IntVec2(x, y);	// Specify the base position of the slot array
IntVec2 v1 = v.coord(1, 1);		// Position 1 slot below and 1 slot on the right of the base position v, equals to new IntVec2(v.x + 18, v.y + 18). Please note that coord() returns a new IntVec2 instance and v doesn't change.
```

All math operations to `IntVec2` return itself to enable chain operations. For example:

```java
IntVec2 v = new IntVec2(x, y);
v.add(3, 4);	// Now v is: (x+3, y+4)
IntVec2 v1 = v.addX(3).addY(4).copy();	// Now v is (x+6, y+8), and v1 is copied from v.
v1.add(5, 6);	// Now v is still (x+6, y+8), and v1 is (x+11, y+14).
v.slotAbove().slotRight(2).add(2, 1);	// You can also combine slot operations and math operations in a chain
```

Note: please ***NEVER*** do this: ~~`IntVec2 v1 = v;`~~ This will make `v` and `v1` refer to the same object and when you call `v.add(x, y)` the `v1` will also change. If you need an identical copy, always use `v1 = v.copy()`.

