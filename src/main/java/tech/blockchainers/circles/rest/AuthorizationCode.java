package tech.blockchainers.circles.rest;

import com.jayway.jsonpath.JsonPath;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.blockchainers.circles.rest.dto.VerificationDTO;
import tech.blockchainers.circles.storage.IStorage;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
@RequestMapping
public class AuthorizationCode {

    @Value("${oauth2.client-secret}")
    private String clientSecret;

    @Value("${oauth2.client-id}")
    private String clientId;

    @Value("${oauth2.token-url}")
    private String tokenUrl;

    @Value("${oauth2.frontend-url}")
    private String frontendUrl;

    @Value("${oauth2.resource-url}")
    private String resourceUrl;

    @Value("${oauth2.redirect-url:https://3b17-2003-ce-7f14-3f9c-5dc6-bcc5-aafb-16e5.ngrok.io/auth}")
    private String redirectUrl;

    private final IStorage storage;

    public AuthorizationCode(IStorage storage) {
        this.storage = storage;
    }

    /*
    https://next.fractal.id/authorize?client_id=mclp5nB-iLXT2DJhP_Km1pL_JaZveFVw8qg44JQPc0c&redirect_uri=https://3b17-2003-ce-7f14-3f9c-5dc6-bcc5-aafb-16e5.ngrok.io/auth&response_type=code&verification.basic%3Aread%20verification.basic.details%3Aread%20verification.liveness%3Aread%20verification.liveness.details%3Aread&state=0x00000000000000000001
     */

    @GetMapping("/createRedirect")
    public ResponseEntity<String> createRedirect() {
        String randomState = RandomStringUtils.random(10, true, true);
        String randomHexWallet = new BigInteger(1, createRandomWallet()).toString(16);
        storage.storeWalletAddressForStateId(randomState, randomHexWallet);
        String redirect = frontendUrl + "/authorize?client_id=" + clientId + "&redirect_uri=" + redirectUrl +
                "&response_type=code&verification.basic%3Aread%20verification.basic.details%3Aread%20verification.liveness%3Aread%20verification.liveness.details%3Aread&state=" +
                randomState;
        return ResponseEntity.ok(redirect);
    }

    private byte[] createRandomWallet() {
        Random random = ThreadLocalRandom.current();
        byte[] r = new byte[20];
        random.nextBytes(r);
        return r;
    }

    @GetMapping("/auth")
    public ResponseEntity<String> authorize(@RequestParam String code, @RequestParam String state) {
        log.info("code={}", code);
        HttpResponse<String> jwt =
                Unirest.post(tokenUrl + "?grant_type=authorization_code&code=" + code + "&client_id=" + clientId + "&client_secret=" + clientSecret + "&redirect_uri=" + redirectUrl)
                        .asString();

        String accessToken = JsonPath.parse(jwt.getBody()).read("access_token").toString();

        HttpResponse<String> uDetails =
                Unirest.get(resourceUrl + "/users/me")
                        .header("Authorization", "Bearer " + accessToken).asString();
        String uid = JsonPath.parse(uDetails.getBody()).read("uid").toString();
        storage.storeUserIdForStateId(state, uid);
        return ResponseEntity.ok(uid);
    }

    @PostMapping("/callback")
    public ResponseEntity<String> callback(@RequestBody VerificationDTO verificationDTO) {
        String userId = verificationDTO.getData().getUser_id();
        log.info("uid={}", userId);
        return ResponseEntity.ok(storage.readWalletAddressForUID(userId));
    }

}
