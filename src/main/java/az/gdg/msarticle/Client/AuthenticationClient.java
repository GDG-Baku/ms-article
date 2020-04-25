package az.gdg.msarticle.Client;

import az.gdg.msarticle.model.Client.Auth.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static az.gdg.msarticle.model.Client.Auth.HttpHeader.X_AUTH_TOKEN;

@FeignClient(name = "ms-auth-client", url = "${service.ms-auth.url}")
public interface AuthenticationClient {
    @PostMapping("/auth/validate")
    UserInfo validateToken(@RequestHeader(X_AUTH_TOKEN) String token);
}