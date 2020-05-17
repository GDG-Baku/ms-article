package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.NoAccessException;
import az.gdg.msarticle.exception.NotValidTokenException;
import az.gdg.msarticle.model.entity.TagEntity;
import az.gdg.msarticle.repository.TagRepository;
import az.gdg.msarticle.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class TagServiceImpl implements TagService {
    private static final Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);
    private final TagRepository tagRepository;


    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    @Override
    public void addTag(String tagName) {
        logger.info("ActionLog.addTag.start");
        tagName = tagName.toUpperCase();
        if (getAuthenticatedObject().isAuthenticated()) {
            TagEntity tagEntity = tagRepository.findByName(tagName);
            if (tagEntity == null) {
                tagEntity = TagEntity.builder()
                        .name(tagName)
                        .build();
                tagRepository.save(tagEntity);
            }
        } else {
            logger.error("Thrown.NoAccessException");
            throw new NoAccessException("You don't have access to add tag");
        }
    }

    private Authentication getAuthenticatedObject() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("Thrown.NotValidTokenException");
            throw new NotValidTokenException("Token is not valid or it is expired");
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
