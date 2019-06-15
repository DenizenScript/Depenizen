# DepenizenBungee
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
11. If using Bukkit/Spigot, navigate to `your_server_directory/plugins/Depenizen`. If using Sponge, navigate to `your_server_directory/config/depenizen2sponge`.
12. Open `config.yml`.
13. Set `Socket.Enabled` to `true`.
14. Set `Socket.IP Address` to the external IP address of your BungeeCord network.
15. Set `Socket.Port` to the same value as in your BungeeCord `config.yml`.
16. Set `Socket.Password` to the same value as in your BungeeCord `config.yml`.
17. Set `Socket.Name` to a **unique** name to identify this server.
18. Repeat steps 9 through 17 for each server on the network.
