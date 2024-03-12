# Usar una base de imagen Java
FROM openjdk:17-jdk-slim-buster AS builder

# Copiar el archivo jar empaquetado
COPY target/productcatalog-*.jar /productcatalog.jar

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "/productcatalog.jar"]
