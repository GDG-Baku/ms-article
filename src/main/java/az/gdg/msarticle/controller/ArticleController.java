package az.gdg.msarticle.controller;

import az.gdg.msarticle.model.ArticleRequest;
import az.gdg.msarticle.service.ArticleService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PutMapping("{articleId}")
    public ResponseEntity<String> updateArticle(@PathVariable String articleId,
                                                @RequestBody ArticleRequest articleRequest) {
        return new ResponseEntity<>(articleService.updateArticle(articleId, articleRequest), HttpStatus.OK);
    }
}
