Bungee Bridge
-------------

### Setup

- Spigot/Paper Side:
    - Install [Denizen](https://github.com/DenizenScript/Denizen) and Depenizen ([builds here](https://ci.citizensnpcs.co/job/Depenizen/)) to your Spigot/Paper server.
    - Set the `Bungee server address` and `Bungee server port` in your Depenizen config to your Bungee proxy server's details.
- Bungee (Proxy) Side:
    - Make sure your Bungee config sees the real address of the end server, not an additional relay proxy (the address will be used to validate the connection).
    - Install the Bungee-side plugin (you don't need to configure anything, just toss it in the Bungee plugins folder).
        - Download builds of that here: https://ci.citizensnpcs.co/job/DepenizenBungee/

### Related Links:

- **DepenizenBungee on GitHub**: https://github.com/DenizenScript/DepenizenBungee
