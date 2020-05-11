package az.gdg.msarticle.controller;

import az.gdg.msarticle.model.dto.UserArticleDTO;
import az.gdg.msarticle.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/articles/{userId}/{page}")
    public UserArticleDTO getArticlesByUserId(
            @RequestHeader(value = "X-Auth-Token", required = false) String token,
            @PathVariable("userId") int userId,
            @PathVariable("page") int page) {
        logger.debug("Get articles by userId {} start", userId);
        return articleService.getArticlesByUserId(userId, page);
    }
}
