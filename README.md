# Spring AI Hermes Starter

Spring Boot Starter for [Hermes Gateway](https://github.com/nousresearch/hermes-agent) integration with Spring AI.

## Features

- **Chat Completions API** - OpenAI-compatible `/v1/chat/completions` endpoint
- **Responses API** - Enhanced responses via `/v1/responses`
- **Session Management** - `X-Hermes-Session-Key` for stable memory scoping
- **Session Tracking** - `X-Hermes-Session-Id` for transcript-scoped sessions
- **Tool Calling** - Function calling support
- **Thinking Control** - Configure model thinking behavior

## Dependencies

```xml
<dependency>
    <groupId>io.github.partmeai</groupId>
    <artifactId>spring-ai-starter-model-hermes</artifactId>
    <version>3.5.x.20260623-SNAPSHOT</version>
</dependency>
```

## Configuration

```yaml
spring:
  ai:
    hermes:
      enabled: true
      base-url: http://localhost:8000
      api-key: your-api-key
      model: hermes-agent
      session-key: channel-123
      chat:
        temperature: 0.7
        max-tokens: 2048
```

### Configuration Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `spring.ai.hermes.enabled` | boolean | `true` | Enable/disable auto-configuration |
| `spring.ai.hermes.base-url` | String | `http://localhost:8000` | Hermes Gateway URL |
| `spring.ai.hermes.api-key` | String | - | Bearer token for auth |
| `spring.ai.hermes.model` | String | `hermes-agent` | Model identifier |
| `spring.ai.hermes.session-key` | String | - | Stable memory scoping key (max 256 chars) |
| `spring.ai.hermes.session-id` | String | - | Transcript-scoped session ID |
| `spring.ai.hermes.chat.temperature` | Double | - | Sampling temperature |
| `spring.ai.hermes.chat.max-tokens` | Integer | - | Max tokens |
| `spring.ai.hermes.chat.top-p` | Double | - | Nucleus sampling probability |
| `spring.ai.hermes.chat.frequency-penalty` | Double | - | Frequency penalty |
| `spring.ai.hermes.chat.presence-penalty` | Double | - | Presence penalty |
| `spring.ai.hermes.chat.stop` | List<String> | - | Stop sequences |

## Usage

### ChatModel

```java
@Autowired
private ChatModel chatModel;

public String chat(String message) {
    ChatResponse response = chatModel.call(new UserMessage(message));
    return response.getResult().getOutput().getText();
}
```

### Session-based Chat

Configure session key for stable memory scoping:

```yaml
spring:
  ai:
    hermes:
      session-key: user-123-channel-abc
```

### Tool Calling

```java
ChatOptions options = HermesChatOptions.builder()
    .tools(myToolCallback)
    .build();

ChatResponse response = chatModel.call(new Prompt(prompt, options));
```

## License

Apache License 2.0
