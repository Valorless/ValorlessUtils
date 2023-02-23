# SakuraTweaks
![Versions](https://img.shields.io/badge/Versions-1.18%20--%201.19%2B-brightgreen?style=flat)<br><br>

# TBD - Disregard everything below.
SakuraHats also has a flew small systems:<br>
***'PlacementBlocker'*** This will prevent players from placing the hats, which would lead to the hats breaking.<br>
***'ResourcePack'*** This will prompt the players upon joining, that the server requires a specific resourcepack to work.<br>

## Commands
| Command | Description |
| --- | --- |
| `/sakurahats` |  |
| `/sakurahats reload` | Reloads config.yml |
| `/sakurahats debug` | Enable/Disable debugging for SakuraHats. |
| `/sakurahats givehat <player> <hat>` | Gives a hat to the specified player. |
| `/sakurahats combine` | Opens GUI to combine helmets with hats. |
| `/sakurahats split` | Opens GUI to split helmets from hats. |

*All commands can be shortened to /sh*
  
## Permissions
| Permission | Description |
| --- | --- |
| `sakurahats.*` | Gives all SakuraHats permissions. |
| `sakurahats.reload` | Allows usage of /sakurahats reload. |
| `sakurahats.debug` | Allows you to enable/disable debugging. |
| `sakurahats.givehat` | Allows you to give hats. |
| `sakurahats.combine` | Allows you to combine hats. |
| `sakurahats.split` | Allows you to split hats. |

## Messages

Within /plugins/SakuraHats/config.yml you'll find a 'message' section. You can customize the messages to anything you want, with or without provided placeholders:
| Placeholder | Description |
| --- | --- |
| `%target%` | The player affected. |
| `%sender%` | The player using the commands. |
| `%hat%` | Name of the hat. |
| `%plugin%` | Will always return '&7[&dSakura&bHats&7]&r'. |

## Configuration
| Config Entry | Description | Default | Comment |
| --- | :---: | :---: | :---: |
| `hats` | List of available hats. |  |  |
| `color` | Color of the item's name. | &6 | To override color, put a color in the hat's name. |
| `prompt-resourcepack` | Whether to promt the player when they log in.<br>(Once) | false |  |
| `pack` | Link to the resourcepack. | [Link](https://sakuramc.netherrain.net/media/files/SakuraMC-Hats.zip) |  |
| `combine` | Whether hats can be combined with helmets. | true |  |
| `split` | Whether helmets can be split from hats. | true |  |
| `debug` | Enabling 'debug' will make the plugin send additional messages in console. | false |  |

Hats and their IDs must match the hats listen in the resourcepack.<br>
Example:
```json
{
	"credit": "By Alynie/Valorless",
	"parent": "block/carved_pumpkin",
	"overrides": [
		{"predicate": {"custom_model_data":1}, "model": "block/sombrero"},
		{"predicate": {"custom_model_data":2}, "model": "block/dimmadome"},
		{"predicate": {"custom_model_data":3}, "model": "block/halo"},
		{"predicate": {"custom_model_data":4}, "model": "block/strawhat"},
	]
}
```
## User Interface (GUI)

| Config Enty | Description |
| --- | --- |
| `gui-name` | Name of the GUI. |
| `gui-size` | The amount of slots in the GUI container. |
| `gui` | Slots defined. |

| GUI Slot Variable | Description |
| --- | --- |
| `name` | Name of the slot's item. |
| `item` | Item material. |
| `lore1` | Lore line 1. |
| `lore2` | Lore line 2. |
| `interact` | Whether the item in this slot can be moved. |
| `tag` | Used to identify player items and buttons.<br> Any item placed in a non-PLAYER slot will be destroyed when closing the GUI. |

| GUI Tags | Description |
| --- | --- |
| `PLAYER` | Slot for the player's item. return these slots to the player upon closing inventory.<br><span style="color:red">IMPORTANT!</span> Only 2 of these, <br>always helmet before hat. |
| `CONFIRM` | Marks this slot as a button to confirm merging. |

<details>
  <summary>GUI Example:</summary>

```yaml
gui:
  '12':
    name: §e§lInfo
    item: IRON_HELMET
    lore1: §fPlace your helmet in the
    lore2: §fempty slot underneath.
    interact: false
    tag: ''
  '14':
    name: §e§lInfo
    item: PLAYER_HEAD
    lore1: §fPlace the hat you wish
    lore2: §fto merge with.
    interact: false
    tag: ''
  '21':
    name: Slot 1
    item: AIR
    lore1: just grass
    lore2: ''
    interact: true
    tag: PLAYER
  '23':
    name: Slot 2
    item: AIR
    lore1: hmm.. glass?
    lore2: yes.
    interact: true
    tag: PLAYER
  '31':
    name: §a§lMerge
    item: LIME_STAINED_GLASS_PANE
    lore1: §fClick here to
    lore2: §fcombine the items.
    interact: false
    tag: CONFIRM

```
You can copy and use this as default.<br>
The reason this is not default, is that if you don't define the ones default in the config file (I removed them all to avoid this),
then any undefined slots would default to the plugin's internal default config.
</details>
