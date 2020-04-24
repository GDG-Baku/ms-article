package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.model.TagRequest;
import az.gdg.msarticle.repository.TagRepository;
import az.gdg.msarticle.service.TagService;

import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public String createTag(TagRequest tagRequest) {
        //tagRepository.save(TagMapper.INSTANCE.requestToEntity(tagRequest));
        return "Tag is created";
    }
}
