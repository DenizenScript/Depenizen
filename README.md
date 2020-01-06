Depenizen
---------

**Depenizen: Adds bridges between external plugins and Denizen!**

- **Support Discord**: https://discord.gg/Q6pZGSR
- **Builds (Download)**: https://ci.citizensnpcs.co/job/Depenizen/
- **Supported Plugin List**: [Docs/BukkitPlugins.md](Docs/BukkitPlugins.md)
- **Bungee Bridge**: [Docs/Bungee.md](Docs/Bungee.md)

### Version 2.x notice:

Depenizen version 2.x is a rewrite of many of the core functions of Depenizen.

2.x version does not include support for the following previously supported plugins
due to the plugins being unmaintained or unavailable for modern servers: PlotMe, SimpleClans, Heroes, HyperConomy, Prism, dtlTraders, BattleNight.

2.x version has a completely redesigned Bungee system. Most (but not all!) script tags/events/commands have been kept available to be called in the same way
(though you should review the new bungee meta docs to be sure, as there are some changes - of particular note, 'server@' is no longer used, and 'bungeerun' is now more standardized).
The configuration / setup of Bungee is significantly different, and you will have to redo the setup. Refer to [the new Bungee setup doc](Docs/Bungee.md).
This change is due to the unnecessary complexity of the old system being not worthy of any replication. The new system is much cleaner and easier to work with.

If you need the 1.x (legacy) version, you can find the last official build of that here: https://ci.citizensnpcs.co/job/Depenizen/476/

### Licensing pre-note:

This is an open source project, provided entirely freely, for everyone to use and contribute to.

If you make any changes that could benefit the community as a whole, please contribute upstream.

### The short of the license is:

You can do basically whatever you want, except you may not hold any developer liable for what you do with the software.

### Previous License

Copyright (C) 2014-2019 The Denizen Script Team, All Rights Reserved.

### The long version of the license follows:

The MIT License (MIT)

Copyright (c) 2019-2020 The Denizen Script Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
