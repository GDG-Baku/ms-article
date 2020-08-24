package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.mapper.TagMapper;
import az.gdg.msarticle.model.TagRequest;
import az.gdg.msarticle.model.entity.TagEntity;
import az.gdg.msarticle.repository.TagRepository;
import az.gdg.msarticle.service.TagService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

    private static final Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    @Override
    public List<TagEntity> getTagsFromRequest(List<TagRequest> tagRequests) {
        logger.info("ActionLog.getTagsFromRequest.start");
        List<TagEntity> tags = new ArrayList<>();
        for (TagRequest tagRequest : tagRequests) {
            tags.add(saveIfNotExist(tagRequest));
        }
        logger.info("ActionLog.getTagsFromRequest.end");
        return tags;


    }

    @Override
    public TagEntity saveIfNotExist(TagRequest tagRequest) {
        TagEntity tagEntity = tagRepository.findByName(tagRequest.getName());
        if (tagEntity == null) {
            tagEntity = tagRepository.save(TagMapper.INSTANCE.requestToEntity(tagRequest));
        }
        return tagEntity;
    }
}
