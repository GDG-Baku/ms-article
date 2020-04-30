package az.gdg.msarticle.controller;

import az.gdg.msarticle.service.CommentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteComment(@PathVariable String id) {
        return new ResponseEntity<>(commentService.deleteComment(id), HttpStatus.OK);
    }
}
