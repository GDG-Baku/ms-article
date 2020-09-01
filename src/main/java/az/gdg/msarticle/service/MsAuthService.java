package az.gdg.msarticle.service;

import az.gdg.msarticle.model.client.auth.UserInfo;
import az.gdg.msarticle.model.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static az.gdg.msarticle.model.client.auth.HttpHeader.X_AUTH_TOKEN;

@FeignClient(value = "ms-auth", url = "${client.service.url.ms-auth}")
public interface MsAuthService {
    @GetMapping("/user/{userId}")
    UserDTO getUserById(
            @PathVariable("userId") Long userId
    );

    @GetMapping("/user/info")
    UserInfo getUserInfo(@RequestHeader(X_AUTH_TOKEN) String token);

    @PutMapping("/user/popularity/{userId}")
    void addPopularity(@PathVariable("userId") Long userId);

    @GetMapping("/user/get-remaining-quack-count")
    Integer getRemainingQuackCount(
            @RequestHeader(X_AUTH_TOKEN) String token
    );

    @PutMapping("/user/update-remaining-quack-count")
    void updateRemainingQuackCount(
            @RequestHeader(X_AUTH_TOKEN) String token
    );
}