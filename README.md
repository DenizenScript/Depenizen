Depenizen
=========

**Depenizen: An extension of Denizen**

- **Wiki**: http://wiki.citizensnpcs.co/Depenizen
- **IRC**: http://webchat.esper.net/?channels=denizen-dev
- **Denizen For Bukkit**: https://github.com/DenizenScript/Denizen-For-Bukkit
- **Denizen2Sponge**: https://github.com/DenizenScript/Denizen2Sponge
- **Builds**: http://ci.citizensnpcs.co/job/Depenizen/
  - Use DepenizenBukkit for Bukkit/Spigot servers (install to `your_server_directory/plugins`)
  - Use Depenizen2Sponge for Sponge servers (install to `your_server_directory/mods`)
  - Use DepenizenBungee for BungeeCord servers (see below.)
  - DepenizenBungee supports connections from both DepenizenBukkit and Depenizen2Sponge. It even allows you to use `bungeerun` for cross-compatible script running!

### DepenizenBungee
For communicating with a BungeeCord network, use the following instructions:

1. Install DepenizenBungee on BungeeCord (`your_bungee_directory/plugins`)
2. Load BungeeCord fully and stop it with `end`.
3. Navigate to `your_bungee_directory/plugins/Depenizen` and open `config.yml`.
4. Set `Socket.Enabled` to `true`.
5. Set `Socket.Port` to an unused port number. ***NOT* your usual BungeeCord port!!!**
6. Set `Socket.Max Clients` to the number of Bukkit/Spigot/Sponge servers you're going to use to connect with.
7. Set `Socket.Password` to whatever you want. **KEEP THIS PASSWORD A SECRET!**
8. Save the file.
9. Install DepenizenBukkit or Depenizen2Sponge, depending on your server brand.
10. Load your Bukkit/Spigot or Sponge server fully and stop it with `stop`.
11. If using Bungee/Spigot, navigate to `your_server_directory/plugins/Depenizen`. If using Sponge, navigate to `your_server_directory/config/depenizen2sponge`.
12. Open `config.yml`.
13. Set `Socket.Enabled` to `true`.
14. Set `Socket.IP Address` to the external IP address of your BungeeCord network.
15. Set `Socket.Port` to the same value as in your BungeeCord `config.yml`.
16. Set `Socket.Password` to the same value as in your BungeeCord `config.yml`.
17. Set `Socket.Name` to a **unique** name to identify this server.
18. Repeat steps 9 through 17 for each server on the network.

### Depenizen2Sponge
Supported Plugins: (And the sources we acquired Jar files from.)

- None yet! Please see http://forum.denizenscript.com/viewtopic.php?t=39
- You may post an issue on the issues page to request support for a plugin!

### DepenizenBukkit
Supported Plugins: (And the sources we acquired Jar files from.)

- AreaShop (https://www.spigotmc.org/resources/areashop.2991/)
- ASkyBlock (https://www.spigotmc.org/resources/a-skyblock.1220/)
- BattleNight (http://dev.bukkit.org/bukkit-plugins/battlenight/) - **Very outdated, no evidence of further development**
- dtlTraders (https://dev.bukkit.org/bukkit-plugins/dtltraders/)
- Dynmap (https://www.spigotmc.org/resources/dynmap.274/)
- Essentials (https://hub.spigotmc.org/jenkins/job/Spigot-Essentials/)
- Factions (https://www.spigotmc.org/resources/factions.1900/)
- GriefPrevention (https://www.spigotmc.org/resources/griefprevention.1884/)
- Heroes (https://www.spigotmc.org/resources/heroes.305/)
- HyperConomy (http://dev.bukkit.org/bukkit-plugins/hyperconomy/)
- Jobs Reborn (https://www.spigotmc.org/resources/jobs-reborn.4216/)
- mcMMO (https://www.spigotmc.org/resources/mcmmo.2445/)
- MobArena (http://dev.bukkit.org/bukkit-plugins/mobarena/)
- MythicMobs (https://www.spigotmc.org/resources/mythicmobs.5702/)
- NoCheatPlus (http://ci.md-5.net/job/NoCheatPlus/)
- PlaceholderAPI (https://www.spigotmc.org/resources/placeholderapi.6245/)
- PlotMe (https://dev.bukkit.org/bukkit-plugins/plotme/)
- Prism (https://dev.bukkit.org/bukkit-plugins/prism/)
- PVP Arena (https://ci2.craftyn.com/job/PVP%20Arena/)
- PVP Stats (http://dev.bukkit.org/bukkit-plugins/pvp-stats/)
- Quests (https://www.spigotmc.org/resources/quests.3711/)
- Residence (https://www.spigotmc.org/resources/residence-1-7-10-up-to-1-10.11480/)
- ShopKeepers (http://dev.bukkit.org/bukkit-plugins/shopkeepers/)
- SimpleClans (http://dev.bukkit.org/bukkit-plugins/simpleclans/)
- SkillAPI (http://dev.bukkit.org/bukkit-plugins/skillapi/)
- TerrainControl (https://www.spigotmc.org/resources/terraincontrol.2214/)
- Towny (http://towny.palmergames.com/)
- TownyChat (http://towny.palmergames.com/)
- Vault (http://dev.bukkit.org/bukkit-plugins/vault/)
- Votifier (http://dev.bukkit.org/bukkit-plugins/votifier/)
- WorldEdit (https://dev.bukkit.org/bukkit-plugins/worldedit/)
- WorldGuard (https://dev.bukkit.org/bukkit-plugins/worldguard/)
- You may post an issue on the issues page to request support for a plugin!
