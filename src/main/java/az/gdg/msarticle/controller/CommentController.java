package az.gdg.msarticle.controller;

import az.gdg.msarticle.model.CommentRequest;
import az.gdg.msarticle.service.CommentService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/comments")
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation(value = "Deleting comment by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@RequestHeader("X-Auth-Token") String token,
                                                @PathVariable String id) {
        logger.debug("Delete comment with id {} start", id);
        return new ResponseEntity<>(commentService.deleteComment(id), HttpStatus.OK);

    }

    @PostMapping
    @ApiOperation("post comment to article")
    public void postComment(@RequestBody CommentRequest commentRequest,
                            @RequestHeader("X-Auth-Token") String token) {

        logger.debug("post start : articleId {}", commentRequest.getArticleId());
        commentService.postComment(token, commentRequest);
        logger.debug("post stop.success : articleId {}", commentRequest.getArticleId());
    }
}
