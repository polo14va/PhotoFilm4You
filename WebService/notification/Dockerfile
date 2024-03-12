# Usar una base de imagen Java
FROM openjdk:17-jdk-slim-buster AS builder

# Copiar el archivo jar empaquetado
COPY target/notification-*.jar /notification.jar

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "/notification.jar"]
