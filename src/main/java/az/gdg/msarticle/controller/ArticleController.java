package az.gdg.msarticle.controller;

import az.gdg.msarticle.model.dto.ArticleDTO;
import az.gdg.msarticle.service.ArticleService;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articles")
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

    @ApiOperation(value = "Publish article method")
    @PutMapping("/{articleId}")
    public ResponseEntity<String> publishArticle(@RequestHeader("X-Auth-Token") String token,
                                                 @PathVariable String articleId) {
        logger.debug("Publish article with article id {} start", articleId);
        return new ResponseEntity<>(articleService.publishArticle(articleId), HttpStatus.OK);
    @GetMapping("/articles/{userId}/{page}")
    public UserArticleDTO getArticlesByUserId(
            @RequestHeader(value = "X-Auth-Token", required = false) String token,
            @PathVariable("userId") int userId,
            @PathVariable("page") int page) {
        logger.debug("Get articles by userId {} start", userId);
        return articleService.getArticlesByUserId(userId, page);
    }
}
