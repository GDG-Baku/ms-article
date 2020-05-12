package az.gdg.msarticle.controller;

import az.gdg.msarticle.model.dto.ArticleDTO;
import az.gdg.msarticle.service.ArticleService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api("Article Controller")
@RequestMapping("/article")
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@RequiredArgsConstructor
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    private final ArticleService articleService;

    @GetMapping(path = "/{type}/{page}/{size}")
    public List<ArticleDTO> getAllPostsByType(@PathVariable("type") String type,
                                              @PathVariable("page") Integer page,
                                              @PathVariable("size") Integer size) {

        logger.debug("getAllPostsByType start");
        return articleService.getAllPostsByType(type, page, size);
    }

}
