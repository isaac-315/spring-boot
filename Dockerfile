# Etapa 1: Construcción (Build)
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . .
# Ejecutamos el wrapper de gradle para compilar y empaquetar
RUN ./gradlew clean build -x test

# Etapa 2: Ejecución (Runtime)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copiamos solo el jar generado de la etapa anterior
COPY --from=build /app/build/libs/*.jar app.jar

# Exponemos el puerto de tu aplicación (ajusta si usas otro)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]