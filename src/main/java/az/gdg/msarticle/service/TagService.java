package az.gdg.msarticle.service;

import az.gdg.msarticle.model.TagRequest;
import az.gdg.msarticle.model.entity.TagEntity;

import java.util.List;

public interface TagService {

    List<TagEntity> getTagsFromRequest(List<TagRequest> tagRequests);

    TagEntity saveIfNotExist(TagRequest tagRequest);
}
