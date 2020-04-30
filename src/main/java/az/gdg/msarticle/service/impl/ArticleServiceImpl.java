package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.NoAccessException;
import az.gdg.msarticle.exception.NoSuchArticleException;
import az.gdg.msarticle.mapper.ArticleMapper;
import az.gdg.msarticle.mapper.CommentMapper;
import az.gdg.msarticle.model.dto.ArticleDTO;
import az.gdg.msarticle.model.dto.UserArticleDTO;
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

import java.util.Collections;

@Service
public class ArticleServiceImpl implements ArticleService{
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
    public UserArticleDTO getArticlesByUserId(String articleId) {
        logger.info("ActionLog.getArticlesByUserId.start with id {}", articleId);
        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoSuchArticleException("Article doesn't exist"));
        int userId = articleEntity.getUserId();

        if(articleEntity.isDraft() && !(getAuthenticatedObject() != null &&
                Integer.parseInt(getAuthenticatedObject().getPrincipal().toString()) == userId) ) {
            logger.info("Thrown.NoAccessException");
            throw new NoAccessException("You don't have permission to get the article");
        }
        ArticleDTO articleDTO = ArticleMapper.INSTANCE.entityToDto(articleEntity);
        articleDTO.setComments(commentMapper.mapEntityListToDtoList(articleEntity.getComments()));
        UserDTO userDTO = msAuthService.getUserById(userId);
        logger.info("ActionLog.getArticlesByUserId.end with id {}", articleId);
        return UserArticleDTO.builder()
                .articleDTOs(Collections.singletonList(articleDTO))
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .imageUrl(userDTO.getImageUrl())
                .build();
    }


    private Authentication getAuthenticatedObject() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
