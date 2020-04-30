package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.CommentNotFoundException;
import az.gdg.msarticle.model.entity.CommentEntity;
import az.gdg.msarticle.repository.CommentRepository;
import az.gdg.msarticle.service.CommentService;

import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public String deleteComment(String id) {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(() ->
                new CommentNotFoundException("Comment doesn't exist with this id " + id));

        if (commentEntity.getReplies() != null) {
            commentRepository.deleteAll(commentEntity.getReplies());
        }
        commentRepository.deleteById(id);
        return "Comment is deleted";
    }
}
