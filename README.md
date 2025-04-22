# Expanded Experience

A plugin that adds ways to get experience in game that feel consistent with vanilla gameplay.

The ways this plugin add to get experience through are:
- Brewing
- Farming
- Shearing
- Archeology
- Piglin Trading

There are 3 ways that experience is done in this plugin:

1) Storage on the block
2) Storage on the player
3) Storage on the entity

## Storage on the block 
For storage on the block, this works just like furnaces do in vanilla. 
- Block stores which recipes have been used
- When the block is broken or the result is pulled out by a player, the experience is calculated and spawned
- This allows for configuration changes to be applied even if someone has a system that automatically makes potions
  - This also prevents spawning experience orbs if someone has a machine making potions, while still keeping the experience there for later.

Just like a furnace, if someone comes and takes something someone else has brewed, the experience will be spawned regardless.

## Storage on the player
Storage on the player is used when the player is directly involved in the action and the action cannot be performed without a player. 
This means Farming, Player Shearing, and Archeology
- Experience is built up until it reaches `1.0`, and then an experience orb is summoned
  - Leftover experience is kept in player data

## Storage on the entity
Storage on the entity is used when a player is not directly tied to the event, and there is not a specific block connected either.
This is used in Piglin Trading
- Disabled by default as this does mean people can have an automatic farm that runs and constantly spawns experience orbs
- Experience is built up until it reaches `1.0`, and then an experience orb is summoned
  - Leftover experience is kept in entity data


## Default Config

For context, 
- Smelting one raw gold give `1.0` experience
- Smelting ancient debris give `2.0` experience
- Smelting a potato gives `0.35` experience
- Breeding animals is anywhere from `1.0` to `14.0` experience

```yml
brewing:
  xp-enabled: true
  xp-amounts:
    turtle_helmet: 2.0
    rabbit_foot: 2.0
    breeze_rod: 1.0
    phantom_membrane: 1.0
    ghast_tear: 1.0
    slime_block: 1.0
    cobweb: 1.0
    dragon_breath: 1.0
    golden_carrot: 0.7
    blaze_powder: 0.7
    glistering_melon_slice: 0.7
    pufferfish: 0.7
    gunpowder: 0.7
    fermented_spider_eye: 0.7
    magma_cream: 0.7
    redstone: 0.5
    glowstone_dust: 0.5
    sugar: 0.5
    spider_eye: 0.5
    stone: 0.5
farming:
  xp-enabled: true
  require-tool: true
  valid-tools:
    - WOODEN_HOE
    - STONE_HOE
    - IRON_HOE
    - GOLDEN_HOE
    - DIAMOND_HOE
    - NETHERITE_HOE
    - WOODEN_AXE
    - STONE_AXE
    - IRON_AXE
    - GOLDEN_AXE
    - DIAMOND_AXE
    - NETHERITE_AXE
  xp-amounts:
    wheat: 0.1
    beetroot: 0.1
    pumpkin: 0.1
    melon: 0.1
    torchflower: 0.5
    pitcher_plant: 0.5
    cocoa: 0.1
    sweet_berries: 0.05
    glow_berries: 0.05
shearing:
  xp-enabled: true
  xp-amounts:
    sheep: 0.2
    mooshroom: 0.5
    bogged: 0.7
    snow_golem: 0.0
archeology:
  xp-enabled: true
  xp-amounts:
    suspicious_sand: 0.8
    suspicious_gravel: 0.8
misc:
  xp-enabled: false
  xp-amounts:
    piglin-barter: 0.1
```
