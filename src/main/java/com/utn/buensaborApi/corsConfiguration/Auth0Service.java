
package com.utn.buensaborApi.corsConfiguration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import org.json.JSONObject;

import java.util.Map;

@Slf4j
@Service
public class Auth0Service {

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.clientId}")
    private String clientId;

    @Value("${auth0.clientSecret}")
    private String clientSecret;

    @Value("${auth0.audience}")
    private String audience;

    private final WebClient webClient = WebClient.create();

    public String obtenerToken() {
        return webClient.post()
                .uri(domain + "/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "grant_type", "client_credentials",
                        "client_id", clientId,
                        "client_secret", clientSecret,
                        "audience", audience
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .block()
                .get("access_token").toString();
    }

    public String crearUsuario(String email, String nombre) {
        String token = obtenerToken();

        JSONObject body = new JSONObject();
        body.put("email", email);
        body.put("user_metadata", new JSONObject().put("nombre", nombre));
        body.put("connection", "Username-Password-Authentication");
        body.put("email_verified", false);
        body.put("verify_email", true);
        body.put("password", "Empleado123@"); 

        Map response = webClient.post()
                .uri(domain + "/api/v2/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body.toString())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return response.get("user_id").toString(); // auth0id
    }

    public void asignarRol(String userId, String rolId) {
        String token = obtenerToken();
        webClient.post()
                .uri(domain + "/api/v2/roles/" + rolId + "/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("users", new String[]{userId}))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
