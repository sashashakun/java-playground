# Exercise 15 — Docker Multi-stage Build

Reference artifact showing how to containerize the course's payment API. No Java code here —
drop the `Dockerfile` and `docker-compose.yml` next to any of the Spring Boot exercises
(e.g. `13-spring-security-jwt`) and build.

## Multi-stage builds

The `Dockerfile` has two stages:

1. **Build stage** (`gradle:8.5-jdk21`) — full JDK plus Gradle, compiles the app and produces the boot jar.
2. **Runtime stage** (`eclipse-temurin:21-jre-alpine`) — slim JRE only; just the jar is copied over
   with `COPY --from=builder`.

The build tools (Gradle, JDK compiler, dependency caches) never reach the final image:
roughly **~200MB** runtime image instead of **~800MB**, and a much smaller attack surface.

## Non-root user

`RUN addgroup -S app && adduser -S app -G app` + `USER app` runs the JVM as an unprivileged
user. If the app is compromised, the attacker doesn't get root inside the container —
container escapes and file-system tampering get significantly harder.

## Layer-caching tip

`COPY build.gradle.kts settings.gradle.kts ./` comes **before** `COPY src ./src` on purpose:
Docker caches each layer, so editing only source code doesn't invalidate the layer where the
build files were copied. (For even better caching, add a `RUN gradle dependencies` step after
copying the build files so dependency downloads are cached too.)

## Build & run

```bash
# Build the image and start app + Postgres
docker compose up --build

# Or just the image:
docker build -t fintech-payments .
docker run -p 8080:8080 fintech-payments
```

The `HEALTHCHECK` polls `/actuator/health` — add `spring-boot-starter-actuator` to the app's
dependencies for it to report healthy.

## TypeScript / Node.js comparison

Exactly the same pattern in Node land:

```dockerfile
FROM node:20 AS builder        # npm ci + tsc — dev deps and compiler live here
...
FROM node:20-alpine            # runtime: only dist/ + production node_modules
COPY --from=builder /app/dist ./dist
```

Build with the full toolchain, ship a slim runtime image — the idea is identical whether the
artifact is a Spring Boot jar or a compiled TypeScript `dist/` folder.
