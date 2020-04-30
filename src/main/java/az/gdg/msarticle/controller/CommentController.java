package az.gdg.msarticle.controller;

import az.gdg.msarticle.model.CommentRequest;
import az.gdg.msarticle.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("Comment Controller")
@RequestMapping("/comment")
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@RequiredArgsConstructor
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;

    @PostMapping("/post")
    @ApiOperation("post comment to article")
    public void post(@RequestBody CommentRequest commentRequest,
                     @RequestHeader("X-Auth-Token") String token) {

        logger.debug("post start : token {}", token);
        commentService.post(token, commentRequest);
        logger.debug("post stop.success");
    }
}
