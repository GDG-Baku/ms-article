package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.InvalidTokenException;
import az.gdg.msarticle.exception.NoSuchArticleException;
import az.gdg.msarticle.exception.UnauthorizedAccessException;
import az.gdg.msarticle.mapper.ArticleMapper;
import az.gdg.msarticle.mapper.custom.CustomCommentMapper;
import az.gdg.msarticle.model.dto.ArticleDTO;
import az.gdg.msarticle.model.dto.CommentDTO;
import az.gdg.msarticle.model.dto.UserDTO;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.model.entity.CommentEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.repository.CommentRepository;
import az.gdg.msarticle.service.ArticleService;
import az.gdg.msarticle.service.MsAuthService;
import az.gdg.msarticle.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final MsAuthService msAuthService;
    private final CommentRepository commentRepository;
    private final CustomCommentMapper customCommentMapper;


    public ArticleServiceImpl(ArticleRepository articleRepository, MsAuthService msAuthService,
                              CommentRepository commentRepository) {
                              CustomCommentMapper customCommentMapper) {
        this.articleRepository = articleRepository;
        this.msAuthService = msAuthService;
        this.commentRepository = commentRepository;
        this.customCommentMapper = customCommentMapper;
    }


    @Override
    public void deleteArticleById(String articleID) {
        logger.info("ActionLog.deleteArticleById.start");
        Long userId = Long.parseLong((String) AuthUtil.getAuthenticatedObject().getPrincipal());
        ArticleEntity articleEntity = articleRepository.findById(articleID)
                .orElseThrow(() -> new NoSuchArticleException("Article doesn't exist"));
        Long articleUserId = articleEntity.getUserId();
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

    public ArticleDTO getArticleById(String articleId) {
        logger.info("ActionLog.getArticleById.start with id {}", articleId);
        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoSuchArticleException("Article doesn't exist"));
        Long userId = articleEntity.getUserId();
        if (articleEntity.isDraft()) {
            try {
                if (Integer.parseInt(AuthUtil.getAuthenticatedObject().getPrincipal().toString()) != userId) {
                    logger.error("Thrown.UnauthorizedAccessException");
                    throw new UnauthorizedAccessException("You don't have permission to get the article");
                }
            } catch (InvalidTokenException e) {
                logger.error("Thrown.UnauthorizedAccessException");
                throw new UnauthorizedAccessException("You don't have permission to get the article");
            }
        }
        UserDTO userDTO = msAuthService.getUserById(userId);
        ArticleDTO articleDTO = ArticleMapper.INSTANCE.entityToDto(articleEntity, userDTO);
        articleDTO.setComments(getCommentDTOsWithUserDTO(articleEntity.getComments()));
        articleDTO.setComments(customCommentMapper.mapEntityListToDtoList(articleEntity.getComments()));

        logger.info("ActionLog.getArticleById.end with id {}", articleId);
        return articleDTO;
    }


    private List<CommentDTO> getCommentDTOsWithUserDTO(List<CommentEntity> commentEntities) {
        List<CommentDTO> commentDTOs = CommentMapper.INSTANCE.entityToDtoList(commentEntities);
        for (CommentDTO commentDTO : commentDTOs) {
            UserDTO userDTO = msAuthService.getUserById(commentDTO.getUserId());
            commentDTO.setUserDTO(userDTO);
        }
        return commentDTOs;
    }

}
