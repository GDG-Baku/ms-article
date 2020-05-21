package az.gdg.msarticle.client;

import az.gdg.msarticle.client.dto.UserDetail;
import az.gdg.msarticle.model.client.auth.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

import static az.gdg.msarticle.model.client.auth.HttpHeader.X_AUTH_TOKEN;

@FeignClient(value = "ms-auth-client", url = "${service.url.ms-auth}")
public interface AuthenticationClient {
    @GetMapping("/user/info")
    UserInfo getUserInfo(@RequestHeader(X_AUTH_TOKEN) String token);

    @PostMapping("/user/get-users")
    List<UserDetail> getUsersById(@RequestBody List<Integer> userIds);
}
