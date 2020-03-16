package club.example.oauth2.server.config;

import club.example.oauth2.server.config.properties.OAuth2SecurityProperties;
import club.example.oauth2.server.service.jwt.ApplicationTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableConfigurationProperties(OAuth2SecurityProperties.class)
public class OAuth2JwtTokenStoreConfig {

    private final OAuth2SecurityProperties auth2SecurityProperties;

    @Autowired
    public OAuth2JwtTokenStoreConfig(OAuth2SecurityProperties auth2SecurityProperties) {
        this.auth2SecurityProperties = auth2SecurityProperties;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // JWT 可以使用 签名来完成校验
        jwtAccessTokenConverter.setSigningKey(auth2SecurityProperties.getJwt().getSignKey());
        return jwtAccessTokenConverter;
    }

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    @ConditionalOnMissingBean(name = "jwtTokenEnhancer")
    public TokenEnhancer jwtTokenEnhancer() {
        return new ApplicationTokenEnhancer();
    }
}
