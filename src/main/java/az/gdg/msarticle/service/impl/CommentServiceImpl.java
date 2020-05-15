package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.ArticleNotFoundException;
import az.gdg.msarticle.exception.InvalidTokenException;
import az.gdg.msarticle.exception.WrongDataException;
import az.gdg.msarticle.mapper.CommentMapper;
import az.gdg.msarticle.model.CommentRequest;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.model.entity.CommentEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.repository.CommentRepository;
import az.gdg.msarticle.repository.TagRepository;
import az.gdg.msarticle.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              ArticleRepository articleRepository, TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
        this.commentRepository = commentRepository;
    }

    private Authentication getAuthenticatedObject() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new InvalidTokenException("Token is not valid or it is expired");
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }


    @Override
    public void post(String token, CommentRequest commentRequest) {
        logger.info("ActionLog.post.start");

        Optional<ArticleEntity> articleEntity = articleRepository.findById(commentRequest.getArticleId());

        if (articleEntity.isPresent()) {
            String userId = (String) getAuthenticatedObject().getPrincipal();
            ArticleEntity article = articleEntity.get();
            CommentEntity commentEntity = CommentMapper.INSTANCE.requestToEntity(commentRequest);
            commentEntity.setUserId(Integer.parseInt(userId));

            if (article.getComments() != null && !commentRequest.getParentCommentId().isEmpty()) {
                Optional<CommentEntity> parentComment = commentRepository.findById(commentRequest.getParentCommentId());
                commentEntity.setReply(true);
                CommentEntity parent = parentComment.get();

                if (!parent.isReply()) {
                    if (parent.getReplies() != null) {
                        parent.getReplies().add(commentEntity);
                    } else {
                        parent.setReplies(Collections.singletonList(commentEntity));
                    }
                    commentRepository.save(commentEntity);
                    commentRepository.save(parent);

                } else {
                    throw new WrongDataException("Is not allowed writing reply to reply");
                }


            } else {
                commentEntity.setReply(false);
                commentRepository.save(commentEntity);

                if (article.getComments() == null) {
                    article.setComments(Collections.singletonList(commentEntity));
                } else {
                    article.getComments().add(commentEntity);
                }

                articleRepository.save(article);
            }

        } else {
            throw new ArticleNotFoundException("Not found such article!");

        }

        logger.info("ActionLog.post.stop.success");

    }
}
