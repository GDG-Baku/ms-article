package az.gdg.msarticle.controller;

import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);


    @ApiOperation(value = "Method will be called by ms-alarm")
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @GetMapping
    public void health() {
        logger.info("ActionLog.ms-article.start");
    }

}