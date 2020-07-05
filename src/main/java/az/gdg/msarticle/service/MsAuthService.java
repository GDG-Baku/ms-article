package az.gdg.msarticle.service;

import az.gdg.msarticle.model.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "ms-auth", url = "${client.service.url.ms-auth}")
public interface MsAuthService {
    @GetMapping("/user/{userId}")
    UserDTO getUserById(
            @PathVariable("userId") Long userId
    );
}