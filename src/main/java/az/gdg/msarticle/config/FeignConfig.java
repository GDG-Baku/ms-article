package az.gdg.msarticle.config;

import az.gdg.msarticle.client.AuthenticationClient;

import az.gdg.msarticle.service.MsAuthService;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {AuthenticationClient.class, MsAuthService.class})
public class FeignConfig {
}
