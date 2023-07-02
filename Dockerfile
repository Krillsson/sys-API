#
# Custom base image for jib with systemd installed
#
FROM azul/zulu-openjdk-debian:11-jre

RUN apt-get -qq update && apt-get -qq -y install systemd