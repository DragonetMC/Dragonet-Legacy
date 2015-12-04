FROM frolvlad/alpine-oraclejdk8:slim

COPY target/*.jar /dragonet/

RUN adduser -h /dragonet -H -D dragonet && \
    mkdir -p /dragonet/data && \
    chown -R dragonet:dragonet /dragonet

# Minecraft PC/Mac (TCP)
EXPOSE 25565

# Minecraft Pocket Edition (UDP)
EXPOSE 19132

# Data volume
VOLUME /dragonet/data

USER dragonet
WORKDIR /dragonet/data

ENTRYPOINT /usr/bin/java -jar $(find .. -name 'dragonet*.jar' | head -n 1)
