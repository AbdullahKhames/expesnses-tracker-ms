package live.tikgik.expenses.gatewayserver.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static live.tikgik.expenses.shared.constant.AppConstants.*;

@Component
public class JwtClaimsToHeaderFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> ctx.getAuthentication())
            .cast(JwtAuthenticationToken.class)
            .flatMap(auth -> {
                Map<String, Object> realmAccess = auth.getToken().getClaim("realm_access");
                String roles = "";

                if (realmAccess != null && realmAccess.containsKey("roles")) {
                    roles = ((List<String>) realmAccess.get("roles"))
                            .stream()
                            .collect(Collectors.joining(","));
                }
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header(X_USER_ID.getKey(), auth.getToken().getClaimAsString("sub"))
                        .header(X_USER_NAME.getKey(), auth.getToken().getClaimAsString("preferred_username"))
                        .header(X_EMAIL.getKey(), auth.getToken().getClaimAsString("email"))
                        .header(X_ROLES.getKey(), roles)
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            })
            .switchIfEmpty(chain.filter(exchange)); // in case no auth
    }

    @Override
    public int getOrder() {
        return -1; // run before routing
    }
}
