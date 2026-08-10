package io.github.partmeai.hermes.spring.boot;

import io.github.partmeai.hermes.api.common.HermesApiConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Hermes Spring AI 自动配置属性。
 *
 * <p>绑定 {@code spring.ai.hermes.*}，统一描述 API Server 地址、认证信息、
 * 默认模型以及 Hermes 会话作用域。</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.5.x.20260630
 */
@Data
@ConfigurationProperties(prefix = HermesSpringAiProperties.PREFIX)
public class HermesSpringAiProperties {

    /** Hermes 配置前缀。 */
    public static final String PREFIX = "spring.ai.hermes";

    /** 是否启用 Hermes 自动配置。 */
    private boolean enabled = true;

    /** Hermes API Server 基础地址。 */
    private String baseUrl = HermesApiConstants.DEFAULT_BASE_URL;

    /** Hermes API Server Bearer Token；默认使用空字符串。 */
    private String apiServerKey = "";

    /** 默认请求模型标识；实际大模型由服务端配置。 */
    private String model = HermesApiConstants.DEFAULT_MODEL;

    /** 稳定的通道级长期记忆作用域键。 */
    private String sessionKey;

    /** 会话记录级标识。 */
    private String sessionId;

    /**
     * 解析发送给 Hermes API Server 的 Bearer Token。
     *
     * @return 已配置的 API Key；配置值为 {@code null} 时返回空字符串
     */
    public String resolveApiKey() {
        return apiServerKey != null ? apiServerKey : "";
    }
}
