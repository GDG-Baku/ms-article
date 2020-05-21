package az.gdg.msarticle.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "ms-auth-client", url = "${service.url.ms-auth}")
public interface MsAuthClient {
    @PutMapping("/user/popularity/{userId}")
    void addPopularity(@PathVariable("userId") Integer userId);
}
