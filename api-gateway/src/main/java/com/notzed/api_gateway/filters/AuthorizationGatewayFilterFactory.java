package com.notzed.api_gateway.filters;

import com.notzed.api_gateway.service.JwtService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Slf4j
public class AuthorizationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizationGatewayFilterFactory.Config> {

    private final JwtService jwtService;

    public AuthorizationGatewayFilterFactory(JwtService jwtService) {
        super(AuthorizationGatewayFilterFactory.Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            if(!config.isEnabled)return chain.filter(exchange);

            String authorizationHeader = exchange.getRequest()
                    .getHeaders().getFirst("Authorization");
            if(authorizationHeader == null || authorizationHeader.startsWith("Bearer ")){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authorizationHeader.substring(7);
            String userRoleFromToken = String.valueOf(jwtService.getUserRoleFromToken(token));
            return chain.filter(exchange);
        };
    }

    @Data
    public static class Config {
        private boolean isEnabled;
        private List<String> allowedRoles;
    }

}
