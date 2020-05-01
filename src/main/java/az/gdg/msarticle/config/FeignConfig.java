package az.gdg.msarticle.config;

import az.gdg.msarticle.client.MsAuthClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = MsAuthClient.class)
public class FeignConfig {
}
