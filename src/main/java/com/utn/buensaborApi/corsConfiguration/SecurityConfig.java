package com.utn.buensaborApi.corsConfiguration;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/articuloInsumo/**",
                    "/api/articuloManufacturado/**",
                    "/api/categoria/**",
                    "/api/v1/factura/**",
                    "/api/imagenes/**",
                    "/api/v1/pedidoVenta/**",
                    "/api/v1/pedidoVentaDetalle/**",
                    "/api/v1/promociones/**",
                    "/api/v1/promocionesDetalle/**",
                    "/api/sucursalInsumos/**",
                    "/api/unidadmedida/**",
                    "/api/clientes/**",
                    "/api/domicilios/**",
                    "/api/empleados/**",
                    "/api/empresas/**",
                    "/api/localidades/**",
                    "/api/paises/**",
                    "/api/provincias/**",
                    "/api/sucursales/**",
                    "/api/usuarios/**"
                ).permitAll()
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("https://buen-sabor.com/roles");
            logger.info("Roles recibidos en el JWT: {}", roles);
            if (roles == null) return List.of();

            return roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());
        });

        return converter;
    }
}