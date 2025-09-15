package live.tikgik.expenses.gatewayserver.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.springframework.cloud.gateway.support.RouteMetadataUtils.CONNECT_TIMEOUT_ATTR;
import static org.springframework.cloud.gateway.support.RouteMetadataUtils.RESPONSE_TIMEOUT_ATTR;


@Configuration
public class AppConfig {

    @Bean
    public RouteLocator tikGikRoutes(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder
                .routes()
                .route(pred -> pred.path("/expenses/account/**")
                        .filters(filter ->
                                filter
                                .rewritePath("/expenses/account/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config ->
                                        config.setName("accountCircuitBreakerConfig")
                                                .setFallbackUri("forward:/contactSupport")
                                )
                        )
                        // this to over ride the default behaviour for all the http client of the gateway for a specific path
                        .metadata(RESPONSE_TIMEOUT_ATTR, Duration.ofSeconds(5))
                        .metadata(CONNECT_TIMEOUT_ATTR, 200)
                        .uri("lb://ACCOUNT"))
                .route(pred -> pred.path("/expenses/transaction/**")
                        .filters(filter ->
                                filter
                                        .rewritePath("/expenses/transaction/(?<segment>.*)","/${segment}")
                                        .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                        .retry(retryConfig ->
                                                retryConfig.setRetries(3)
                                                        .setMethods(HttpMethod.GET)
                                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2 , true)
                                        )
                        )
                        .uri("lb://TRANSACTIONS"))
                .route(pred -> pred.path("/expenses/category/**")
                        .filters(filter ->
                                filter
                                        .rewritePath("/expenses/category/(?<segment>.*)","/${segment}")
                                        .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                        .requestRateLimiter(config ->
                                                config.setRateLimiter(redisRateLimiter())
                                                        .setKeyResolver(userKeyResolver())
                                        )
                        )
                        .uri("lb://CATEGORY"))
                .build();

    }

    // this bean to customize the default time limit of the circut breaker pattern
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(1, 1, 1);
    }
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
                .defaultIfEmpty("anonymous");
    }

}
