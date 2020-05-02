package az.gdg.msarticle.controller;

import az.gdg.msarticle.service.ArticleService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @ApiOperation("Get Quacks")
    @GetMapping("/getQuacks/{articleId}")
    public Integer getQuacksByArticleId(@PathVariable("articleId") String articleId) {
        logger.debug("get quacks by articleId {} start", articleId);
        return articleService.getQuacksByArticleId(articleId);
    }
}
