package com.github.dearmann.apigateway.config;

import com.github.dearmann.apigateway.utility.RealmRoleConverter;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity.csrf().disable()
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.DELETE, "/*/by-userid/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/*/by-matchid/**").hasRole("ADMIN")
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers(HttpMethod.GET).permitAll()
                        .pathMatchers("/bets/**").authenticated()
                        .pathMatchers("/comments/**").authenticated()
                        .pathMatchers("/ratings/**").authenticated()
                        .anyExchange().hasRole("ADMIN"))
                .oauth2ResourceServer().jwt(jwt -> jwt.jwtAuthenticationConverter(jwtRealmRoleConverter()));
        return serverHttpSecurity.build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @RefreshScope
    public CorsWebFilter corsWebFilter(CorsConfigurationSource corsConfigurationSource) {
        return new CorsWebFilter(corsConfigurationSource);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(GlobalCorsProperties globalCorsProperties) {
        var source = new UrlBasedCorsConfigurationSource();
        globalCorsProperties.getCorsConfigurations().forEach(source::registerCorsConfiguration);
        return source;
    }

    private ReactiveJwtAuthenticationConverterAdapter jwtRealmRoleConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new RealmRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
