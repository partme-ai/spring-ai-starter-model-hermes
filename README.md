# spring-ai-starter-model-hermes

Spring Boot auto-configuration for the `spring-ai-hermes` model adapter.

## Compatibility

| Branch | Starter version | Spring Boot | Core adapter | Spring AI | JDK |
|---|---|---|---|---|---|
| `feature/3.5.x` | `3.5.x.20260630-SNAPSHOT` | `3.5.5` | `spring-ai-hermes:1.0.x.20260630-SNAPSHOT` | `1.1.7` | 17 |
| `feature/4.1.x` | `4.1.x.20260630-SNAPSHOT` | `4.1.0` | `spring-ai-hermes:2.0.x.20260630-SNAPSHOT` | `2.0.0` | 17+ |

## Dependency

```xml
<dependency>
    <groupId>io.github.partmeai</groupId>
    <artifactId>spring-ai-starter-model-hermes</artifactId>
    <version>4.1.x.20260630-SNAPSHOT</version>
</dependency>
```

## Configuration

```yaml
spring.ai.hermes:
  enabled: true
  base-url: http://localhost:8080
  api-server-key: ${HERMES_API_KEY:}
  model: default
  session-key: optional-memory-scope
  session-id: optional-session-id
```

The starter conditionally registers `HermesApi`, `HermesChatModel`, and
`HermesModelManager`. User-provided beans take precedence.

## Build

```bash
mvn clean verify
mvn javadoc:javadoc
```
