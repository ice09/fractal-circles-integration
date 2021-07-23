package tech.blockchainers.circles.rest;

import com.jayway.jsonpath.JsonPath;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping
public class AuthorizationCode {

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.redirect-url}")
    private String redirectUrl;

    @Value("${keycloak.token-url}")
    private String keycloakTokenUrl;

    @GetMapping("/auth")
    public ResponseEntity<String> authorize(@RequestParam String code) {
        log.info("code={}", code);
        HttpResponse<String> jwt =
            Unirest.post(keycloakTokenUrl)
                .header("content-type", "application/x-www-form-urlencoded")
                .body("grant_type=authorization_code&code=" + code + "&client_id=verification&client_secret=" + clientSecret + "&redirect_uri=" + redirectUrl)
                .asString();
        String accessToken = JsonPath.parse(jwt.getBody()).read("access_token").toString();
        return ResponseEntity.ok(accessToken);
    }

    @GetMapping("/users/me")
    public ResponseEntity<String> userInfoRetrieval(@RequestHeader("Authorization") String bearerToken) {
        log.info(bearerToken);
        String accessToken = bearerToken.substring(7);
        String accessTokenBody = accessToken.substring(accessToken.indexOf(".")+1, accessToken.lastIndexOf("."));
        String base64encoded = new String(Base64.getDecoder().decode(accessTokenBody.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        String wallet_address = JsonPath.parse(base64encoded).read("wallet-address").toString();

        return ResponseEntity.ok("wallet-address:" + wallet_address);
    }
}
