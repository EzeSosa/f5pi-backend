FROM eclipse-temurin:17-jdk-jammy AS deps

WORKDIR /build

COPY --chmod=0755 gradlew gradlew
COPY gradle/ gradle/

RUN --mount=type=bind,source=build.gradle.kts,target=build.gradle.kts \
    --mount=type=bind,source=settings.gradle.kts,target=settings.gradle.kts \
    --mount=type=cache,target=/root/.gradle ./gradlew dependencies --no-daemon

FROM deps AS package

WORKDIR /build

COPY ./src src/
RUN --mount=type=bind,source=build.gradle.kts,target=build.gradle.kts \
    --mount=type=bind,source=settings.gradle.kts,target=settings.gradle.kts \
    --mount=type=cache,target=/root/.gradle \
    ./gradlew bootJar --no-daemon && \
    mv build/libs/*.jar build/libs/app.jar

FROM package AS extract

WORKDIR /build

RUN java -Djarmode=layertools -jar build/libs/app.jar extract --destination build/extracted

FROM eclipse-temurin:17-jre-jammy AS final

ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser

COPY --from=extract build/build/extracted/dependencies/ ./
COPY --from=extract build/build/extracted/spring-boot-loader/ ./
COPY --from=extract build/build/extracted/snapshot-dependencies/ ./
COPY --from=extract build/build/extracted/application/ ./

EXPOSE 8080

ENTRYPOINT [ "java", "org.springframework.boot.loader.launch.JarLauncher" ]