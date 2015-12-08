Dragonet
========

[![Join the chat at https://gitter.im/DragonetMC/Dragonet](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/DragonetMC/Dragonet?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

### Brought to you by [BytePowered](http://www.bytepowered.com)

The universal Minecraft server, supports both `Minecraft PC/Mac` and `Minecraft PE` clients.  

[![Build status indicator](https://circleci.com/gh/DragonetMC/Dragonet/tree/master.svg?style=badge)](https://circleci.com/gh/DragonetMC/Dragonet/tree/master)  

##Supported Plugins
Currently it supports Bukkit plugins, Sponge plugins(non-mod) and DAPIS Javascript plugins. 

#### [Bukkit Plugin Compatibility](https://github.com/GlowstoneMC/Glowstone/wiki/Plugin-Compatibility)

##Is this fully working now?
Yes, mostly! But you may expirence some bugs and issues because this software is still in heavy development stage. 

## How can I try Dragonet ?

### Demo Servers
`Always Running Latest Dragonet 0.0.3 Development Build. `
`Installed Bukkit Essentials plugins for only demo purpose. `
### The official [BytePowered](http://www.bytepowered.com) server is not ready yet
#### For Minecraft PC/Mac: `demo.cgrp.co.kr:25565`
#### For Minecraft PE: `demo.cgrp.co.kr` with default port 19132

* There are more community sponsored servers [here](https://github.com/DragonetMC/Dragonet/wiki/Community-Servers).

### Docker image

We provide a Docker image which is automatically updated upon changes on our **master** branch.
It requires [Docker](https://docs.docker.com/engine/installation/), of course.

Just run:
```
docker run -it -p 25565:25565 -p 19132:19132/udp dragonet/dragonet:unstable
```

If you want to keep your data in a known place, you can mount `/dragonet/data` volume:
```
docker run -it -p 25565:25565 -p 19132:19132/udp -v /somewhere/on/my/computer:/dragonet/data dragonet/dragonet:unstable
```

If you want to run it in the background:
```
docker run -d -p 25565:25565 -p 19132:19132/udp -v /somewhere/on/my/computer:/dragonet/data dragonet/dragonet:unstable
```

### About Bukkit Essentials
#### If you are looking for Bukkit Essentials, you should use [Spigot Essentials](https://hub.spigotmc.org/jenkins/job/Spigot-Essentials/).

## Download
You may download pre-compiled binaries at our website:
[http://dragonet.org/](http://dragonet.org/)<br>
You can also get development builds from the [CircleCI](https://circleci.com/gh/DragonetMC/Dragonet/tree/master) page(*Github login required*).  

### Credits & Licencing
Dragonet software: "A universal Minecraft server that supports both Minecraft for PC/Mac and Minecraft: Pocket Edition. "<br>
Dragonet's code is under `org.dragonet` package and they are under LGPL v3. <br>
Glowstone software: `A Minecraft server that written from zero and no Mojang code included. `<br>
Glowstone++ software: `A Glowstone fork by @GlowstonePlusPlus team. `<br />
Glowstone++'s code is under `net.glowstone` package and they are under MIT Licence. <br>
#### Demo server is proudly provided by [BytePowered](http://www.bytepowered.com). <br>
