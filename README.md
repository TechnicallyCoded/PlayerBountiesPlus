# PlayerBountiesPlus

## Overview
PlayerBountiesPlus lets servers place and claim monetary bounties on players while blocking kills from clanmates or teammates. It hooks into Vault for economy handling and supports several team plugins so only outsiders can collect rewards.

## Features
- Clan-aware bounty system that prevents teammates from claiming each other's bounties【F:src/main/resources/plugin.yml†L6】
- Integrates with Vault and supports ClansLite, Parties, BetterTeams, SimpleClans and Towny for team detection【F:src/main/resources/plugin.yml†L10-L17】
- Inventory GUI for viewing bounties【F:src/main/java/com/tcoded/playerbountiesplus/gui/MainBountyGui.java†L1-L24】
- PlaceholderAPI support for custom placeholders【F:src/main/java/com/tcoded/playerbountiesplus/hook/placeholder/PlaceholderAPIHook.java†L1-L20】
- Localized messages in multiple languages【7bb75a†L1-L13】

## Commands
- `/bounty set <player> <amount>` – set a bounty on a player【F:src/main/java/com/tcoded/playerbountiesplus/command/BountyCommand.java†L35-L40】
- `/bounty top` – list the top bounties【F:src/main/java/com/tcoded/playerbountiesplus/command/BountyCommand.java†L35-L41】
- `/bounty check <player>` – check a player's bounty【F:src/main/java/com/tcoded/playerbountiesplus/command/BountyCommand.java†L35-L43】
- `/pbp reload` – reload configuration and messages【F:src/main/java/com/tcoded/playerbountiesplus/command/PlayerBountiesPlusAdminCmd.java†L40-L42】
- `/pbp version` – display plugin version【F:src/main/java/com/tcoded/playerbountiesplus/command/PlayerBountiesPlusAdminCmd.java†L40-L44】
- `/pbp bounty set <player> <amount>` – admin set a bounty【F:src/main/java/com/tcoded/playerbountiesplus/command/admin/bounty/AdminBountyCmd.java†L20-L23】
- `/pbp bounty add <player> <amount>` – add to a bounty【F:src/main/java/com/tcoded/playerbountiesplus/command/admin/bounty/AdminBountyCmd.java†L20-L25】
- `/pbp bounty remove <player> <amount>` – remove from a bounty【F:src/main/java/com/tcoded/playerbountiesplus/command/admin/bounty/AdminBountyCmd.java†L20-L27】
- `/pbp bounty delete <player>` – delete a bounty【F:src/main/java/com/tcoded/playerbountiesplus/command/admin/bounty/AdminBountyCmd.java†L20-L29】
- `/pbp bounty get <player>` – view a bounty【F:src/main/java/com/tcoded/playerbountiesplus/command/admin/bounty/AdminBountyCmd.java†L20-L31】

## Permissions
| Permission | Default |
| --- | --- |
| `playerbountiesplus.command.bounty` | all |
| `playerbountiesplus.command.bounty.set` | all |
| `playerbountiesplus.event.claim` | all |
| `playerbountiesplus.command.admin` | op |
| `playerbountiesplus.command.admin.version` | op |
| `playerbountiesplus.command.admin.reload` | op |
| `playerbountiesplus.command.admin.bounty.set` | op |
| `playerbountiesplus.command.admin.bounty.add` | op |
| `playerbountiesplus.command.admin.bounty.remove` | op |
| `playerbountiesplus.command.admin.bounty.delete` | op |
| `playerbountiesplus.command.admin.bounty.get` | op |

## Developer Notes
- Commands reside in `com.tcoded.playerbountiesplus.command` with subpackages for bounty and admin actions【F:src/main/java/com/tcoded/playerbountiesplus/command/BountyCommand.java†L1-L44】【F:src/main/java/com/tcoded/playerbountiesplus/command/admin/bounty/AdminBountyCmd.java†L1-L31】
- Event listeners like `DeathListener` handle bounty claim logic【F:src/main/java/com/tcoded/playerbountiesplus/listener/DeathListener.java†L1-L28】
- Hooks under `com.tcoded.playerbountiesplus.hook` integrate with Vault, PlaceholderAPI and team plugins【F:src/main/java/com/tcoded/playerbountiesplus/hook/placeholder/PlaceholderAPIHook.java†L1-L20】【F:src/main/java/com/tcoded/playerbountiesplus/hook/team/TownyHook.java†L1-L30】
- Inventory GUIs in `com.tcoded.playerbountiesplus.gui` present bounty data【F:src/main/java/com/tcoded/playerbountiesplus/gui/MainBountyGui.java†L1-L24】
- `BountyDataManager` manages persistence of bounties【F:src/main/java/com/tcoded/playerbountiesplus/manager/BountyDataManager.java†L1-L28】

