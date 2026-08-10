# spring-ai-starter-model-hermes

面向 `spring-ai-hermes` 模型适配器的 Spring Boot 自动配置模块。

## 兼容矩阵

| 分支 | Starter 版本 | Spring Boot | Core 适配器 | Spring AI | JDK |
|---|---|---|---|---|---|
| `feature/3.5.x` | `3.5.x.20260630-SNAPSHOT` | `3.5.5` | `spring-ai-hermes:1.0.x.20260630-SNAPSHOT` | `1.1.7` | 17 |
| `feature/4.1.x` | `4.1.x.20260630-SNAPSHOT` | `4.1.0` | `spring-ai-hermes:2.0.x.20260630-SNAPSHOT` | `2.0.0` | 17+ |

## 引入依赖

```xml
<dependency>
    <groupId>io.github.partmeai</groupId>
    <artifactId>spring-ai-starter-model-hermes</artifactId>
    <version>3.5.x.20260630-SNAPSHOT</version>
</dependency>
```

## 配置

```yaml
spring.ai.hermes:
  enabled: true
  base-url: http://localhost:8080
  api-server-key: ${HERMES_API_KEY:}
  model: default
  session-key: optional-memory-scope
  session-id: optional-session-id
```

Starter 按条件注册 `HermesApi`、`HermesChatModel` 和 `HermesModelManager`；
应用自行声明同类型 Bean 时自动配置会退让。

## 构建验证

```bash
mvn clean verify
mvn javadoc:javadoc
```
