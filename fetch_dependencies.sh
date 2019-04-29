#!/bin/bash
rm libs/craftbukkit-*

wget https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.8.8-R0.1-SNAPSHOT.jar -P libs/
wget https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.9-R0.1-SNAPSHOT.jar -P libs/
wget https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.9.4-R0.1-SNAPSHOT.jar -P libs/
wget https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.10.2-R0.1-SNAPSHOT.jar -P libs/
wget https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.11.2-R0.1-SNAPSHOT.jar -P libs/

wget https://mcmirror.io/files/Spigot/Spigot-1.12-596221b_86aa17f-20170726-0522.jar -P libs/
mv libs/Spigot-1.12-596221b_86aa17f-20170726-0522.jar libs/craftbukkit-1.12.2-R0.1-SNAPSHOT.jar
wget https://mcmirror.io/files/Spigot/Spigot-1.13-fe3ab0d_f41aae4-20180815-2348.jar -P libs/
mv libs/Spigot-1.13-fe3ab0d_f41aae4-20180815-2348.jar libs/craftbukkit-1.13-R0.1-SNAPSHOT.jar
wget https://mcmirror.io/files/Spigot/Spigot-1.13.2-a1f2566-20181123-1027.jar -P libs/
mv libs/Spigot-1.13.2-a1f2566-20181123-1027.jar libs/craftbukkit-1.13.1-R0.1-SNAPSHOT.jar
wget https://mcmirror.io/files/Spigot/Spigot-1.14-0c02b0c-20190425-0538.jar -P libs/
mv libs/Spigot-1.14-0c02b0c-20190425-0538.jar libs/craftbukkit-1.13.1-R0.1-SNAPSHOT.jar