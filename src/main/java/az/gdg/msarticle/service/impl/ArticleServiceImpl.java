package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.client.TeamClient;
import az.gdg.msarticle.exception.AlreadyPublishedArticleException;
import az.gdg.msarticle.exception.ArticleNotFoundException;
import az.gdg.msarticle.exception.InvalidTokenException;
import az.gdg.msarticle.exception.MembersNotFoundException;
import az.gdg.msarticle.exception.NoSuchArticleException;
import az.gdg.msarticle.exception.UnauthorizedAccessException;
import az.gdg.msarticle.mapper.ArticleMapper;
import az.gdg.msarticle.mapper.CommentMapper;
import az.gdg.msarticle.model.ArticleRequest;
import az.gdg.msarticle.model.dto.ArticleDTO;
import az.gdg.msarticle.model.dto.CommentDTO;
import az.gdg.msarticle.model.dto.UserArticleDTO;
import az.gdg.msarticle.model.dto.UserDTO;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.model.entity.CommentEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.repository.CommentRepository;
import az.gdg.msarticle.service.ArticleService;
import az.gdg.msarticle.service.MailService;
import az.gdg.msarticle.service.MsAuthService;
import az.gdg.msarticle.service.TagService;
import az.gdg.msarticle.util.AuthUtil;
import az.gdg.msarticle.util.MailUtil;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final MsAuthService msAuthService;
    private final CommentRepository commentRepository;
    private final MailService mailService;
    private final TeamClient teamClient;
    private final TagService tagService;

    public ArticleServiceImpl(ArticleRepository articleRepository,
                              MsAuthService msAuthService,
                              CommentRepository commentRepository,
                              MailService mailService,
                              TeamClient teamClient,
                              TagService tagService) {
        this.articleRepository = articleRepository;
        this.msAuthService = msAuthService;
        this.commentRepository = commentRepository;
        this.mailService = mailService;
        this.teamClient = teamClient;
        this.tagService = tagService;
    }


    @Override
    public String addDraft(String token, ArticleRequest articleRequest) {
        logger.info("ServiceLog.addDraft.start");
        Long userId = Long.parseLong((String) AuthUtil.getAuthenticatedObject().getPrincipal());

        ArticleEntity draft = ArticleMapper.INSTANCE.requestToEntity(articleRequest);
        draft.setUserId(userId);
        draft.setDraft(true);
        draft.setReadCount(0);
        draft.setHateCount(0);
        draft.setQuackCount(0);
        draft.setApproved(false);
        draft.setApproverId(null);
        draft.setComments(Collections.emptyList());

        if (articleRequest.getType().equals("NEWS")) {
            draft.setTags(Collections.emptyList());
        } else {
            draft.setTags(tagService.getTagsFromRequest(articleRequest.getTags()));
        }

        articleRepository.save(draft);

        logger.info("ServiceLog.addDraft.stop.success");

        return draft.getId();


    }

    @Override
    public void addReadCount(String articleId) {
        logger.info("ServiceLog.addReadCount.start.articleId : {}", articleId);
        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Not found such article"));

        Long userId = articleEntity.getUserId();
        Integer count = articleEntity.getReadCount();
        articleEntity.setReadCount(count + 1);

        msAuthService.addPopularity(userId);
        articleRepository.save(articleEntity);

        logger.info("ServiceLog.addReadCount.stop.success");
    }

    public void deleteArticleById(String articleID) {
        logger.info("ServiceLog.deleteArticleById.start");
        Long userId = Long.parseLong((String) AuthUtil.getAuthenticatedObject().getPrincipal());
        ArticleEntity articleEntity = articleRepository.findById(articleID)
                .orElseThrow(() -> new NoSuchArticleException("Article doesn't exist"));
        Long articleUserId = articleEntity.getUserId();
        if (articleUserId.equals(userId)) {
            if (articleEntity.getComments() != null &&
                    !articleEntity.getComments().isEmpty()) {
                deleteAllComments(articleEntity.getComments());
            }
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
        logger.info("ServiceLog.getArticleById.start with id {}", articleId);
        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoSuchArticleException("Article doesn't exist"));
        Long userId = articleEntity.getUserId();
        if (articleEntity.isDraft()) {
            try {
                if (Long.parseLong(AuthUtil.getAuthenticatedObject().getPrincipal().toString()) != userId) {
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
        if (articleEntity.getComments() != null) {
            articleDTO.setComments(getCommentDTOsWithUserDTO(articleEntity.getComments()));
        }
        articleDTO.setComments(CommentMapper.INSTANCE.entityToDtoList(articleEntity.getComments()));

        logger.info("ServiceLog.getArticleById.end with id {}", articleId);
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

    @Override
    public String publishArticle(String articleId) {
        logger.info("ServiceLog.publishArticle.start with articleId {}", articleId);
        String userId = (String) AuthUtil.getAuthenticatedObject().getPrincipal();
        List<String> memberMails;
        String message;
        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoSuchArticleException("Article doesn't exist with this id" + articleId));

        if (String.valueOf(articleEntity.getUserId()).equals(userId)) {
            if (articleEntity.isDraft()) {
                memberMails = teamClient.getAllMails();
                if (memberMails != null && !memberMails.isEmpty()) {
                    sendMail(articleId, "publish", memberMails);
                    logger.info("ServiceLog.publishArticle.success");
                    message = "Article is sent for reviewing";
                } else {
                    throw new MembersNotFoundException("Sending publish request is failed");
                }
            } else {
                throw new AlreadyPublishedArticleException("Article is already published");
            }
            return message;
        }
        logger.info("ServiceLog.publishArticle.end");
        throw new UnauthorizedAccessException("You don't have permission to publish this article");
    }

    public void sendMail(String articleId, String requestType,
                         List<String> receivers) {
        logger.info("ServiceLog.sendMail.start with articleId {} and requestType: {}", articleId, requestType);
        String mailBody = String.format("Author that has article with id %s wants to %s it.<br>" +
                "Please review article before %s", articleId, requestType, requestType);
        mailService.sendToQueue(MailUtil.buildMail(receivers, mailBody));
        logger.info("ServiceLog.sendMail.end with articleId {} and requestType: {}", articleId, requestType);
    }

    @Override
    public UserArticleDTO getArticlesByUserId(Long userId, int page) {
        logger.info("ServiceLog.getArticlesByUserId.start with userId {}", userId);
        List<ArticleEntity> articleEntities;
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
        try {
            if (Integer.parseInt(AuthUtil.getAuthenticatedObject().getPrincipal().toString()) == userId) {
                articleEntities = articleRepository.getArticleEntitiesByUserId(userId, pageable).getContent();
            } else {
                throw new UnauthorizedAccessException("It's not your article");
            }
        } catch (InvalidTokenException | UnauthorizedAccessException e) {
            articleEntities = articleRepository
                    .getArticleEntitiesByUserIdAndIsDraftFalseAndIsApprovedTrue(userId, pageable).getContent();
        }
        List<ArticleDTO> articleDTOs = ArticleMapper.INSTANCE.entityToDtoList(articleEntities);
        UserDTO userDTO = msAuthService.getUserById(userId);

        logger.info("ServiceLog.getArticlesByUserId.end with userId {}", userId);
        return UserArticleDTO.builder()
                .articleDTOs(articleDTOs)
                .userDTO(userDTO)
                .build();
    }

}
