package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.NoSuchArticleException;
import az.gdg.msarticle.exception.NotValidTokenException;
import az.gdg.msarticle.exception.UnauthorizedAccessException;
import az.gdg.msarticle.mapper.ArticleMapper;
import az.gdg.msarticle.mapper.CommentMapper;
import az.gdg.msarticle.model.dto.ArticleDTO;
import az.gdg.msarticle.model.dto.UserDTO;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.service.ArticleService;
import az.gdg.msarticle.service.MsAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final MsAuthService msAuthService;
    private final CommentMapper commentMapper;


    public ArticleServiceImpl(ArticleRepository articleRepository, MsAuthService msAuthService,
                              CommentMapper commentMapper) {
        this.articleRepository = articleRepository;
        this.msAuthService = msAuthService;
        this.commentMapper = commentMapper;
    }


    @Override
    public ArticleDTO getArticleById(String articleId) {
        logger.info("ActionLog.getArticleById.start with id {}", articleId);
        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoSuchArticleException("Article doesn't exist"));
        Integer userId = articleEntity.getUserId();
        if (articleEntity.isDraft()) {
            try {
                if (Integer.parseInt(getAuthenticatedObject().getPrincipal().toString()) != userId) {
                    logger.error("Thrown.UnauthorizedAccessException");
                    throw new UnauthorizedAccessException("You don't have permission to get the article");
                }
            } catch (NotValidTokenException e) {
                logger.error("Thrown.UnauthorizedAccessException");
                throw new UnauthorizedAccessException("You don't have permission to get the article");
            }
        }
        UserDTO userDTO = msAuthService.getUserById(userId);
        ArticleDTO articleDTO = ArticleMapper.INSTANCE.entityToDto(articleEntity, userDTO);
        articleDTO.setComments(commentMapper.mapEntityListToDtoList(articleEntity.getComments()));

        logger.info("ActionLog.getArticleById.end with id {}", articleId);
        return articleDTO;
    }


    private Authentication getAuthenticatedObject() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("Thrown.NotValidTokenException");
            throw new NotValidTokenException("Token is not valid or it is expired");
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }


}
