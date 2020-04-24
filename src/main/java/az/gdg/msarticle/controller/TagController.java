package az.gdg.msarticle.controller;

import az.gdg.msarticle.model.TagRequest;
import az.gdg.msarticle.service.TagService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<String> createTag(@RequestBody TagRequest tagRequest) {
        return new ResponseEntity<>(tagService.createTag(tagRequest), HttpStatus.OK);
    }
}
