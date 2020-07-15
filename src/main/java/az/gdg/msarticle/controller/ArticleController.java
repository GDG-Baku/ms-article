package az.gdg.msarticle.controller;

import az.gdg.msarticle.model.dto.ArticleDTO;
import az.gdg.msarticle.service.ArticleService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/article")
@RestController
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
public class ArticleController {
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/{articleId}")
    public ArticleDTO getArticleById(@RequestHeader(value = "X-Auth-Token", required = false) String token,
                                     @PathVariable("articleId") String articleId) {
        logger.debug("Get article by id {} start", articleId);
        return articleService.getArticleById(articleId);
    }

    @ApiOperation("delete article")
    @DeleteMapping("/{articleId}")
    public void deleteArticleById(@RequestHeader(value = "X-Auth-Token") String token,
                                  @PathVariable("articleId") String articleId) {
        logger.debug("delete article by articleId {} start", articleId);
        articleService.deleteArticleById(articleId);
    }

    @ApiOperation("add read count for article which is defined by id")
    @PostMapping(value = "/read-count")
    public void addReadCount(@RequestBody String articleId) {
        logger.debug("Add read count start : articleId {}", articleId);
        articleService.addReadCount(articleId);
    }
}
