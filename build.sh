#!bin/sh
cp -fr ./GlowstonePlusPlus/src/* ./src/
mvn clean package
rm -fr ./src/main/java/net/glowstone
