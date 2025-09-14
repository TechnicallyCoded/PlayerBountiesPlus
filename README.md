# PlayerBountiesPlus

## Overview
PlayerBountiesPlus lets players set and claim bounties while blocking teammates from claiming rewards. Hooks into Vault for economy handling and supports many team plugins.

## Features
- Clan-aware bounty system that prevents teammates from claiming each other's bounties
- Integrates with Vault and supports ClansLite, ClansPlus, Parties, BetterTeams, SimpleClans and Towny for team detection
- Inventory GUI for viewing bounties
- PlaceholderAPI support for custom placeholders
- Localized messages in multiple languages

## Commands
- `/bounty set <player> <amount>` – set a bounty on a player
- `/bounty top` – list the top bounties
- `/bounty check <player>` – check a player's bounty
- `/pbp reload` – reload configuration and messages
- `/pbp version` – display plugin version
- `/pbp bounty set <player> <amount>` – admin set a bounty
- `/pbp bounty add <player> <amount>` – add to a bounty
- `/pbp bounty remove <player> <amount>` – remove from a bounty
- `/pbp bounty delete <player>` – delete a bounty
- `/pbp bounty get <player>` – view a bounty

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
- Commands reside in `com.tcoded.playerbountiesplus.command` with subpackages for bounty and admin actions
- Event listeners like `DeathListener` handle bounty claim logic
- Hooks under `com.tcoded.playerbountiesplus.hook` integrate with Vault, PlaceholderAPI and team plugins
- Inventory GUIs in `com.tcoded.playerbountiesplus.gui` present bounty data
- `BountyDataManager` manages persistence of bounties

