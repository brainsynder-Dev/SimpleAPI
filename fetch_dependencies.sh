#!/bin/bash
mkdir -p libs
rm libs/*.jar
wget https://cdn.getbukkit.org/craftbukkit/craftbukkit-1.8.8-R0.1-SNAPSHOT-latest.jar -P libs/
wget https://mcmirror.io/files/Spigot/Spigot-1.9-d20369f_7fc5cd8-20160330-1754.jar -P libs/
wget https://cdn.getbukkit.org/craftbukkit/craftbukkit-1.9.4-R0.1-SNAPSHOT-latest.jar -P libs/
wget https://mcmirror.io/files/Spigot/Spigot-1.10.2-de459a2_51263e9-20161106-0219.jar -P libs/
wget https://mcmirror.io/files/Spigot/Spigot-1.11-6f7aabf_c8ff651-20161219-1311.jar -P libs/
wget https://mcmirror.io/files/Spigot/Spigot-1.12-596221b_86aa17f-20170726-0522.jar -P libs/
wget https://mcmirror.io/files/Spigot/Spigot-1.13-fe3ab0d_f41aae4-20180815-2348.jar -P libs/
wget https://mcmirror.io/files/Spigot/Spigot-1.13.2-a1f2566-20181123-1027.jar -P libs/

mv libs/craftbukkit-1.8.8-R0.1-SNAPSHOT-latest.jar libs/craftbukkit-1.8.8-R0.1-SNAPSHOT.jar
mv libs/Spigot-1.9-d20369f_7fc5cd8-20160330-1754.jar libs/craftbukkit-1.9-R0.1-SNAPSHOT.jar
mv libs/craftbukkit-1.9.4-R0.1-SNAPSHOT-latest.jar libs/craftbukkit-1.9.4-R0.1-SNAPSHOT.jar
mv libs/Spigot-1.10.2-de459a2_51263e9-20161106-0219.jar libs/craftbukkit-1.10.2-R0.1-SNAPSHOT.jar
mv libs/Spigot-1.11-6f7aabf_c8ff651-20161219-1311.jar libs/craftbukkit-1.11.2-R0.1-SNAPSHOT.jar
mv libs/Spigot-1.12-596221b_86aa17f-20170726-0522.jar libs/craftbukkit-1.12.2-R0.1-SNAPSHOT.jar
mv libs/Spigot-1.13-fe3ab0d_f41aae4-20180815-2348.jar libs/craftbukkit-1.13-R0.1-SNAPSHOT.jar
mv libs/Spigot-1.13.2-a1f2566-20181123-1027.jar libs/craftbukkit-1.13.1-R0.1-SNAPSHOT.jar
