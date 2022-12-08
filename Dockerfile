FROM eclipse-temurin:17.0.5_8-jre-alpine as builder
WORKDIR /extracted
ADD target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17.0.5_8-jre-alpine
WORKDIR /application
COPY --from=builder /extracted/dependencies/ ./
COPY --from=builder /extracted/spring-boot-loader/ ./
COPY --from=builder /extracted/snapshot-dependencies/ ./
COPY --from=builder /extracted/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]