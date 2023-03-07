# Befriend Mobs API

Befriend Mob API is intended to help modders easily create features of befriending (or taming) mobs and friendly mobs despite their type hierarchy.

For example, vanilla tamable mobs (e.g. wolf) inherit TamableMob class (except horse and variants). If you want to make hostile mobs (e.g. Zombie) friendly or tamed, only using vanilla API it may become complex. 

you can easily create a subclass of Zombie (or anything else extends LivingEntity) with Befriended Mobs API without using any vanilla TamableMob interfaces, and without considering about the type hierarchy of the existing mobs.

## How to create a friendly mob

1) Create a mob class (anything extends LivingEntity; PathfinderMob recommended) and **implement IBefriendedMob interface**. (Do not create default implementations in IDE)

2) Copy-paste code in "General Settings" box from the example mob class. (Of course you can adjust them if you know what you're doing.)

3) Implement other functions. See comments in the example mob class.