# Befriend Mobs API

## Overview

Befriend Mobs API is intended to help modders easily create features of befriending (or taming) mobs and friendly mobs despite their type hierarchy.

For example, vanilla tamable mobs (e.g. wolf) inherit `TamableMob` class (except horse and variants). If you want to make hostile mobs (e.g. Zombie) friendly or tamed, only using vanilla API it may become complex. 

You can easily create a subclass of Zombie (or anything else extends LivingEntity) with Befriended Mobs API without using any vanilla TamableMob interfaces, and without considering about the type hierarchy of the existing mobs.



## Key terms

### Befriending

A process for acquire ownership of a certain mob. This concept is more commonly called "Taming", but as I designed this system primarily for humanoid mobs (more specifically, mobs in some monster girl mods), let's use the word "Befriending" instead in this system.

### Befriendable Mobs

Existing mobs that can be befriended. Usually they belong to existing classes, generally not providing any methods to allow players to own them.

### Befriended Mobs

The class of a mob after befriending, usually inheriting the corresponding befriendable mob class. It should have a player as owner, and performs more like "tamed" mobs e.g. wolves. They must **implement IBefriendedMob interface**. 

The term Befriended Mob is actually not limited to mobs after befriending. It can be simply created as a friendly mob inheriting mobs of classes you need.

For creating Befriended Mobs, see below. Simply creating a Befriended Mob class are not related to any Befriendable Mobs.

### Befriending Process

A procedure to befriend a mob, i.e. convert a befriendable mob into the corresponding befriended mob. It's highly customizable (see below).



## How to create a befriended mob class

1. Create a mob class (anything extends Mob; PathfinderMob recommended) **implementing `IBefriendedMob` interface**. 

2. Do general setup: register entity type; register entity renderer; register entity attributes

3. Copy-paste code from the template mob class. 

4. Implement other functions. Positions labeled with `/* ... */` should be changed. Code under "General Settings" box from the template mob class usually doesn't need to modify. (Of course you can adjust them if you know what you're doing.)  See comments in the example mob class for details. 

5. (Optional, if you need GUI) Create the inventory menu. Inventory menu is for add the mob's inventory into the GUI. Inventory menu for befriended mob **inherits `AbstractInventoryMenuBefriended` class**. In this class you need to override `addMenuSlots` method for the inventory of your mob. 

   By default, after running `addMenuSlots`, the player inventory will be automatically added just like in the vanilla inventory screen, at the position specified by `getPlayerInventoryPosition` method which you need to override. If player inventory is not needed, override `doAddPlayerInventory` to false.

   If the Bauble system is applied (see below), you can optionally override `getBaubleSlotAmount`, `getBaubleSlotPositionX` and `getBaubleSlotPositionY` to simplify configuring Bauble slots in the `addMenuSlots` function body. Of course you can also ignore them and set each slot manually.

   Finally, override `makeGui` method to generate GUI screen from this menu. It must return a new GUI screen instance. For how to make, see below.

6. Configure GUI. The GUI screen class for Befriended Mobs **inherit `AbstractGuiBefriended` class**. You must override `getTextureLocation` method to specify the texture resource location, and override `renderBg` to make the GUI background. Generally `render`method doesn't need to be overridden, but if you need some features other than the inventory and mob rendering (e.g. buttons), you need to manually implement them (including pack sending).

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

   This action maps the type of the befriendable mob, the befriended mob and the befriending process handler types. 

2)  Create a handler class inheriting `AbstractBefriendingHandler` and override methods in which you define all behaviors on interacting with mobs of a specific class (defined in step 1 with type mapping). In these overridden methods you can add very complex logic about befriending the mobs. Use `Befriend` in the handler class to finally befriend the mob. (By default it will instantly convert the mob to the corresponding Befriended Mob and do data sync.)

   Please note that if you have a "Debug Befriender" item on the main hand, the interaction method in the handler will NOT be executed, and the target mob will be instantly befriended. (i.e. directly call the `Befriend` method.)

## Features in `IBefriendedMob` interface

1. Ownership-related get/set functions: already defined in the "general setting" in the example and usually you just need to copy-paste it into your code.
2. AI State: there are 3 preset states: wait, follow and wander. You must manually specify which states are allowed in each AI goals (described below).
3.  (To be completed)

## How to port an existing AI Goal to Befriended Goal

1. Create a class inheriting BefriendedGoal or BefriendedTargetGoal.

2. Copy-paste code in the whole existing AI goal.

3. The goal usually has a "mob" reference. Delete it because BefriendedGoal or BefriendedTargetGoal already keeps a "mob" reference as IBefriendedMob. If the owning mob needs some subclasses or interfaces, check and cast them in the constructor.

4. Change the class of input mob reference in the constructors to IBefriendedMob.

5. Now you'll get tons of errors in the IDE. As it will not automatically cast IBefriendedMob to Mob, replace the mob reference to mob.asMob() at each position. 

6. Add AIState filter at the end of the constructors using:

   ```java
   allowAllStates(); // Can use under any AI states
   allowAllStatesExceptWait(); // Can use under any AI states except wait
   allowState(BefriendedAIState state); // Add a state to the allowed list
   disallowState(BefrinededAIState state); // Remove a state from the allowed list
   disallowAllowStates();	// Remove all AI states. Call this before reset AIState filter if the class is inheriting other Befriended (Target) Goal class instead of the raw BefriendedGoal or BefriendedTargetGoal.
   ```

   

7. Add AIState filter at the beginning of `CanUse()` override:

   ```
   if (isDisabled())
   	return false;
   ```

   the `isDisabled()`function will check if the AIState is allowed and if this goal is manually disabled. To manually disable/enable, use `block()` and `unblock()`.

8. Other changes depending on the specific goals.