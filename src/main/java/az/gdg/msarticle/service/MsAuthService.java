package az.gdg.msarticle.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static az.gdg.msarticle.model.client.auth.HttpHeader.X_AUTH_TOKEN;

@FeignClient(value = "ms-auth", url = "${service.ms-auth.url}")
public interface MsAuthService {
    @GetMapping("/user/get-remaining-hate-count")
    Integer getRemainingHateCount(
            @RequestHeader(X_AUTH_TOKEN) String token
    );

    @PutMapping("/user/update-remaining-hate-count")
    void updateRemainingHateCount(
            @RequestHeader(X_AUTH_TOKEN) String token
    );
}