package az.gdg.msarticle.controller;

import az.gdg.msarticle.service.ArticleService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @ApiOperation("Add Quack")
    @PutMapping("/addQuack/{articleId}")
    public void addQuackByArticleId(@RequestHeader("X-Auth-Token") String token,
                                    @PathVariable("articleId") String articleId) {
        logger.debug("add quack by articleId {} start", articleId);
        articleService.addQuackByArticleId(articleId);
    }
}
