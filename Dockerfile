#
# Custom base image for jib with systemd installed
#
# $ docker buildx build --push --platform linux/arm/v7,linux/arm64/v8,linux/amd64 --tag krillsson/openjdk11-debian-systemd:latest .
FROM azul/zulu-openjdk-debian:11-jre

RUN apt-get -qq update && apt-get -qq -y install systemd