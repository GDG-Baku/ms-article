package az.gdg.msarticle.client;

import az.gdg.msarticle.model.client.auth.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static az.gdg.msarticle.model.client.auth.HttpHeader.X_AUTH_TOKEN;

@FeignClient(value = "ms-auth-client", url = "https://gdg-ms-auth.herokuapp.com/")
public interface AuthenticationClient {
    @GetMapping("/user/info")
    UserInfo getUserInfo(@RequestHeader(X_AUTH_TOKEN) String token);
}
