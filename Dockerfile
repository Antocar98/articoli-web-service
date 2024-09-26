# Utilizza l'immagine ufficiale OpenJDK come base
FROM openjdk:17-jdk-alpine

# Imposta la working directory all'interno del container
WORKDIR /app

# Copia il file JAR generato nella working directory del container
COPY target/*.jar app.jar

# Comando di avvio dell'applicazione
ENTRYPOINT ["java","-jar","/app/app.jar"]
