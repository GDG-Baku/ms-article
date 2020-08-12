package az.gdg.msarticle.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "ms-team-client", url = "https://gdg-ms-team.herokuapp.com/api/members")
public interface TeamClient {
    @GetMapping("/emails")
    List<String> getAllMails();
}
