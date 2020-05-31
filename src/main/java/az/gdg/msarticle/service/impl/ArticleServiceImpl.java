package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.NoSuchArticleException;
import az.gdg.msarticle.exception.NotValidTokenException;
import az.gdg.msarticle.exception.UnauthorizedAccessException;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.model.entity.CommentEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.repository.CommentRepository;
import az.gdg.msarticle.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;


    public ArticleServiceImpl(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }


    @Override
    public void deleteArticleById(String articleID) {
        logger.info("ActionLog.deleteArticleById.start");
        Integer userId = Integer.parseInt((String) getAuthenticatedObject().getPrincipal());
        ArticleEntity articleEntity = articleRepository.findById(articleID)
                .orElseThrow(() -> new NoSuchArticleException("Article doesn't exist"));
        Integer articleUserId = articleEntity.getUserId();
        if (articleUserId.equals(userId)) {
            deleteAllComments(articleEntity.getComments());
            articleRepository.deleteById(articleID);
        } else {
            logger.info("Thrown.UnauthorizedAccessException");
            throw new UnauthorizedAccessException("You don't have permission to delete the article");
        }
    }

    private void deleteAllComments(List<CommentEntity> commentEntityList) {
        for (CommentEntity commentEntity : commentEntityList) {
            if (commentEntity.getReplies() != null) {
                commentRepository.deleteAll(commentEntity.getReplies());
            }
        }
        commentRepository.deleteAll(commentEntityList);
    }

    private Authentication getAuthenticatedObject() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("Thrown.NotValidTokenException");
            throw new NotValidTokenException("Token is not valid or it is expired");
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
