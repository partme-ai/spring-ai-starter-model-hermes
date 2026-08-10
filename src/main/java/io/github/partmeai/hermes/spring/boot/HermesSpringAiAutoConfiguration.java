package io.github.partmeai.hermes.spring.boot;

import io.github.partmeai.hermes.HermesChatModel;
import io.github.partmeai.hermes.api.HermesApi;
import io.github.partmeai.hermes.api.HermesChatOptions;
import io.github.partmeai.hermes.api.SseErrorHandler;
import io.github.partmeai.hermes.management.HermesModelManager;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Hermes API Server 的 Spring Boot 自动配置。
 *
 * <p>当 {@code spring.ai.hermes.enabled=true}（默认值）时，按缺失 Bean 条件创建
 * {@link HermesApi}、{@link HermesChatModel} 与 {@link HermesModelManager}。应用自行
 * 声明同类型 Bean 时，本配置会自动退让。</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.5.x.20260630
 */
@AutoConfiguration(after = { RestClientAutoConfiguration.class, WebClientAutoConfiguration.class })
@ConditionalOnClass({ HermesApi.class, HermesChatModel.class })
@ConditionalOnProperty(prefix = HermesSpringAiProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(HermesSpringAiProperties.class)
@Slf4j
public class HermesSpringAiAutoConfiguration {

    /**
     * 创建共享底层 HTTP 资源的 Hermes API 客户端。
     *
     * @param properties Hermes 连接与认证配置
     * @param restClientBuilder Spring 管理的同步客户端构建器
     * @param webClientBuilder Spring 管理的响应式客户端构建器
     * @param responseErrorHandler 可选的同步响应错误处理器
     * @param sseErrorHandler 可选的 SSE 解析异常处理器
     * @return 配置完成的 Hermes API 客户端
     */
    @Bean
    @ConditionalOnMissingBean
    public HermesApi hermesApi(HermesSpringAiProperties properties,
                               RestClient.Builder restClientBuilder,
                               WebClient.Builder webClientBuilder,
                               ObjectProvider<ResponseErrorHandler> responseErrorHandler,
                               ObjectProvider<SseErrorHandler> sseErrorHandler) {
        log.info("Creating HermesApi bean connected to {}", properties.getBaseUrl());
        // Builder 必须克隆后再写入认证头，避免污染容器中供其他客户端复用的实例。
        return HermesApi.builder()
            .baseUrl(properties.getBaseUrl())
            .restClientBuilder(restClientBuilder.clone()
                .defaultHeader("Authorization", "Bearer " + properties.resolveApiKey()))
            .webClientBuilder(webClientBuilder.clone()
                .defaultHeader("Authorization", "Bearer " + properties.resolveApiKey()))
            .responseErrorHandler(responseErrorHandler.getIfAvailable(() -> RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER))
            .sseErrorHandler(sseErrorHandler.getIfAvailable(() -> SseErrorHandler.DEFAULT))
            .build();
    }

    /**
     * 创建 Spring AI Hermes 聊天模型适配器。
     *
     * @param api Hermes HTTP API 客户端
     * @param properties 默认模型与会话配置
     * @param toolCallingManager Spring AI 工具调用管理器
     * @param observationRegistry Micrometer 观测注册表
     * @return 配置完成的 Hermes 聊天模型
     */
    @Bean
    @ConditionalOnMissingBean
    public HermesChatModel hermesChatModel(HermesApi api,
                                           HermesSpringAiProperties properties,
                                           ToolCallingManager toolCallingManager,
                                           ObservationRegistry observationRegistry) {
        HermesChatOptions defaultOptions = HermesChatOptions.builder()
            .model(properties.getModel())
            .hermesSessionKey(properties.getSessionKey())
            .hermesSessionId(properties.getSessionId())
            .build();
        log.info("Creating HermesChatModel bean (model={})", defaultOptions.getModel());
        return HermesChatModel.builder()
            .api(api)
            .defaultOptions(defaultOptions)
            .toolCallingManager(toolCallingManager)
            .observationRegistry(observationRegistry)
            .build();
    }

    /**
     * 创建 Hermes 模型发现管理器。
     *
     * @param api Hermes HTTP API 客户端
     * @return 使用默认缓存时长的模型管理器
     */
    @Bean
    @ConditionalOnMissingBean
    public HermesModelManager hermesModelManager(HermesApi api) {
        return new HermesModelManager(api);
    }
}
