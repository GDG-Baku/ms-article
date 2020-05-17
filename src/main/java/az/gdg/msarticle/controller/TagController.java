package az.gdg.msarticle.controller;

import az.gdg.msarticle.service.TagService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tag")
public class TagController {
    private static final Logger logger = LoggerFactory.getLogger(TagController.class);
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @ApiOperation("Add Tag")
    @PostMapping
    public void addTag(@RequestHeader("X-Auth-Token") String token,
                                    @RequestBody String tagName) {
        logger.debug("add tag by tagName {} start", tagName);
        tagService.addTag(tagName);
    }
}
