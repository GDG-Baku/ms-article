package az.gdg.msarticle.config;

import az.gdg.msarticle.service.MsAuthService;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {MsAuthService.class})
public class FeignConfig {
}
