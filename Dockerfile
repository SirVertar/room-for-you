FROM openjdk:11
ADD build/libs/room-for-you-V.0.2.0.jar .
EXPOSE 8080
CMD java -jar room-for-you-V.0.2.0.jar